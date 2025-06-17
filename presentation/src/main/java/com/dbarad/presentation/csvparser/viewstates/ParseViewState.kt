package com.dbarad.presentation.csvparser.viewstates

import com.dbarad.domain.csvparser.models.DeviceDetails

sealed interface ParseViewState {
    data object Loading : ParseViewState
    data object DoNothing : ParseViewState
    data class Success(val deviceDetails: DeviceDetails) : ParseViewState
}