package com.weather.app.data.datasource.remote.model

import com.weather.app.domain.model.SearchModel
import kotlinx.serialization.SerialName
import kotlinx.serialization.Serializable

@Serializable
data class SearchResponse(
    @SerialName("id")
    val id: Int,
    @SerialName("name")
    val name: String,
    @SerialName("country")
    val country: String,
)

fun List<SearchResponse>.toDomain() = map {
    SearchModel(id = it.id, name = it.name, country = it.country)
}
