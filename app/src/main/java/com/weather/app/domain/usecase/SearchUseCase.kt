package com.weather.app.domain.usecase

import com.weather.app.domain.model.SearchModel
import com.weather.app.domain.repository.SearchRepository
import com.weather.app.framework.usecase.UseCase
import kotlinx.coroutines.CoroutineDispatcher

class SearchUseCase(
    ioDispatcher: CoroutineDispatcher,
    private val searchRepository: SearchRepository
) : UseCase<SearchUseCase.Parameters, List<SearchModel>>(ioDispatcher) {
    override suspend fun execute(parameters: Parameters): List<SearchModel> =
        searchRepository.streaksByUser(parameters.query)

    data class Parameters(val query: String)
}
