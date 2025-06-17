package com.dbarad.presentation.csvparser.screens

import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.LazyColumn
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
import com.dbarad.core.csvparser.common.ERROR
import com.dbarad.core.csvparser.common.JSON_OUTPUT
import com.dbarad.core.csvparser.common.PARSE
import com.dbarad.core.csvparser.common.PASTE_CSV_DATA_HERE
import com.dbarad.core.csvparser.common.TOTAL_RECORDS_PROCESSED
import com.dbarad.core.csvparser.extensions.toJson
import com.dbarad.presentation.csvparser.viewmodels.ParserViewModel
import com.dbarad.presentation.csvparser.viewstates.ParseViewState


@Composable
fun ParserScreen(viewModel: ParserViewModel = hiltViewModel()) {
    var input by remember { mutableStateOf("") }
    var output by remember { mutableStateOf("") }
    val parserViewState by viewModel.parserViewState.collectAsState()

    LazyColumn {
        item {
            OutlinedTextField(
                value = input,
                onValueChange = { input = it },
                label = { Text(PASTE_CSV_DATA_HERE) },
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(16.dp)
            )
            Spacer(Modifier.height(8.dp))
            Button(modifier = Modifier.padding(16.dp), onClick = {
                try {
                    viewModel.parseInput(input)
                } catch (e: Exception) {
                    output = "$ERROR ${e.message}"
                }
            }) {
                Text(PARSE)
            }
            Spacer(Modifier.height(8.dp))
            Text(modifier = Modifier.padding(16.dp), text = JSON_OUTPUT)
        }
        item {
            when (parserViewState) {
                ParseViewState.Loading -> {
                    CircularProgressIndicator(
                        color = Color.Blue,
                        modifier = Modifier.padding(16.dp)
                    )
                }

                is ParseViewState.Success -> {
                    Text(
                        modifier = Modifier.padding(16.dp),
                        text = """$TOTAL_RECORDS_PROCESSED ${(parserViewState as ParseViewState.Success).parsedData.deviceDetails.deviceLines.size}"""
                    )
                    Text(
                        modifier = Modifier.padding(16.dp),
                        text = (parserViewState as ParseViewState.Success).parsedData.toJson()
                    )
                }

                else -> Unit
            }
        }
    }
}
