package com.weather.app.data.datasource.remote.repository

import com.weather.app.data.datasource.remote.SearchNetworkClient
import com.weather.app.data.datasource.remote.model.toDomain
import com.weather.app.domain.model.SearchModel
import com.weather.app.domain.repository.SearchRepository
import com.weather.app.framework.network.Failure
import com.weather.app.framework.network.NetworkHandler

class SearchRepositoryImpl(
    private val networkHandler: NetworkHandler,
    private val searchNetworkClient: SearchNetworkClient
) : SearchRepository {
    override suspend fun streaksByUser(query: String): List<SearchModel> =
        when (networkHandler.isConnected) {
            true -> searchNetworkClient.search(query).toDomain()
            else -> throw Failure.NetworkConnection
        }
}
