package com.palashai.ai.translation.poc

import ai.onnxruntime.*
import android.util.Log
import java.io.File

/**
 * IndicTrans2 Seq2Seq translation engine.
 * Optimized for distilled many-to-many models.
 */
class IndicTransPoc(
    private val modelDir: File,
    private val env: OrtEnvironment
) {
    private val TAG = "IndicTransPoc"
    
    private val EOS_TOKEN_ID = 2L
    private val MAX_LENGTH = 128
    
    // Exact IDs for the distilled 320M model
    private val SAT_OLCK_ID = 29925L 

    private var srcTokenizer: BpeTokenizer
    private var tgtTokenizer: BpeTokenizer

    private var encoderSession: OrtSession? = null
    private var decoderSession: OrtSession? = null
    private var decoderWithPastSession: OrtSession? = null

    init {
        srcTokenizer = BpeTokenizer(File(modelDir, "tokenizer_src.json"))
        tgtTokenizer = BpeTokenizer(File(modelDir, "tokenizer_tgt.json"))
    }

    fun ensureSessionsLoaded() {
        if (encoderSession == null) {
            Log.d(TAG, "Initializing ONNX Sessions from $modelDir")
            val options = OrtSession.SessionOptions()
            // Optimization: Use 2 threads for inference
            options.setIntraOpNumThreads(2)
            
            encoderSession = env.createSession(File(modelDir, "encoder_model.onnx").absolutePath, options)
            decoderSession = env.createSession(File(modelDir, "decoder_model.onnx").absolutePath, options)
            decoderWithPastSession = env.createSession(File(modelDir, "decoder_with_past_model.onnx").absolutePath, options)
        }
    }

    fun translate(hindiText: String): String {
        var encoderResult: OrtSession.Result? = null
        var decoderResult: OrtSession.Result? = null
        var loopResult: OrtSession.Result? = null
        
        try {
            ensureSessionsLoaded()

            // 1. Pre-process
            val normalized = ScriptUnifier.normalize(hindiText)
            
            // Encode Hindi with source tag
            val hindiIds = srcTokenizer.encode("__hin_Deva__ $normalized")
            
            // IndicTrans2 Distilled many-to-many format: [src_tag, ..., sentence, ..., tgt_tag, </s>]
            val inputIdsList = hindiIds.toMutableList()
            inputIdsList.add(SAT_OLCK_ID)
            inputIdsList.add(EOS_TOKEN_ID)
            val inputIds = inputIdsList.toLongArray()
            
            Log.d(TAG, "Encoder Input IDs: ${inputIds.joinToString(", ")}")
            
            // 2. Encoder
            val inputTensor = OnnxTensor.createTensor(env, arrayOf(inputIds))
            val attentionMask = OnnxTensor.createTensor(env, arrayOf(LongArray(inputIds.size) { 1L }))
            
            encoderResult = encoderSession!!.run(mapOf(
                "input_ids" to inputTensor,
                "attention_mask" to attentionMask
            ))
            val hiddenState = encoderResult.get(0) as OnnxTensor
            
            // 3. Initial Decode Step
            // Decoder starts with </s> (ID 2)
            val decoderStartIds = longArrayOf(EOS_TOKEN_ID)
            val decoderInputIds = OnnxTensor.createTensor(env, arrayOf(decoderStartIds))
            
            decoderResult = decoderSession!!.run(mapOf(
                "input_ids" to decoderInputIds,
                "encoder_hidden_states" to hiddenState,
                "encoder_attention_mask" to attentionMask
            ))
            
            val initialLogits = decoderResult.get(0) as OnnxTensor
            var lastTokenId = getArgMax(initialLogits, 0) 
            
            val generated = mutableListOf<Long>()
            Log.d(TAG, "Generation start. Initial token: $lastTokenId")

            if (lastTokenId != EOS_TOKEN_ID) {
                generated.add(lastTokenId)
            } else {
                return "" // Empty translation
            }
            
            // Collect initial KV Cache
            var pastKeyValues = mutableMapOf<String, OnnxTensor>()
            val decoderOutputNames = decoderSession!!.outputNames
            for (i in 1 until decoderResult.size()) {
                val outName = decoderOutputNames.elementAt(i)
                val inName = outName.replace("present", "past_key_values")
                pastKeyValues[inName] = decoderResult.get(i) as OnnxTensor
            }
            
            // 4. Autoregressive Loop
            var step = 0
            while (lastTokenId != EOS_TOKEN_ID && step < MAX_LENGTH) {
                val loopInputIds = OnnxTensor.createTensor(env, arrayOf(longArrayOf(lastTokenId)))
                val loopInputs = mutableMapOf<String, OnnxTensor>()
                loopInputs["input_ids"] = loopInputIds
                loopInputs["encoder_attention_mask"] = attentionMask
                loopInputs.putAll(pastKeyValues)
                
                val currentLoopResult = decoderWithPastSession!!.run(loopInputs)
                val loopLogits = currentLoopResult.get(0) as OnnxTensor
                
                lastTokenId = getArgMax(loopLogits, 0)
                Log.d(TAG, "Step $step: generated $lastTokenId")

                if (lastTokenId == EOS_TOKEN_ID) {
                    currentLoopResult.close()
                    loopInputIds.close()
                    break
                }
                
                generated.add(lastTokenId)
                
                // Prepare next pastKeyValues
                val nextPastKeyValues = mutableMapOf<String, OnnxTensor>()
                val loopOutputNames = decoderWithPastSession!!.outputNames
                for (i in 1 until currentLoopResult.size()) {
                    val outName = loopOutputNames.elementAt(i)
                    val inName = outName.replace("present", "past_key_values")
                    nextPastKeyValues[inName] = currentLoopResult.get(i) as OnnxTensor
                }
                
                // Result Handover: Close previous results to save memory
                if (step == 0) {
                    decoderResult?.close()
                } else {
                    loopResult?.close()
                }
                
                loopResult = currentLoopResult
                pastKeyValues = nextPastKeyValues
                loopInputIds.close()
                step++
            }
            
            // 5. Post-process
            val rawOutput = tgtTokenizer.decode(generated.toLongArray())
            Log.d(TAG, "Final raw output: $rawOutput")
            return rawOutput
            
        } catch (e: Exception) {
            Log.e(TAG, "Translation loop failed: ${e.message}", e)
            throw e
        } finally {
            encoderResult?.close()
            decoderResult?.close()
            loopResult?.close()
        }
    }

    private fun getArgMax(logits: OnnxTensor, tokenIndex: Int): Long {
        val shape = logits.info.shape
        val vocabSize = shape[2].toInt()
        
        val floatBuffer = logits.floatBuffer
        val offset = (tokenIndex * vocabSize)
        floatBuffer.position(offset)
        
        var maxIdx = 0
        var maxVal = Float.NEGATIVE_INFINITY
        
        for (i in 0 until vocabSize) {
            val v = floatBuffer.get()
            if (v > maxVal) {
                maxVal = v
                maxIdx = i
            }
        }
        return maxIdx.toLong()
    }

    fun release() {
        Log.d(TAG, "Releasing IndicTransPoc sessions")
        encoderSession?.close()
        decoderSession?.close()
        decoderWithPastSession?.close()
        encoderSession = null
        decoderSession = null
        decoderWithPastSession = null
    }
}
