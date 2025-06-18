package com.dbarad.domain.csvparser.usecases

import com.dbarad.core.csvparser.common.Result
import com.dbarad.data.csvparser.repository.CsvDeviceParserRepository
import com.dbarad.domain.csvparser.models.DeviceDetails
import com.dbarad.domain.csvparser.models.DeviceLine
import com.dbarad.domain.csvparser.models.ParsedData
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.delay
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.catch
import kotlinx.coroutines.flow.flowOn
import kotlinx.coroutines.flow.map
import kotlinx.coroutines.flow.onStart
import javax.inject.Inject
import com.dbarad.data.csvparser.models.DeviceDetails as DataDeviceDetails

class ParseDeviceReportUseCase @Inject constructor(private val csvDeviceParserRepository: CsvDeviceParserRepository) {
    operator fun invoke(csv: String): Flow<Result<ParsedData>> =
        csvDeviceParserRepository.parse(csv)
            .onStart {
                emit(Result.Loading)
            }.flowOn(Dispatchers.IO)
            .map { result ->
                when (result) {
                    is Result.Success -> {
                        delay(500) //intentionally added delay to show loading state
                        Result.Success(toDomainModel(result.data))
                    }

                    is Result.Error -> {
                        Result.Error(result.exception)
                    }

                    else -> Result.Loading
                }
            }.catch {
                emit(Result.Error(it))
            }

    /**
     * Transform data model to domain model
     */
    private fun toDomainModel(dataDeviceDetails: DataDeviceDetails): ParsedData {
        val deviceDetails = DeviceDetails(
            dataDeviceDetails.serverID,
            dataDeviceDetails.deviceLines.map {
                DeviceLine(
                    it.imei1,
                    it.imei2,
                    it.serialNumber,
                    it.deviceName
                )
            })
        return ParsedData(deviceDetails)
    }
}