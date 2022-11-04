package com.weather.app.data.datasource.remote.repository

import com.weather.app.data.datasource.remote.WeatherNetworkClient
import com.weather.app.data.datasource.remote.model.toDomain
import com.weather.app.domain.model.SearchModel
import com.weather.app.domain.repository.WeatherRepository
import com.weather.app.framework.network.Failure
import com.weather.app.framework.network.NetworkHandler

class WeatherRepositoryImpl(
    private val networkHandler: NetworkHandler,
    private val weatherNetworkClient: WeatherNetworkClient
) : WeatherRepository {
    override suspend fun streaksByUser(query: String): List<SearchModel> =
        when (networkHandler.isConnected) {
            true -> weatherNetworkClient.search(query).toDomain()
            else -> throw Failure.NetworkConnection
        }
}
