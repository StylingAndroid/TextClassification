package com.stylingandroid.textclassification

import android.app.PendingIntent
import android.app.RemoteAction
import android.content.Context
import android.content.Intent
import android.graphics.drawable.Icon
import android.net.Uri
import android.view.textclassifier.TextClassification
import android.view.textclassifier.TextSelection
import androidx.annotation.DrawableRes

interface TextClassifierFactory {

    fun buildTextSelection(startIndex: Int, endIndex: Int, entityType: String, confidenceScore: Float): TextSelection

    fun buildTextClassification(
            text: String,
            entityTypes: List<Pair<String, Float>>,
            actions: List<RemoteAction>
    ): TextClassification

    fun buildRemoteAction(
            context: Context,
            @DrawableRes drawableId: Int,
            title: String,
            contentDescription: String,
            uri: String
    ): RemoteAction
}

class FrameworkFactory : TextClassifierFactory {

    override fun buildRemoteAction(
            context: Context,
            drawableId: Int,
            title: String,
            contentDescription: String,
            uri: String
    ): RemoteAction {
        return RemoteAction(
                Icon.createWithResource(context, drawableId),
                title,
                contentDescription,
                PendingIntent.getActivity(
                        context,
                        0,
                        Intent(Intent.ACTION_VIEW, Uri.parse(uri)),
                        0
                )
        )
    }

    override fun buildTextClassification(
            text: String,
            entityTypes: List<Pair<String, Float>>,
            actions: List<RemoteAction>
    ): TextClassification {
        return TextClassification.Builder()
                .run {
                    setText(text)
                    entityTypes.forEach { setEntityType(it.first, it.second) }
                    actions.forEach { addAction(it) }
                    build()
                }
    }

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
