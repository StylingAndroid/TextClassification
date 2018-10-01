package com.stylingandroid.textclassification

import android.view.textclassifier.TextSelection

interface TextClassifierFactory {

    fun buildTextSelection(startIndex: Int, endIndex: Int, entityType: String, confidenceScore: Float): TextSelection
}

class FrameworkFactory : TextClassifierFactory {

    override fun buildTextSelection(
            startIndex: Int,
            endIndex: Int,
            entityType: String,
            confidenceScore: Float
    ): TextSelection {
        return TextSelection.Builder(startIndex, endIndex)
                .setEntityType(entityType, confidenceScore)
                .build()
    }
}
