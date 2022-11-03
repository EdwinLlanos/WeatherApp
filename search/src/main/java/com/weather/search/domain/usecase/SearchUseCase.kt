package com.weather.search.domain.usecase

import com.weather.library.usecase.UseCase
import com.weather.search.domain.model.SearchModel
import com.weather.search.domain.repository.SearchRepository
import kotlinx.coroutines.CoroutineDispatcher

class SearchUseCase(
    ioDispatcher: CoroutineDispatcher,
    private val searchRepository: SearchRepository
) : UseCase<SearchUseCase.Parameters, List<SearchModel>>(ioDispatcher) {
    override suspend fun execute(parameters: Parameters): List<SearchModel> =
        searchRepository.streaksByUser(parameters.query)

    data class Parameters(val query: String)
}
