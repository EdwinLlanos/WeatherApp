package com.weather.app.data.datasource.remote.model

import com.weather.app.domain.model.ConditionModel
import com.weather.app.domain.model.CurrentModel
import com.weather.app.domain.model.DayModel
import com.weather.app.domain.model.ForecastDayModel
import com.weather.app.domain.model.ForecastModel
import com.weather.app.domain.model.LocationModel
import com.weather.app.domain.model.WeatherDetailModel
import kotlinx.serialization.SerialName
import kotlinx.serialization.Serializable

@Serializable
data class WeatherDetailResponse(
    @SerialName("current")
    val current: Current,
    @SerialName("forecast")
    val forecast: Forecast,
    @SerialName("location")
    val location: Location
)

@Serializable
data class Current(
    @SerialName("condition")
    val condition: Condition,
    @SerialName("temp_c")
    val tempC: Double
)

@Serializable
data class Forecast(
    @SerialName("forecastday")
    val forecastDay: List<ForecastDay>
)

@Serializable
data class Location(
    @SerialName("localtime")
    val localtime: String,
    @SerialName("name")
    val name: String
)

@Serializable
data class Condition(
    @SerialName("icon")
    val icon: String,
    @SerialName("text")
    val text: String
)

@Serializable
data class ForecastDay(
    @SerialName("date")
    val date: String,
    @SerialName("day")
    val day: Day
)

@Serializable
data class Day(
    @SerialName("avgtemp_c")
    val avgTempC: Double,
    @SerialName("condition")
    val condition: Condition
)

fun WeatherDetailResponse.toDomain() = WeatherDetailModel(
    currentModel = CurrentModel(
        tempC = current.tempC,
        conditionModel = ConditionModel(
            icon = current.condition.icon,
            text = current.condition.text
        )
    ),
    forecastModel = ForecastModel(
        forecastDayModel = forecast.forecastDay.map { forecastDay ->
            ForecastDayModel(
                date = forecastDay.date,
                dayModel = DayModel(
                    avgTempC = forecastDay.day.avgTempC, conditionModel = ConditionModel(
                        icon = forecastDay.day.condition.icon,
                        text = forecastDay.day.condition.text,
                    )
                )
            )
        },
    ), locationModel = LocationModel(localtime = location.localtime, name = location.name)
)
