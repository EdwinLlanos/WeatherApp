package com.weather.app.data.datasource.remote.model

import com.weather.app.domain.model.WeatherModel
import kotlinx.serialization.SerialName
import kotlinx.serialization.Serializable

@Serializable
data class WeatherResponse(
    @SerialName("id")
    val id: Int,
    @SerialName("name")
    val name: String,
    @SerialName("country")
    val country: String,
)

fun List<WeatherResponse>.toDomain() = map {
    WeatherModel(id = it.id, name = it.name, country = it.country)
}
