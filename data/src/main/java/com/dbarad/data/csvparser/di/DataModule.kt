package com.dbarad.data.csvparser.di

import com.dbarad.data.csvparser.repository.CsvDeviceParserRepository
import com.dbarad.data.csvparser.repository.CsvDeviceParserRepositoryImpl
import dagger.Binds
import dagger.Module
import dagger.hilt.InstallIn
import dagger.hilt.components.SingletonComponent

@Module
@InstallIn(SingletonComponent::class)
abstract class DataModule {

    @Binds
    abstract fun bindCsvDeviceParserRepository(impl: CsvDeviceParserRepositoryImpl): CsvDeviceParserRepository
}