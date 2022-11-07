package com.weather.app.presentation.detail.viewmodel

import android.util.Log
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.setValue
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.weather.app.R
import com.weather.app.domain.model.WeatherDetailModel
import com.weather.app.domain.usecase.GetWeatherDetailUseCase
import com.weather.app.framework.network.Failure
import com.weather.app.framework.state.ScreenState
import com.weather.app.framework.usecase.result.Result
import kotlinx.coroutines.launch

data class DetailUiState(
    val screenState: ScreenState = ScreenState.Empty,
    val errorMessage: Int? = null,
    val weatherDetailModel: WeatherDetailModel? = null
)

class DetailViewModel(
    private val getWeatherDetailUseCase: GetWeatherDetailUseCase
) : ViewModel() {

    private companion object {
        const val TAG = "DetailViewModel"
        const val DAYS_FOR_DEFAULT = 3
    }

    var uiState by mutableStateOf(DetailUiState(screenState = ScreenState.Empty))
        private set

    fun getWeatherDetail(query: String?) {
        query?.let { noNullQuery ->
            uiState = DetailUiState(screenState = ScreenState.Empty)
            uiState = uiState.copy(screenState = ScreenState.Loading)
            viewModelScope.launch {
                when (val result = getWeatherDetailUseCase(
                    GetWeatherDetailUseCase.Parameters(
                        noNullQuery,
                        DAYS_FOR_DEFAULT
                    )
                )) {
                    is Result.Success -> handleGetWeatherDetailSuccess(result.data)
                    is Result.Error -> handleGetWeatherDetailError(result.exception)
                    else -> handleGetWeatherDetailError(Failure.ExceptionUnknown)
                }
            }
        } ?: handleGetWeatherDetailError(Failure.ExceptionUnknown)
    }

    private fun handleGetWeatherDetailSuccess(weatherDetailModel: WeatherDetailModel) {
        Log.d(TAG, weatherDetailModel.toString())
        uiState = uiState.copy(
            screenState = ScreenState.Success, weatherDetailModel = weatherDetailModel
        )
    }

    private fun handleGetWeatherDetailError(exception: Exception) {
        Log.d(TAG, exception.toString())
        uiState = uiState.copy(
            screenState = ScreenState.Error,
            errorMessage = getErrorMessage(exception)
        )
    }

    private fun getErrorMessage(exception: Exception) = when (exception) {
        Failure.NetworkConnection -> R.string.error_network
        else -> R.string.error_unexpected
    }
}
