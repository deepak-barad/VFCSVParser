package com.dbarad.presentation.csvparser.screens

import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import androidx.hilt.navigation.compose.hiltViewModel
import com.dbarad.core.csvparser.common.ERROR
import com.dbarad.core.csvparser.common.JSON_OUTPUT
import com.dbarad.core.csvparser.common.PARSE
import com.dbarad.core.csvparser.common.PASTE_CSV_DATA_HERE
import com.dbarad.core.csvparser.common.TOTAL_RECORDS_PROCESSED
import com.dbarad.core.csvparser.designsystem.VFCSVButton
import com.dbarad.core.csvparser.designsystem.VFCSVCircularProgressIndicator
import com.dbarad.core.csvparser.designsystem.VFCSVText
import com.dbarad.core.csvparser.extensions.toJson
import com.dbarad.presentation.csvparser.viewmodels.ParserViewModel
import com.dbarad.presentation.csvparser.viewstates.ParseViewState


@Composable
fun ParserScreen(viewModel: ParserViewModel = hiltViewModel()) {
    var input by remember { mutableStateOf("") }
    var output by remember { mutableStateOf("") }
    val parserViewState by viewModel.parserViewState.collectAsState()
    Box(Modifier.fillMaxSize()) {
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
                VFCSVButton(
                    modifier = Modifier.padding(16.dp),
                    onClick = {
                        try {
                            viewModel.parseInput(input)
                        } catch (e: Exception) {
                            output = "$ERROR ${e.message}"
                        }
                    },
                    text = PARSE
                )
                Spacer(Modifier.height(8.dp))
                VFCSVText(modifier = Modifier.padding(16.dp), text = JSON_OUTPUT)
            }
            item {
                when (parserViewState) {
                    is ParseViewState.Success -> {
                        VFCSVText(
                            modifier = Modifier.padding(16.dp),
                            text = """$TOTAL_RECORDS_PROCESSED ${(parserViewState as ParseViewState.Success).parsedData.deviceDetails.deviceLines.size}"""
                        )
                        VFCSVText(
                            modifier = Modifier.padding(16.dp),
                            text = (parserViewState as ParseViewState.Success).parsedData.toJson()
                        )
                    }

                    is ParseViewState.Error -> {
                        VFCSVText(
                            modifier = Modifier.padding(16.dp),
                            text = (parserViewState as ParseViewState.Error).message
                        )
                    }

                    else -> Unit
                }
            }
        }
        when (parserViewState) {
            ParseViewState.Loading -> {
                VFCSVCircularProgressIndicator(modifier = Modifier)
            }

            else -> Unit
        }
    }
}