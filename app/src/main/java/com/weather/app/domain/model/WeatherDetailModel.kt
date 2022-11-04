package com.weather.app.domain.model

import androidx.compose.runtime.Immutable

@Immutable
data class WeatherDetailModel(
    val locationModel: LocationModel,
    val currentModel: CurrentModel,
    val forecastModel: ForecastModel,
)

@Immutable
data class CurrentModel(
    val conditionModel: ConditionModel,
    val tempC: Double
)

@Immutable
data class ForecastModel(
    val forecastDayModel: List<ForecastDayModel>
)

@Immutable
data class LocationModel(
    val localtime: String,
    val name: String
)

@Immutable
data class ConditionModel(
    val icon: String,
    val text: String
)

@Immutable
data class ForecastDayModel(
    val date: String,
    val dayModel: DayModel
)

@Immutable
data class DayModel(
    val avgTempC: Double,
    val conditionModel: ConditionModel
)
