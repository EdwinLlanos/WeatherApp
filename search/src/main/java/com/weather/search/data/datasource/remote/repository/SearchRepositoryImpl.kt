package com.weather.search.data.datasource.remote.repository

import com.weather.library.network.Failure
import com.weather.library.network.NetworkHandler
import com.weather.search.data.datasource.remote.SearchNetworkClient
import com.weather.search.data.datasource.remote.model.toDomain
import com.weather.search.domain.model.SearchModel
import com.weather.search.domain.repository.SearchRepository

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
