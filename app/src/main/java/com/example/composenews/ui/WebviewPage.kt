package com.example.composenews.ui

import android.view.ViewGroup
import android.webkit.WebView
import android.webkit.WebViewClient
import androidx.compose.runtime.Composable
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.viewinterop.AndroidView

@Composable
fun WebviewPage(url: String) {
    AndroidView(factory = {
        val webview = WebView(it).apply {
            layoutParams = ViewGroup.LayoutParams(
                ViewGroup.LayoutParams.MATCH_PARENT,
                ViewGroup.LayoutParams.MATCH_PARENT
            )
            webViewClient = WebViewClient()
        }
        webview
    }, update = {
        it.loadUrl(url)
    })
}

@Preview(showBackground = true)
@Composable
fun WebviewPreview() {
    WebviewPage(url = "https://www.google.ca")
}