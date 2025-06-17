package com.dbarad.data.csvparser.repository

import com.dbarad.core.csvparser.common.Result
import com.dbarad.data.csvparser.models.DeviceDetails
import com.dbarad.data.csvparser.models.DeviceLine
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.flow
import javax.inject.Inject

class CsvDeviceParserRepositoryImpl @Inject constructor() : CsvDeviceParserRepository {
    override fun parse(csv: String): Flow<Result<DeviceDetails>> = flow {
        try {
            val lines = csv.trim().lines()
            val header = lines.first { it.startsWith("H|") }.split("|")[1]
            val serverID = header.take(7)
            val records = lines.filter { it.startsWith("R|") }.map {
                val parts = it.split("|")
                DeviceLine(parts[1], parts[2], parts[3], parts[4])
            }
            val trailerCount = lines.first { it.startsWith("T|") }.split("|")[1].toInt()
            require(trailerCount == records.size)
            emit(Result.Success(DeviceDetails(serverID, records)))
        } catch (exception: Exception) {
            emit(Result.Error(exception))
        }
    }
}