package com.palashai.ai.asr

import android.content.Context
import android.content.res.AssetManager
import android.util.Log
import com.k2fsa.sherpa.onnx.*

/**
 * AsrEngine handles offline speech-to-text recognition using Sherpa-ONNX.
 * It is configured to use the AI4Bharat Hindi IndicConformer model.
 */
class AsrEngine(private val context: Context) {

    private var recognizer: OfflineRecognizer? = null

    companion object {
        private const val TAG = "AsrEngine"
        private const val MODEL_PATH = "asr/model.int8.onnx"
        private const val TOKENS_PATH = "asr/tokens.txt"
        private const val SAMPLE_RATE = 16000
    }

    /**
     * Initializes the OfflineRecognizer.
     * This method loads the model and tokens from the app's assets.
     * Should be called from a background thread to avoid UI lag.
     */
    fun init(): Result<Unit> {
        return try {
            val assetManager = context.assets
            
            // Verify model and tokens exist in assets
            if (!assetExists(assetManager, MODEL_PATH)) {
                return Result.failure(Exception("ASR model file not found in assets: $MODEL_PATH"))
            }
            if (!assetExists(assetManager, TOKENS_PATH)) {
                return Result.failure(Exception("ASR tokens file not found in assets: $TOKENS_PATH"))
            }

            val modelConfig = OfflineModelConfig(
                nemo = OfflineNemoEncDecCtcModelConfig(
                    model = MODEL_PATH
                ),
                tokens = TOKENS_PATH,
                numThreads = 2,
                debug = false,
                provider = "cpu",
                modelType = "nemo_ctc"
            )

            val featConfig = FeatureConfig(
                sampleRate = SAMPLE_RATE,
                featureDim = 80
            )

            val config = OfflineRecognizerConfig(
                modelConfig = modelConfig,
                featConfig = featConfig,
                decodingMethod = "greedy_search"
            )

            recognizer = OfflineRecognizer(assetManager, config)
            Log.d(TAG, "OfflineRecognizer initialized successfully with nemo_ctc")
            Result.success(Unit)
        } catch (e: Exception) {
            Log.e(TAG, "Failed to initialize OfflineRecognizer", e)
            Result.failure(e)
        }
    }

    /**
     * Transcribes normalized PCM audio samples (FloatArray) into text.
     * @param samples Normalized audio samples (-1.0 to 1.0) at 16kHz.
     * @return Result containing the recognized Hindi text.
     */
    fun transcribe(samples: FloatArray): Result<String> {
        val recognizer = this.recognizer ?: return Result.failure(Exception("ASR Engine not initialized"))

        return try {
            Log.d("PalashASR", "Starting ASR inference for ${samples.size} samples")
            val stream = recognizer.createStream()
            stream.acceptWaveform(samples, SAMPLE_RATE)
            
            recognizer.decode(stream)
            val result = recognizer.getResult(stream)
            
            val text = result.text
            stream.release()
            
            Log.d("PalashASR", "ASR inference finished. Text: $text")
            Result.success(text)
        } catch (e: Exception) {
            Log.e("PalashASR", "ASR transcription failed", e)
            Result.failure(e)
        }
    }

    /**
     * Checks if a file exists in the Android assets.
     */
    private fun assetExists(assetManager: AssetManager, path: String): Boolean {
        return try {
            assetManager.open(path).use { true }
        } catch (e: Exception) {
            false
        }
    }

    /**
     * Releases the native recognizer resources.
     */
    fun release() {
        recognizer?.release()
        recognizer = null
        Log.d(TAG, "ASR Engine resources released")
    }
}
