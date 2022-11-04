package com.weather.app.domain.model

import androidx.compose.runtime.Immutable

@Immutable
data class SearchModel(
    val id: Int,
    val name: String,
    val country: String,
)
