package com.palashai.ai.translation.poc

import ai.onnxruntime.OrtEnvironment
import android.util.Log
import java.io.File

/**
 * Utility to run the isolated IndicTrans2 POC translation test.
 */
object PocTester {
    private const val TAG = "PalashPoc"

    /**
     * Executes the POC test on a supply Hindi string.
     * @param modelDirPath Local path to the directory containing ONNX models and JSONs.
     */
    fun runTest(modelDirPath: String) {
        Log.d(TAG, "--- STARTING POC TEST ---")
        val env = OrtEnvironment.getEnvironment()
        val modelDir = File(modelDirPath)
        
        try {
            if (!modelDir.exists()) {
                Log.e(TAG, "Model directory not found: $modelDirPath")
                return
            }

            val poc = IndicTransPoc(modelDir, env)
            
            val testInput = "नमस्ते"
            Log.d(TAG, "Input Hindi: $testInput")
            
            val startTime = System.currentTimeMillis()
            val result = poc.translate(testInput)
            val duration = System.currentTimeMillis() - startTime
            
            Log.d(TAG, "Output Santali (Ol Chiki): $result")
            Log.d(TAG, "Translation took: ${duration}ms")
            Log.d(TAG, "--- POC TEST SUCCESS ---")
            
        } catch (e: Exception) {
            Log.e(TAG, "--- POC TEST FAILED ---")
            Log.e(TAG, e.message ?: "Unknown error")
        } finally {
            env.close()
        }
    }
}
