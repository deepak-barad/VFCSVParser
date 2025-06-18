package com.dbarad.core.csvparser.designsystem

import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.Card
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp

@Composable
fun VFCSVDisplayCard(modifier: Modifier = Modifier, content: @Composable () -> Unit) {
    Card(shape = RoundedCornerShape(topStart = 48.dp, topEnd = 48.dp)) {
        content()
    }
}