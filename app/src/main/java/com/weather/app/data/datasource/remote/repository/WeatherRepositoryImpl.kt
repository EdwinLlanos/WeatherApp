package com.weather.app.data.datasource.remote.repository

import com.weather.app.data.datasource.remote.WeatherNetworkClient
import com.weather.app.data.datasource.remote.model.toDomain
import com.weather.app.domain.model.WeatherDetailModel
import com.weather.app.domain.model.WeatherModel
import com.weather.app.domain.repository.WeatherRepository
import com.weather.app.framework.network.Failure
import com.weather.app.framework.network.NetworkHandler

class WeatherRepositoryImpl(
    private val networkHandler: NetworkHandler,
    private val weatherNetworkClient: WeatherNetworkClient
) : WeatherRepository {
    override suspend fun search(query: String): List<WeatherModel> =
        when (networkHandler.isConnected) {
            true -> weatherNetworkClient.search(query).toDomain()
            else -> throw Failure.NetworkConnection
        }

    override suspend fun getWeatherDetail(query: String, days: Int): WeatherDetailModel =
        when (networkHandler.isConnected) {
            true -> weatherNetworkClient.getWeatherDetail(query, days).toDomain()
            else -> throw Failure.NetworkConnection
        }
}
