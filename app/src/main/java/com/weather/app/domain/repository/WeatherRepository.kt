package com.weather.app.domain.repository

import com.weather.app.domain.model.WeatherDetailModel
import com.weather.app.domain.model.WeatherModel

interface WeatherRepository {
    suspend fun streaksByUser(
        query: String,
    ): List<WeatherModel>

    suspend fun getWeatherDetail(
        query: String,
        days: Int,
    ): WeatherDetailModel
}
