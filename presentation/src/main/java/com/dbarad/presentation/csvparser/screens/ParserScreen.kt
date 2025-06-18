package com.dbarad.presentation.csvparser.screens

import android.content.ClipData
import android.content.ClipboardManager
import android.content.Context
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.CopyAll
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.unit.dp
import androidx.hilt.navigation.compose.hiltViewModel
import com.dbarad.core.csvparser.common.CLEAR
import com.dbarad.core.csvparser.common.EMPTY_STRING
import com.dbarad.core.csvparser.common.ERROR
import com.dbarad.core.csvparser.common.JSON_OUTPUT
import com.dbarad.core.csvparser.common.PARSE
import com.dbarad.core.csvparser.common.PASTE_CSV_DATA_HERE
import com.dbarad.core.csvparser.common.TOTAL_RECORDS_PROCESSED
import com.dbarad.core.csvparser.designsystem.VFCSVButton
import com.dbarad.core.csvparser.designsystem.VFCSVCircularProgressIndicator
import com.dbarad.core.csvparser.designsystem.VFCSVDisplayCard
import com.dbarad.core.csvparser.designsystem.VFCSVIconButton
import com.dbarad.core.csvparser.designsystem.VFCSVText
import com.dbarad.core.csvparser.designsystem.VFCSVTextBold
import com.dbarad.core.csvparser.extensions.toJson
import com.dbarad.presentation.csvparser.viewmodels.ParserViewModel
import com.dbarad.presentation.csvparser.viewstates.ParseViewState


@Composable
fun ParserScreen(viewModel: ParserViewModel = hiltViewModel()) {
    var input by remember { mutableStateOf("") }
    var output by remember { mutableStateOf("") }
    val parserViewState by viewModel.parserViewState.collectAsState()
    val clipboardData by viewModel.clipboardData.collectAsState()
    val canCopyToClipboard by viewModel.canCopyToClipboard.collectAsState()

    when (canCopyToClipboard) {
        true -> {
            CopyOutput(clipboardData)
        }

        false -> Unit
    }

    Box(Modifier.fillMaxSize()) {
        LazyColumn {
            item {
                OutlinedTextField(
                    value = input,
                    onValueChange = { input = it },
                    label = { Text(PASTE_CSV_DATA_HERE) },
                    modifier = Modifier
                        .fillMaxWidth()
                        .height(200.dp)
                        .padding(16.dp)
                )
                Row {
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

                    VFCSVButton(
                        modifier = Modifier.padding(16.dp),
                        onClick = {
                            input = EMPTY_STRING
                        },
                        text = CLEAR
                    )
                }
            }
            item {
                when (parserViewState) {
                    is ParseViewState.Success -> {
                        VFCSVDisplayCard {
                            Row {
                                VFCSVTextBold(
                                    modifier = Modifier
                                        .padding(16.dp)
                                        .fillMaxWidth()
                                        .weight(0.80f),
                                    text = """$TOTAL_RECORDS_PROCESSED ${(parserViewState as ParseViewState.Success).parsedData.deviceDetails.deviceLines.size}""",
                                    isCenterAligned = true
                                )
                                VFCSVIconButton(
                                    modifier = Modifier.weight(0.20f),
                                    onClick = {
                                        viewModel.copyOutput()
                                    },
                                    imageVector = Icons.Default.CopyAll
                                )
                            }
                            VFCSVText(
                                modifier = Modifier.padding(16.dp),
                                text = (parserViewState as ParseViewState.Success).parsedData.toJson()
                            )
                        }
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

@Composable
private fun CopyOutput(value: String) {
    val clipboard =
        LocalContext.current.getSystemService(Context.CLIPBOARD_SERVICE) as ClipboardManager
    val clipData = ClipData.newPlainText(JSON_OUTPUT, value)
    clipboard.setPrimaryClip(clipData)
}
