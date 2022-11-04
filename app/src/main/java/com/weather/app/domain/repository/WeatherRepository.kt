package com.weather.app.domain.repository

import com.weather.app.domain.model.SearchModel

interface WeatherRepository {
    suspend fun streaksByUser(
        query: String,
    ): List<SearchModel>
}
