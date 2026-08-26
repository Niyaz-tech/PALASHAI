package com.palashai.viewmodel

import android.app.Application
import android.util.Log
import androidx.lifecycle.AndroidViewModel
import androidx.lifecycle.viewModelScope
import com.palashai.ai.asr.AsrEngine
import com.palashai.ai.audio.AudioRecorder
import com.palashai.ai.translation.DeterministicTranslator
import com.palashai.ai.tts.SantaliTtsEngine
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.Job
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext
import java.io.File
import java.nio.ByteBuffer
import java.nio.ByteOrder

class VoiceTranslationViewModel(application: Application) : AndroidViewModel(application) {

    private val audioRecorder = AudioRecorder(application)
    private val asrEngine = AsrEngine(application)
    private val santaliTtsEngine = SantaliTtsEngine(
        context = application,
        onReady = { isSupported ->
            _isNativeSantaliSupported.value = isSupported
        },
        onSpeakingStateChanged = { isSpeaking ->
            _isTtsSpeaking.value = isSpeaking
        }
    )
    
    private var recordingJob: Job? = null
    private var timerJob: Job? = null
    private val TAG = "PalashASR"

    private val _uiState = MutableStateFlow<RecordingState>(RecordingState.Idle)
    val uiState: StateFlow<RecordingState> = _uiState.asStateFlow()

    private val _recordingDuration = MutableStateFlow(0L)
    val recordingDuration: StateFlow<Long> = _recordingDuration.asStateFlow()

    private val _recognizedText = MutableStateFlow("")
    val recognizedText: StateFlow<String> = _recognizedText.asStateFlow()

    private val _translatedText = MutableStateFlow("")
    val translatedText: StateFlow<String> = _translatedText.asStateFlow()

    private val _isNativeSantaliSupported = MutableStateFlow(false)
    val isNativeSantaliSupported: StateFlow<Boolean> = _isNativeSantaliSupported.asStateFlow()

    private val _isTtsSpeaking = MutableStateFlow(false)
    val isTtsSpeaking: StateFlow<Boolean> = _isTtsSpeaking.asStateFlow()

    sealed class RecordingState {
        object Idle : RecordingState()
        object Initializing : RecordingState()
        object Recording : RecordingState()
        object Processing : RecordingState()
        data class Success(val filePath: String) : RecordingState()
        data class Error(val message: String) : RecordingState()
    }

    init {
        viewModelScope.launch {
            _uiState.value = RecordingState.Initializing
            Log.d(TAG, "Initializing AsrEngine...")
            asrEngine.init().onFailure {
                Log.e(TAG, "AsrEngine init failed: ${it.message}")
                _uiState.value = RecordingState.Error("ASR Init Failed: ${it.message}")
            }.onSuccess {
                Log.d(TAG, "AsrEngine initialized successfully")
                _uiState.value = RecordingState.Idle
            }
        }
    }

    fun startRecording() {
        if (recordingJob?.isActive == true) return

        val outputFile = audioRecorder.getOutputFile()
        Log.d(TAG, "Starting recording to: ${outputFile.name}")
        _recognizedText.value = ""
        _translatedText.value = ""
        _uiState.value = RecordingState.Recording
        _recordingDuration.value = 0L

        timerJob = viewModelScope.launch {
            val startTime = System.currentTimeMillis()
            while (true) {
                kotlinx.coroutines.delay(1000)
                _recordingDuration.value = (System.currentTimeMillis() - startTime) / 1000
            }
        }

        recordingJob = viewModelScope.launch {
            val result = audioRecorder.startRecording(outputFile)
            timerJob?.cancel()
            
            if (result.isSuccess) {
                Log.d(TAG, "Recording stopped. Starting processing flow...")
                processRecording(outputFile)
            } else {
                Log.e(TAG, "Recording failed: ${result.exceptionOrNull()?.message}")
                _uiState.value = RecordingState.Error(result.exceptionOrNull()?.message ?: "Unknown recording error")
            }
        }
    }

    private fun processRecording(file: File) {
        viewModelScope.launch {
            _uiState.value = RecordingState.Processing
            Log.d(TAG, "Processing recording file: ${file.name}")
            
            val transcriptionResult = withContext(Dispatchers.IO) {
                try {
                    val bytes = file.readBytes()
                    Log.d(TAG, "Read ${bytes.size} bytes from PCM file")
                    if (bytes.isEmpty()) return@withContext Result.failure(Exception("Recording is empty"))
                    
                    // Convert PCM bytes to normalized floats
                    val floatArray = convertPcm16ToFloat(bytes)
                    Log.d(TAG, "Captured ${floatArray.size} audio samples. Starting ASR inference...")
                    asrEngine.transcribe(floatArray)
                } catch (e: Exception) {
                    Log.e(TAG, "Conversion/Processing Error: ${e.message}")
                    Result.failure(e)
                }
            }

            transcriptionResult.onSuccess { text ->
                Log.d(TAG, "ASR Success. Recognized text: '$text'")
                _recognizedText.value = text
                
                // Perform Curated MVP Translation
                val translation = DeterministicTranslator.translate(text)
                Log.d(TAG, "Translation engine produced: $translation")
                _translatedText.value = translation
                
                _uiState.value = RecordingState.Success(file.absolutePath)
                
                // Automatically trigger TTS after translation
                speakCurrentTranslation()
            }.onFailure {
                Log.e(TAG, "ASR Inference Failed: ${it.message}")
                _uiState.value = RecordingState.Error("ASR Error: ${it.message}")
            }
        }
    }

    fun speakCurrentTranslation() {
        val textToSpeak = if (santaliTtsEngine.isSantaliSupported) {
            _translatedText.value
        } else {
            _recognizedText.value // Hindi fallback
        }
        
        if (textToSpeak.isNotEmpty()) {
            santaliTtsEngine.speak(textToSpeak)
        }
    }

    fun stopSpeaking() {
        santaliTtsEngine.stop()
    }

    private fun convertPcm16ToFloat(pcmData: ByteArray): FloatArray {
        val shorts = ShortArray(pcmData.size / 2)
        ByteBuffer.wrap(pcmData).order(ByteOrder.LITTLE_ENDIAN).asShortBuffer().get(shorts)
        
        val floats = FloatArray(shorts.size)
        for (i in shorts.indices) {
            floats[i] = shorts[i] / 32768.0f
        }
        return floats
    }

    fun stopRecording() {
        Log.d(TAG, "Stop recording requested by UI")
        audioRecorder.stopRecording()
        // Crucial: Do NOT cancel recordingJob here, as it needs to transition to processing
        timerJob?.cancel()
    }

    override fun onCleared() {
        super.onCleared()
        audioRecorder.stopRecording()
        recordingJob?.cancel()
        timerJob?.cancel()
        asrEngine.release()
        santaliTtsEngine.release()
    }
}
