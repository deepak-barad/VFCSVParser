package com.dbarad.presentation.csvparser.viewstates

import com.dbarad.domain.csvparser.models.ParsedData

sealed interface ParseViewState {
    data object Loading : ParseViewState
    data object DoNothing : ParseViewState
    data class Success(val parsedData: ParsedData) : ParseViewState
}