package com.dbarad.data.csvparser.repository

import com.dbarad.data.csvparser.models.DeviceDetails
import com.dbarad.data.csvparser.models.DeviceLine
import javax.inject.Inject

class CsvDeviceParserRepositoryImpl @Inject constructor() : CsvDeviceParserRepository {
    override fun parse(csv: String): DeviceDetails {
        val lines = csv.trim().lines()
        val header = lines.first { it.startsWith("H|") }.split("|")[1]
        val serverID = header.take(7)
        val records = lines.filter { it.startsWith("R|") }.map {
            val parts = it.split("|")
            DeviceLine(parts[1], parts[2], parts[3], parts[4])
        }
        val trailerCount = lines.first { it.startsWith("T|") }.split("|")[1].toInt()
        require(trailerCount == records.size)
        return DeviceDetails(serverID, records)
    }
}