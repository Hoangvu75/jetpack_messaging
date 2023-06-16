package com.example.kotlinjetpack

import android.content.Intent
import android.os.Bundle
import android.widget.Toast
import androidx.activity.ComponentActivity
import androidx.lifecycle.lifecycleScope
import com.example.kotlinjetpack.view.LoginAndRegisterActivity
import com.google.mlkit.common.model.DownloadConditions
import com.google.mlkit.nl.translate.TranslateLanguage
import com.google.mlkit.nl.translate.Translation
import com.google.mlkit.nl.translate.TranslatorOptions
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.async
import kotlinx.coroutines.awaitAll
import kotlinx.coroutines.launch

class MainActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        setUpLanguage()

        finish()
        startActivity(Intent(this, LoginAndRegisterActivity::class.java))
    }

    private fun setUpLanguage() {
        val langList = arrayOf(
            TranslateLanguage.CHINESE,
            TranslateLanguage.FRENCH,
            TranslateLanguage.GERMAN,
            TranslateLanguage.ITALIAN,
            TranslateLanguage.JAPANESE,
            TranslateLanguage.KOREAN,
            TranslateLanguage.VIETNAMESE,
        )

        fun downloadLanguageKit(languageCode: String) {
            val options = TranslatorOptions
                .Builder()
                .setSourceLanguage(languageCode)
                .setTargetLanguage(TranslateLanguage.ENGLISH)
                .build()
            val translator = Translation.getClient(options)
            val conditions = DownloadConditions
                .Builder()
                .requireWifi()
                .build()

            translator
                .downloadModelIfNeeded(conditions)
                .addOnFailureListener {
                    Toast.makeText(
                        this@MainActivity, "Can't download language kit: $languageCode",
                        Toast.LENGTH_SHORT,
                    ).show()
                }.addOnCompleteListener {
                    if (it.isSuccessful) {
                        Toast.makeText(
                            this@MainActivity, "Downloaded language kit: $languageCode",
                            Toast.LENGTH_SHORT,
                        ).show()
                    } else {
                        Toast.makeText(
                            this@MainActivity, "Can't language kit: $languageCode",
                            Toast.LENGTH_SHORT,
                        ).show()
                    }
                }
        }

        lifecycleScope.launch {
            (langList).map {
                async(Dispatchers.IO) {
                    downloadLanguageKit(
                        languageCode = it
                    )
                }
            }.awaitAll() // waits all of them
        }
    }
}

