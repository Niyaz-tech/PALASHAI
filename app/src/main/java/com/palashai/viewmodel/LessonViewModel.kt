package com.palashai.viewmodel

import android.app.Application
import android.util.Log
import androidx.lifecycle.AndroidViewModel
import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import androidx.lifecycle.viewModelScope
import com.palashai.ai.tts.SantaliTtsEngine
import com.palashai.data.Lesson
import com.palashai.repository.LessonRepository
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.flow.*
import kotlinx.coroutines.launch

@OptIn(ExperimentalCoroutinesApi::class)
class LessonViewModel(
    application: Application,
    private val repository: LessonRepository
) : AndroidViewModel(application) {
    
    private val TAG = "PalashCurriculum"
    
    private val _lessons = MutableStateFlow<List<Lesson>>(emptyList())
    val lessons: StateFlow<List<Lesson>> = _lessons.asStateFlow()

    private val _selectedLesson = MutableStateFlow<Lesson?>(null)
    val selectedLesson: StateFlow<Lesson?> = _selectedLesson.asStateFlow()

    private val _currentClassLevel = MutableStateFlow(1)

    private val _isTtsSpeaking = MutableStateFlow(false)
    val isTtsSpeaking: StateFlow<Boolean> = _isTtsSpeaking.asStateFlow()

    private val santaliTtsEngine = SantaliTtsEngine(
        context = application,
        onReady = { /* No-op */ },
        onSpeakingStateChanged = { isSpeaking ->
            _isTtsSpeaking.value = isSpeaking
        }
    )

    init {
        // Observe lessons for the current class level
        _currentClassLevel
            .flatMapLatest { classLevel ->
                Log.d(TAG, "Loading lessons for class level: $classLevel")
                repository.getLessonsByClass(classLevel)
            }
            .onEach { 
                Log.d(TAG, "Received ${it.size} lessons from repository")
                _lessons.value = it 
            }
            .launchIn(viewModelScope)
    }

    fun loadLessonsByClass(classLevel: Int) {
        _currentClassLevel.value = classLevel
    }

    fun getLessonById(id: String) {
        Log.d(TAG, "Requested lesson ID: $id")
        // Clear previous lesson to show loading indicator and avoid stale data
        _selectedLesson.value = null
        
        viewModelScope.launch {
            val lesson = repository.getLessonById(id)
            if (lesson != null) {
                Log.d(TAG, "Lesson loaded: ${lesson.topic}")
                _selectedLesson.value = lesson
            } else {
                Log.e(TAG, "Lesson not found for ID: $id")
            }
        }
    }

    fun speakLesson(text: String) {
        if (santaliTtsEngine.isInitialized) {
            Log.d(TAG, "Starting TTS for lesson text")
            santaliTtsEngine.speak(text)
        } else {
            Log.e(TAG, "TTS Engine not initialized")
        }
    }

    fun stopSpeaking() {
        santaliTtsEngine.stop()
    }

    override fun onCleared() {
        super.onCleared()
        santaliTtsEngine.release()
    }
}

class LessonViewModelFactory(
    private val application: Application,
    private val repository: LessonRepository
) : ViewModelProvider.Factory {
    override fun <T : ViewModel> create(modelClass: Class<T>): T {
        if (modelClass.isAssignableFrom(LessonViewModel::class.java)) {
            @Suppress("UNCHECKED_CAST")
            return LessonViewModel(application, repository) as T
        }
        throw IllegalArgumentException("Unknown ViewModel class")
    }
}
