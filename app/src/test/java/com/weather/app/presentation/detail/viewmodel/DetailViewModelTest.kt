package com.weather.app.presentation.detail.viewmodel

import com.weather.app.domain.model.LocationModel
import com.weather.app.domain.model.WeatherDetailModel
import com.weather.app.domain.usecase.GetWeatherDetailUseCase
import com.weather.app.framework.MainDispatcherRule
import com.weather.app.framework.scenario
import com.weather.app.framework.state.ScreenState
import com.weather.app.framework.usecase.result.Result
import com.weather.app.framework.usecase.result.Result.Success
import io.mockk.coEvery
import io.mockk.coVerify
import io.mockk.confirmVerified
import io.mockk.every
import io.mockk.mockk
import io.mockk.slot
import junit.framework.Assert.assertEquals
import junit.framework.Assert.assertNotNull
import junit.framework.Assert.assertNull
import org.junit.Before
import org.junit.Rule
import org.junit.Test

class DetailViewModelTest {
    @get:Rule
    val mainDispatcherRule = MainDispatcherRule()
    private val getWeatherDetailUseCase: GetWeatherDetailUseCase = mockk(relaxed = true)
    private lateinit var detailViewModel: DetailViewModel

    private companion object {
        const val QUERY = "bogota"
    }

    @Before
    fun setup() {
        detailViewModel = DetailViewModel(getWeatherDetailUseCase)
    }

    @Test
    fun shouldInitializedWithStatusEmpty() {
        scenario("should initialized with status Empty") {
            then {
                assertEquals(ScreenState.Empty, detailViewModel.uiState.screenState)
                assertNull(detailViewModel.uiState.errorMessage)
                assertNull(detailViewModel.uiState.weatherDetailModel)
            }
        }
    }


    @Test
    fun shouldGetDetailByQueryCorrectly() {
        scenario("should get detail by query correctly") {

            val slotGetWeatherDetailUseCaseParams = slot<GetWeatherDetailUseCase.Parameters>()
            val weatherDetailModel: WeatherDetailModel = mockk(relaxed = true) {
                every { locationModel } returns LocationModel("2022-11-06 21:41", "Bogota")
            }
            val resultGetWeatherDetailUseCase: Result<WeatherDetailModel> =
                Success(weatherDetailModel)

            given {
                coEvery {
                    getWeatherDetailUseCase(capture(slotGetWeatherDetailUseCaseParams))
                } returns resultGetWeatherDetailUseCase
            }

            execute {
                detailViewModel.getWeatherDetail(QUERY)
            }

            then {
                assertEquals(ScreenState.Success, detailViewModel.uiState.screenState)
                assertEquals(weatherDetailModel, detailViewModel.uiState.weatherDetailModel)
            }

            then {
                coVerify {
                    getWeatherDetailUseCase(slotGetWeatherDetailUseCaseParams.captured)
                }
                confirmVerified()
            }
        }
    }

    @Test
    fun shouldGetDetailByQueryUnsuccessfully() {
        scenario("should get detail by query unsuccessfully") {

            val slotGetWeatherDetailUseCaseParams = slot<GetWeatherDetailUseCase.Parameters>()
            val resultGetWeatherDetailUseCase: Result<WeatherDetailModel> =
                Result.Error(Exception("Error unit text"))

            given {
                coEvery {
                    getWeatherDetailUseCase(capture(slotGetWeatherDetailUseCaseParams))
                } returns resultGetWeatherDetailUseCase
            }

            execute {
                detailViewModel.getWeatherDetail(QUERY)
            }

            then {
                assertEquals(ScreenState.Error, detailViewModel.uiState.screenState)
                assertNotNull(detailViewModel.uiState.errorMessage)
                assertNull(detailViewModel.uiState.weatherDetailModel)
            }

            then {
                coVerify {
                    getWeatherDetailUseCase(slotGetWeatherDetailUseCaseParams.captured)
                }
                confirmVerified()
            }
        }
    }
}
