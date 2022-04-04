package com.example.composenews.ui

import androidx.compose.material.Text
import androidx.compose.runtime.Composable
import com.example.composenews.models.ViewMoreCategory

@Composable
fun ViewMoreScreen(category: ViewMoreCategory) {
    Text(text = "ViewMore: ${category.name}")
}