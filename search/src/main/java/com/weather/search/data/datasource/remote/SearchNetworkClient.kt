package com.weather.search.data.datasource.remote

import com.weather.search.BuildConfig.BASE_URL
import com.weather.search.BuildConfig.END_POINT_SEARCH
import com.weather.search.BuildConfig.WEATHER_API_KEY
import com.weather.search.data.datasource.remote.model.SearchResponse
import io.ktor.client.HttpClient
import io.ktor.client.request.get
import io.ktor.client.request.parameter

class SearchNetworkClient(private val client: HttpClient) {
    private companion object {
        const val KEY = "key"
        const val QUERY = "q"
    }

    suspend fun search(query: String): List<SearchResponse> =
        client.get("${BASE_URL}$END_POINT_SEARCH") {
            parameter(KEY, WEATHER_API_KEY)
            parameter(QUERY, query)
        }
}
