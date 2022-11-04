package com.weather.app.data.datasource.remote

import com.weather.app.BuildConfig.BASE_URL
import com.weather.app.BuildConfig.END_POINT_FORECAST
import com.weather.app.BuildConfig.END_POINT_SEARCH
import com.weather.app.BuildConfig.WEATHER_API_KEY
import com.weather.app.data.datasource.remote.model.WeatherDetailResponse
import com.weather.app.data.datasource.remote.model.WeatherResponse
import io.ktor.client.HttpClient
import io.ktor.client.request.get
import io.ktor.client.request.parameter

class WeatherNetworkClient(private val client: HttpClient) {
    private companion object {
        const val KEY = "key"
        const val QUERY = "q"
        const val DAYS = "days"
    }

    suspend fun search(query: String): List<WeatherResponse> =
        client.get("${BASE_URL}$END_POINT_SEARCH") {
            parameter(KEY, WEATHER_API_KEY)
            parameter(QUERY, query)
        }

    suspend fun getWeatherDetail(
        query: String,
        days: Int
    ): WeatherDetailResponse =
        client.get("${BASE_URL}$END_POINT_FORECAST") {
            parameter(KEY, WEATHER_API_KEY)
            parameter(QUERY, query)
            parameter(DAYS, days)
        }
}
