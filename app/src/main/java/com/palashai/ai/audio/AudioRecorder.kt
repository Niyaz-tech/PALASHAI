package com.palashai.ai.audio

import android.annotation.SuppressLint
import android.content.Context
import android.media.AudioFormat
import android.media.AudioRecord
import android.media.MediaRecorder
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.isActive
import kotlinx.coroutines.withContext
import java.io.File
import java.io.FileOutputStream
import java.io.IOException
import java.util.concurrent.atomic.AtomicBoolean

class AudioRecorder(private val context: Context) {

    companion object {
        const val SAMPLE_RATE = 16000
        const val CHANNEL_CONFIG = AudioFormat.CHANNEL_IN_MONO
        const val AUDIO_FORMAT = AudioFormat.ENCODING_PCM_16BIT
    }

    private val isRecording = AtomicBoolean(false)
    private var audioRecord: AudioRecord? = null
    private val bufferSize = AudioRecord.getMinBufferSize(SAMPLE_RATE, CHANNEL_CONFIG, AUDIO_FORMAT)

    @SuppressLint("MissingPermission")
    suspend fun startRecording(outputFile: File): Result<Unit> = withContext(Dispatchers.IO) {
        if (isRecording.get()) return@withContext Result.failure(IllegalStateException("Already recording"))

        try {
            audioRecord = AudioRecord(
                MediaRecorder.AudioSource.MIC,
                SAMPLE_RATE,
                CHANNEL_CONFIG,
                AUDIO_FORMAT,
                bufferSize
            )

            if (audioRecord?.state != AudioRecord.STATE_INITIALIZED) {
                return@withContext Result.failure(IOException("AudioRecord initialization failed"))
            }

            audioRecord?.startRecording()
            isRecording.set(true)

            FileOutputStream(outputFile).use { outputStream ->
                val audioBuffer = ByteArray(bufferSize)
                while (isRecording.get() && isActive) {
                    val read = audioRecord?.read(audioBuffer, 0, audioBuffer.size) ?: -1
                    if (read > 0) {
                        outputStream.write(audioBuffer, 0, read)
                    } else if (read < 0) {
                        throw IOException("Error reading audio data: $read")
                    }
                }
            }
            Result.success(Unit)
        } catch (e: Exception) {
            Result.failure(e)
        } finally {
            stopRecording()
        }
    }

    fun stopRecording() {
        isRecording.set(false)
        audioRecord?.apply {
            if (state == AudioRecord.RECORDSTATE_RECORDING) {
                stop()
            }
            release()
        }
        audioRecord = null
    }

    fun getOutputFile(): File {
        val fileName = "recording_${System.currentTimeMillis()}.pcm"
        return File(context.cacheDir, fileName)
    }
}
