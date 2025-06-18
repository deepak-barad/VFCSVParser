package com.dbarad.core.csvparser.designsystem

import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.vector.ImageVector
import com.dbarad.core.csvparser.common.COPY_OUTPUT

@Composable
fun VFCSVIconButton(modifier: Modifier = Modifier, onClick: () -> Unit, imageVector: ImageVector) {
    IconButton(modifier = modifier, onClick = {
        onClick()
    }) {
        Icon(
            imageVector = imageVector,
            contentDescription = COPY_OUTPUT
        )
    }
}