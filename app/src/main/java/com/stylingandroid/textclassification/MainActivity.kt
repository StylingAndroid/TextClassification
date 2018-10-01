package com.stylingandroid.textclassification

import android.content.Context
import android.os.Bundle
import android.os.LocaleList
import android.view.textclassifier.TextClassificationManager
import androidx.appcompat.app.AppCompatActivity
import kotlinx.android.synthetic.main.activity_main.*
import kotlinx.coroutines.experimental.CommonPool
import kotlinx.coroutines.experimental.async

class MainActivity : AppCompatActivity() {

    private val emailText = "dummy@email.com"
    private val urlText = "https://blog.stylingandroid.com"
    private val hybridText = "Email: $emailText"

    private lateinit var textClassificationManager: TextClassificationManager

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        textClassificationManager = getSystemService(Context.TEXT_CLASSIFICATION_SERVICE) as TextClassificationManager

        classifier()

        text1.textClassifier = StylingAndroidTextClassifier(this, textClassificationManager.textClassifier)
    }

    private fun classifier() = async(CommonPool) {
        val textClassifier = textClassificationManager.textClassifier
        val emailClassification = textClassifier.classifyText(emailText, 0, emailText.length, LocaleList.getDefault())
        println(emailClassification)
        val urlClassification = textClassifier.classifyText(urlText, 0, urlText.length, LocaleList.getDefault())
        println(urlClassification)
        val suggestions = textClassifier.suggestSelection(hybridText, 10, 11, LocaleList.getDefault())
        println(suggestions)
    }
}
