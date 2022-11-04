package com.weather.app.presentation.search.viewmodel

import android.util.Log
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.setValue
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.weather.app.domain.model.SearchModel
import com.weather.app.domain.usecase.SearchUseCase
import com.weather.app.domain.usecase.SearchUseCase.Parameters
import com.weather.app.framework.network.Failure
import com.weather.app.framework.state.ScreenState
import com.weather.app.framework.usecase.result.Result
import kotlinx.coroutines.launch

data class SearchUiState(
    val screenState: ScreenState = ScreenState.Empty,
    val errorMessage: String = String(),
    val listSearchModel: List<SearchModel> = emptyList()
)

class SearchViewModel(private val searchUseCase: SearchUseCase) : ViewModel() {

    private companion object {
        const val TAG = "SearchViewModel"
        const val MINIMAL_LENGTH_WORDS = 3
    }

    var uiState by mutableStateOf(SearchUiState(screenState = ScreenState.Empty))
        private set

    fun search(query: String) {
        uiState = SearchUiState(screenState = ScreenState.Empty)
        if (query.length >= MINIMAL_LENGTH_WORDS) {
            uiState = uiState.copy(screenState = ScreenState.Loading)
            viewModelScope.launch {
                when (val result = searchUseCase(Parameters(query))) {
                    is Result.Success -> handleSearchSuccess(result.data)
                    is Result.Error -> handleSearchError(result.exception)
                    else -> handleSearchError(Failure.ExceptionUnknown)
                }
            }
        }
    }

    private fun handleSearchSuccess(listSearchModel: List<SearchModel>) {
        Log.d(TAG, listSearchModel.toString())
        uiState = uiState.copy(
            screenState = ScreenState.Success, listSearchModel = listSearchModel
        )
    }

    private fun handleSearchError(exception: Exception) {
        Log.d(TAG, exception.toString())
        uiState = uiState.copy(
            screenState = ScreenState.Error,
            errorMessage = exception.message.toString()
        )
    }
}
