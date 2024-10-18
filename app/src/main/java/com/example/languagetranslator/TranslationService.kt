package com.example.languagetranslator

import android.app.Service
import android.content.Intent
import android.os.Binder
import android.os.IBinder
import com.google.mlkit.nl.translate.TranslateLanguage
import com.google.mlkit.nl.translate.Translation
import com.google.mlkit.nl.translate.TranslatorOptions

class TranslationService : Service() {
    val binder by lazy {
        TranslationBinder()
    }

    inner class TranslationBinder: Binder(){
        fun getService() : TranslationService = this@TranslationService
    }

    override fun onBind(intent: Intent?): IBinder? {
        // clients get bound to the service
        return binder
    }

    override fun onCreate() {
        super.onCreate()
        // Initialize the service
    }

    override fun onUnbind(intent: Intent?): Boolean {
        return super.onUnbind(intent)
        // all clients unbound by calling this
        // if you want to trigger onRebind, return true
    }

    override fun onRebind(intent: Intent?) {
        super.onRebind(intent)
        // there is a very small time gap between onUnbind and onDestroy
        // in between them, if a client binds to the service again, onRebind is called
    }

    override fun onDestroy() {
        super.onDestroy()
        // clean up the service
    }

    fun translate(text: String, onSuccess: (String) -> Unit, onFailure: (Exception) -> Unit){
        val options = TranslatorOptions.Builder()
            .setSourceLanguage(TranslateLanguage.ENGLISH)
            .setTargetLanguage(TranslateLanguage.BENGALI)
            .build()
        val englishToBengaliTranslator = Translation.getClient(options)
        englishToBengaliTranslator.downloadModelIfNeeded()
            .addOnSuccessListener {
                englishToBengaliTranslator.translate(text)
                    .addOnSuccessListener { translatedText ->
                        onSuccess(translatedText)
                    }
                    .addOnFailureListener { exception ->
                        onFailure(exception)
                    }
            }
            .addOnFailureListener { exception ->
                onFailure(exception)
            }
    }


}