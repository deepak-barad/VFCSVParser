package com.dbarad.data.csvparser.repository

import com.dbarad.data.csvparser.models.DeviceDetails

interface CsvDeviceParserRepository {
    fun parse(csv: String): DeviceDetails
}