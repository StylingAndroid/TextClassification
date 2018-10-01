package com.stylingandroid.textclassification

import android.app.RemoteAction
import android.content.Context
import android.view.textclassifier.TextClassification
import android.view.textclassifier.TextClassifier
import android.view.textclassifier.TextSelection
import com.nhaarman.mockitokotlin2.any
import com.nhaarman.mockitokotlin2.never
import com.nhaarman.mockitokotlin2.times
import com.nhaarman.mockitokotlin2.verify
import com.nhaarman.mockitokotlin2.whenever
import org.assertj.core.api.Assertions.assertThat
import org.junit.Before
import org.junit.Test
import org.mockito.Mockito.mock

class StylingAndroidTextClassifierTest {

    private val saString = "This is a string containing Styling Android."
    private val nonSaString = "This is a string containing something else entirely."

    private val context = mock(Context::class.java)
    private val selectionBuilder = mock(TextClassifierFactory::class.java)
    private val fallback = mock(TextClassifier::class.java)
    private val selection = mock(TextSelection::class.java)
    private val classification = mock(TextClassification::class.java)
    private val selectionRequest = mock(TextSelection.Request::class.java)
    private val classificationRequest = mock(TextClassification.Request::class.java)
    private val remoteAction = mock(RemoteAction::class.java)

    private lateinit var classifier: StylingAndroidTextClassifier


    @Before
    fun setUp() {
        classifier = StylingAndroidTextClassifier(context, fallback, selectionBuilder)
        whenever(selectionRequest.startIndex).thenReturn(30)
        whenever(selectionRequest.endIndex).thenReturn(31)
        whenever(selectionBuilder.buildTextSelection(any(), any(), any(), any())).thenReturn(selection)
        whenever(classificationRequest.startIndex).thenReturn(28)
        whenever(classificationRequest.endIndex).thenReturn(43)
        whenever(selectionBuilder.buildTextClassification(any(), any(), any())).thenReturn(classification)
        whenever(selectionBuilder.buildRemoteAction(any(), any(), any(), any(), any())).thenReturn(remoteAction)
        whenever(fallback.classifyText(any())).thenReturn(classification)
        whenever(fallback.suggestSelection(any())).thenReturn(selection)
    }

    @Test
    fun `when the text contains 'Styling Android' then the appropriate selection is returned`() {
        whenever(selectionRequest.text).thenReturn(saString)

        assertThat(classifier.suggestSelection(selectionRequest)).isNotNull

        verify(selectionBuilder, times(1)).buildTextSelection(28, 43, TextClassifier.TYPE_URL, 1.0f)
        verify(fallback, never()).suggestSelection(any())
        assertThat(saString.substring(28..42)).isEqualTo("Styling Android")
    }

    @Test
    fun `when the text does not contain 'Styling Android' then the appropriate selection is returned`() {
        whenever(selectionRequest.text).thenReturn(nonSaString)

        assertThat(classifier.suggestSelection(selectionRequest)).isNotNull

        verify(selectionBuilder, never()).buildTextSelection(any(), any(), any(), any())
        verify(fallback, times(1)).suggestSelection(any())
    }

    @Test
    fun `when the text contains 'Styling Android' then the appropriate classification is returned`() {
        whenever(classificationRequest.text).thenReturn(saString)

        assertThat(classifier.classifyText(classificationRequest)).isNotNull

        verify(selectionBuilder, times(1)).buildTextClassification("Styling Android", listOf(TextClassifier.TYPE_URL to 1.0f), listOf(remoteAction))
        verify(fallback, never()).classifyText(any())
    }

    @Test
    fun `when the text does not contain 'Styling Android' then the appropriate classification is returned`() {
        whenever(classificationRequest.text).thenReturn(nonSaString)

        assertThat(classifier.classifyText(classificationRequest)).isNotNull

        verify(selectionBuilder, never()).buildTextClassification(any(), any(), any())
        verify(fallback, times(1)).classifyText(any())
    }
}
