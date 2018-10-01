package com.stylingandroid.textclassification

import android.content.Context
import android.view.textclassifier.TextClassification
import android.view.textclassifier.TextClassifier
import android.view.textclassifier.TextSelection

class StylingAndroidTextClassifier(
        private val context: Context,
        private val fallback: TextClassifier,
        private val factory: TextClassifierFactory = FrameworkFactory()
) : TextClassifier by fallback {

    private val stylingAndroid = "Styling Android"
    private val stylingAndroidUri = "https://blog.stylingandroid.com"
    private val regex = Regex("Styling\\s?Android", RegexOption.IGNORE_CASE)

    override fun suggestSelection(request: TextSelection.Request): TextSelection {
        return findRangeOfMatch(request)
                ?: fallback.suggestSelection(request)
    }

    private fun findRangeOfMatch(request: TextSelection.Request): TextSelection? {
        return regex.findAll(request.text)
                .firstOrNull { it.range.contains(request.startIndex until request.endIndex) }
                ?.range
                ?.let {
                    factory.buildTextSelection(it.start, it.endInclusive + 1, TextClassifier.TYPE_URL, 1.0f)
                }
    }

    private fun <T : Comparable<T>> ClosedRange<T>.contains(range: ClosedRange<T>) =
            contains(range.start) && contains(range.endInclusive)

    override fun classifyText(request: TextClassification.Request): TextClassification {
        return fallback.classifyText(request)
    }
}
