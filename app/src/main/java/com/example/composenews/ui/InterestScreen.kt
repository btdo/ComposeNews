package com.example.composenews.ui

import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.material.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.tooling.preview.Preview

@Composable
fun InterestScreen() {
    Column(modifier = Modifier.fillMaxSize()) {
        Text(text = "Interest Screen")
    }
}

@Composable
@Preview
fun InterestScreenPreview(){
    InterestScreen()
}