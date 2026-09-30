package com.palashai.viewmodel

import android.app.Application
import android.util.Log
import androidx.lifecycle.AndroidViewModel
import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import com.palashai.ai.tts.SantaliTtsEngine
import com.palashai.data.Flashcard
import com.palashai.data.FlashcardTopic
import com.palashai.repository.FlashcardRepository
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow

class FlashcardViewModel(application: Application) : AndroidViewModel(application) {

    private val TAG = "PalashFlashcards"
    
    private val _selectedClass = MutableStateFlow(1)
    val selectedClass: StateFlow<Int> = _selectedClass.asStateFlow()

    private val _availableTopics = MutableStateFlow<List<FlashcardTopic>>(emptyList())
    val availableTopics: StateFlow<List<FlashcardTopic>> = _availableTopics.asStateFlow()

    private val _selectedTopic = MutableStateFlow<FlashcardTopic?>(null)
    val selectedTopic: StateFlow<FlashcardTopic?> = _selectedTopic.asStateFlow()

    private val _currentIndex = MutableStateFlow(0)
    val currentIndex: StateFlow<Int> = _currentIndex.asStateFlow()

    private val _isFlipped = MutableStateFlow(false)
    val isFlipped: StateFlow<Boolean> = _isFlipped.asStateFlow()

    private val _isTtsSpeaking = MutableStateFlow(false)
    val isTtsSpeaking: StateFlow<Boolean> = _isTtsSpeaking.asStateFlow()

    private val _isNativeSantaliSupported = MutableStateFlow(false)
    val isNativeSantaliSupported: StateFlow<Boolean> = _isNativeSantaliSupported.asStateFlow()

    private val santaliTtsEngine = SantaliTtsEngine(
        context = application,
        onReady = { isSupported ->
            Log.d(TAG, "TTS Engine ready. Santali supported: $isSupported")
            _isNativeSantaliSupported.value = isSupported
        },
        onSpeakingStateChanged = { isSpeaking ->
            _isTtsSpeaking.value = isSpeaking
        }
    )

    init {
        loadTopics(1)
    }

    fun selectClass(classLevel: Int) {
        _selectedClass.value = classLevel
        loadTopics(classLevel)
    }

    private fun loadTopics(classLevel: Int) {
        val topics = FlashcardRepository.getTopicsByClass(classLevel)
        _availableTopics.value = topics
        if (topics.isNotEmpty()) {
            selectTopic(topics[0])
        } else {
            _selectedTopic.value = null
        }
    }

    fun selectTopic(topic: FlashcardTopic) {
        _selectedTopic.value = topic
        _currentIndex.value = 0
        _isFlipped.value = false
    }

    fun nextCard() {
        _selectedTopic.value?.let { topic ->
            if (_currentIndex.value < topic.cards.size - 1) {
                _currentIndex.value++
                _isFlipped.value = false
            }
        }
    }

    fun previousCard() {
        if (_currentIndex.value > 0) {
            _currentIndex.value--
            _isFlipped.value = false
        }
    }

    fun toggleFlip() {
        _isFlipped.value = !_isFlipped.value
    }

    fun speakFlashcard(card: Flashcard, isQuestionSide: Boolean) {
        if (!santaliTtsEngine.isInitialized) {
            Log.e(TAG, "TTS Engine not initialized yet")
            return
        }

        val textToSpeak = if (isQuestionSide) {
            card.hindi
        } else {
            card.santali
        }

        Log.d(TAG, "Speaking: $textToSpeak")
        santaliTtsEngine.speak(textToSpeak)
    }

    fun stopSpeaking() {
        santaliTtsEngine.stop()
    }

    override fun onCleared() {
        super.onCleared()
        santaliTtsEngine.release()
    }
}

class FlashcardViewModelFactory(private val application: Application) : ViewModelProvider.Factory {
    override fun <T : ViewModel> create(modelClass: Class<T>): T {
        if (modelClass.isAssignableFrom(FlashcardViewModel::class.java)) {
            @Suppress("UNCHECKED_CAST")
            return FlashcardViewModel(application) as T
        }
        throw IllegalArgumentException("Unknown ViewModel class")
    }
}
