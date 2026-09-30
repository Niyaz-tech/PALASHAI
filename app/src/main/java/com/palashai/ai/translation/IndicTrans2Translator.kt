package com.palashai.ai.translation

import android.content.Context
import android.util.Log
import ai.onnxruntime.OrtEnvironment
import com.palashai.ai.translation.poc.IndicTransPoc
import java.io.File

/**
 * Production-ready wrapper for the IndicTrans2 Hindi-to-Santali translation engine.
 * Reuses the validated POC implementation for inference.
 */
class IndicTrans2Translator(private val context: Context) {
    
    private val TAG = "IndicTrans2Translator"
    private var engine: IndicTransPoc? = null
    private val env = OrtEnvironment.getEnvironment()

    /**
     * Initializes the translation engine by loading tokenizers and ONNX sessions.
     * Should be called from a background thread.
     */
    fun init(): Result<Unit> {
        return try {
            val modelDir = File(context.getExternalFilesDir(null), "indictrans_poc")
            if (!modelDir.exists() || !modelDir.isDirectory) {
                return Result.failure(Exception("IndicTrans2 model directory not found at ${modelDir.absolutePath}"))
            }

            Log.d(TAG, "Initializing IndicTrans2 engine...")
            engine = IndicTransPoc(modelDir, env)
            engine?.ensureSessionsLoaded()
            
            Log.d(TAG, "IndicTrans2 engine initialized successfully")
            Result.success(Unit)
        } catch (e: Exception) {
            Log.e(TAG, "Failed to initialize IndicTrans2 engine", e)
            Result.failure(e)
        }
    }

    /**
     * Translates Hindi text to Santali (Ol Chiki).
     * Falls back to DeterministicTranslator if the engine is not initialized or fails.
     */
    fun translate(hindiText: String): String {
        val currentEngine = engine
        if (currentEngine == null) {
            Log.w(TAG, "Engine not initialized, falling back to DeterministicTranslator")
            return DeterministicTranslator.translate(hindiText)
        }

        return try {
            Log.d(TAG, "Translating: $hindiText")
            val result = currentEngine.translate(hindiText)
            Log.d(TAG, "Translation result: $result")
            result
        } catch (e: Exception) {
            Log.e(TAG, "IndicTrans2 translation failed, falling back", e)
            DeterministicTranslator.translate(hindiText)
        }
    }

    /**
     * Releases the engine and ONNX environment.
     */
    fun release() {
        Log.d(TAG, "Releasing translator resources")
        engine?.release()
        engine = null
        env.close()
    }
}
