package com.palashai.ai.tts

import android.content.Context
import android.speech.tts.TextToSpeech
import android.speech.tts.UtteranceProgressListener
import android.util.Log
import java.util.Locale

class SantaliTtsEngine(
    context: Context, 
    private val onReady: (Boolean) -> Unit,
    private val onSpeakingStateChanged: (Boolean) -> Unit
) : TextToSpeech.OnInitListener {

    private var tts: TextToSpeech? = TextToSpeech(context, this)
    var isInitialized = false
        private set
    
    var isSantaliSupported = false
        private set

    private val TAG = "PalashTTS"

    override fun onInit(status: Int) {
        if (status == TextToSpeech.SUCCESS) {
            val santaliLocale = Locale("sat", "IN")
            val result = tts?.isLanguageAvailable(santaliLocale)
            
            if (result == TextToSpeech.LANG_AVAILABLE || result == TextToSpeech.LANG_COUNTRY_AVAILABLE) {
                tts?.language = santaliLocale
                isSantaliSupported = true
                Log.d(TAG, "Native Santali TTS is supported")
            } else {
                tts?.language = Locale("hi", "IN")
                isSantaliSupported = false
                Log.d(TAG, "Native Santali TTS NOT supported. Falling back to Hindi.")
            }
            
            setupProgressListener()
            isInitialized = true
            onReady(isSantaliSupported)
        } else {
            Log.e(TAG, "TTS Initialization failed")
        }
    }

    private fun setupProgressListener() {
        tts?.setOnUtteranceProgressListener(object : UtteranceProgressListener() {
            override fun onStart(utteranceId: String?) {
                onSpeakingStateChanged(true)
            }

            override fun onDone(utteranceId: String?) {
                onSpeakingStateChanged(false)
            }

            @Deprecated("Deprecated in Java")
            override fun onError(utteranceId: String?) {
                onSpeakingStateChanged(false)
            }
        })
    }

    fun speak(text: String) {
        if (isInitialized) {
            val params = android.os.Bundle()
            params.putString(TextToSpeech.Engine.KEY_PARAM_UTTERANCE_ID, "UtteranceID")
            tts?.speak(text, TextToSpeech.QUEUE_FLUSH, params, "UtteranceID")
        }
    }

    fun stop() {
        tts?.stop()
        onSpeakingStateChanged(false)
    }

    fun release() {
        tts?.stop()
        tts?.shutdown()
        tts = null
        isInitialized = false
    }
}
