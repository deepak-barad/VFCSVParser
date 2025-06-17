package com.dbarad.data.csvparser.repository

import com.dbarad.core.csvparser.common.Result
import com.dbarad.data.csvparser.models.DeviceDetails
import kotlinx.coroutines.flow.Flow

interface CsvDeviceParserRepository {
    fun parse(csv: String): Flow<Result<DeviceDetails>>
}