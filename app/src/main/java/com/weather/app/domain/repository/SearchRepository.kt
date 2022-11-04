package com.weather.app.domain.repository

import com.weather.app.domain.model.SearchModel

interface SearchRepository {
    suspend fun streaksByUser(
        query: String,
    ): List<SearchModel>
}
