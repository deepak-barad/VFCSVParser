package com.dbarad.presentation.csvparser.screens

import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.Button
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.unit.dp
import androidx.hilt.navigation.compose.hiltViewModel
import com.dbarad.presentation.csvparser.viewmodels.ParserViewModel
import com.dbarad.presentation.csvparser.viewstates.ParseViewState

@Composable
fun ParserScreen(viewModel: ParserViewModel = hiltViewModel()) {
    var input by remember { mutableStateOf("") }
    var output by remember { mutableStateOf("") }
    val parserViewState by viewModel.parserViewState.collectAsState()

    OutlinedTextField(
        value = input,
        onValueChange = { input = it },
        label = { Text("Input CSV") },
        modifier = Modifier
            .fillMaxWidth()
            .padding(16.dp)
    )
    Spacer(Modifier.height(8.dp))
    Button(modifier = Modifier.padding(16.dp), onClick = {
        try {
            viewModel.parseInput(input)
        } catch (e: Exception) {
            output = "Error: ${e.message}"
        }
    }) {
        Text("Parse")
    }
    Spacer(Modifier.height(8.dp))
    Text(modifier = Modifier.padding(16.dp), text = "Json output:")


    when (parserViewState) {
        ParseViewState.DoNothing -> {

        }

        ParseViewState.Loading -> {
            CircularProgressIndicator(color = Color.Blue)
        }

        is ParseViewState.Success -> {
            output = parserViewState.toString()
            Text(modifier = Modifier.padding(16.dp), text = output)
        }
    }
}
