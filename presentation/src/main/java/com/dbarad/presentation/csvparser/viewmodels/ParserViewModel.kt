package com.dbarad.presentation.csvparser.viewmodels

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.dbarad.core.csvparser.common.EMPTY_STRING
import com.dbarad.core.csvparser.common.Result
import com.dbarad.core.csvparser.common.SOMETHING_WENT_WRONG_OR_WRONG_INPUT
import com.dbarad.core.csvparser.extensions.toJson
import com.dbarad.domain.csvparser.models.ParsedData
import com.dbarad.domain.csvparser.usecases.ParseDeviceReportUseCase
import com.dbarad.presentation.csvparser.viewstates.ParseViewState
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.delay
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class ParserViewModel @Inject constructor(private val parseDeviceReportUseCase: ParseDeviceReportUseCase) :
    ViewModel() {
    private val _parserViewState = MutableStateFlow<ParseViewState>(ParseViewState.DoNothing)
    val parserViewState = _parserViewState.asStateFlow()

    private val _clipboardData = MutableStateFlow<String>(EMPTY_STRING)
    val clipboardData = _clipboardData.asStateFlow()

    private val _canCopyToClipboard = MutableStateFlow<Boolean>(false)
    val canCopyToClipboard = _canCopyToClipboard.asStateFlow()

    fun parseInput(csv: String) {
        viewModelScope.launch {
            parseDeviceReportUseCase(csv).collect { result ->
                when (result) {
                    is Result.Error -> {
                        _parserViewState.update {
                            ParseViewState.Error(SOMETHING_WENT_WRONG_OR_WRONG_INPUT)
                        }
                    }

                    is Result.Loading -> {
                        _parserViewState.update {
                            ParseViewState.Loading
                        }
                    }

                    is Result.Success<*> -> {
                        _parserViewState.update {
                            ParseViewState.Success(result.data as ParsedData)
                        }
                    }
                }
            }
        }
    }

    private fun reset() {
        _canCopyToClipboard.update {
            false
        }
        _clipboardData.update { _ ->
            EMPTY_STRING
        }
    }

    fun copyOutput() {
        viewModelScope.launch {
            _parserViewState.value.let {
                if (it is ParseViewState.Success) {
                    _clipboardData.update { _ ->
                        it.parsedData.deviceDetails.toJson()
                    }
                    _canCopyToClipboard.update {
                        true
                    }
                }
            }
            delay(2000)
            reset()
        }
    }
}