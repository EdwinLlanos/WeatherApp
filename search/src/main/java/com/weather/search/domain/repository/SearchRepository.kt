package com.weather.search.domain.repository

import com.weather.search.domain.model.SearchModel

interface SearchRepository {
    suspend fun streaksByUser(
        query: String,
    ): List<SearchModel>
}
