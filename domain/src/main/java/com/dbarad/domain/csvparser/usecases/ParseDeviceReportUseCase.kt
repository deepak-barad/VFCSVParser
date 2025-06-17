package com.dbarad.domain.csvparser.usecases

import com.dbarad.core.csvparser.common.Result
import com.dbarad.data.csvparser.repository.CsvDeviceParserRepository
import com.dbarad.domain.csvparser.models.DeviceDetails
import com.dbarad.domain.csvparser.models.DeviceLine
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.flow
import javax.inject.Inject
import com.dbarad.data.csvparser.models.DeviceDetails as DataDeviceDetails

class ParseDeviceReportUseCase @Inject constructor(private val parser: CsvDeviceParserRepository) {
    operator fun invoke(csv: String): Flow<Result<DeviceDetails>> = flow {
        emit(Result.Success(toDomainModel(parser.parse(csv))))
    }

    private fun toDomainModel(dataDeviceDetails: DataDeviceDetails): DeviceDetails {
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
        return deviceDetails
    }
}