package com.dbarad.presentation.csvparser.viewmodels

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.dbarad.core.csvparser.common.Result
import com.dbarad.domain.csvparser.models.ParsedData
import com.dbarad.domain.csvparser.usecases.ParseDeviceReportUseCase
import com.dbarad.presentation.csvparser.viewstates.ParseViewState
import dagger.hilt.android.lifecycle.HiltViewModel
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

    fun parseInput(csv: String) {
        viewModelScope.launch {
            parseDeviceReportUseCase(csv).collect { result ->
                when (result) {
                    is Result.Error -> {

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
}