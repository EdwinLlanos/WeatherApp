package com.weather.app.domain.usecase

import com.weather.app.domain.model.WeatherDetailModel
import com.weather.app.domain.repository.WeatherRepository
import com.weather.app.framework.usecase.UseCase
import kotlinx.coroutines.CoroutineDispatcher

class GetWeatherDetailUseCase(
    ioDispatcher: CoroutineDispatcher,
    private val weatherRepository: WeatherRepository
) : UseCase<GetWeatherDetailUseCase.Parameters, WeatherDetailModel>(ioDispatcher) {
    override suspend fun execute(parameters: Parameters): WeatherDetailModel =
        weatherRepository.getWeatherDetail(parameters.query, parameters.days)

    data class Parameters(val query: String, val days: Int)
}
