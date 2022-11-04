package com.weather.app.domain.usecase

import com.weather.app.domain.model.SearchModel
import com.weather.app.domain.repository.WeatherRepository
import com.weather.app.framework.usecase.UseCase
import kotlinx.coroutines.CoroutineDispatcher

class SearchUseCase(
    ioDispatcher: CoroutineDispatcher,
    private val weatherRepository: WeatherRepository
) : UseCase<SearchUseCase.Parameters, List<SearchModel>>(ioDispatcher) {
    override suspend fun execute(parameters: Parameters): List<SearchModel> =
        weatherRepository.streaksByUser(parameters.query)

    data class Parameters(val query: String)
}
