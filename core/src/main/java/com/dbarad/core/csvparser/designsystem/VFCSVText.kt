package com.dbarad.core.csvparser.designsystem

import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign

@Composable
fun VFCSVText(
    modifier: Modifier = Modifier,
    text: String,
    isCenterAligned: Boolean = false,
    fontWeight: FontWeight = FontWeight.Normal
) {
    Text(
        modifier = modifier,
        text = text,
        textAlign = if (isCenterAligned) TextAlign.Center else TextAlign.Start,
        fontWeight = fontWeight
    )
}

@Composable
fun VFCSVTextBold(modifier: Modifier = Modifier, text: String, isCenterAligned: Boolean = false) {
    VFCSVText(modifier, text, isCenterAligned, FontWeight.Bold)
}