package com.weather.app.presentation.search.viewmodel

import com.weather.app.domain.model.WeatherModel
import com.weather.app.domain.usecase.SearchUseCase
import com.weather.app.framework.MainDispatcherRule
import com.weather.app.framework.scenario
import com.weather.app.framework.state.ScreenState
import com.weather.app.framework.usecase.result.Result
import io.mockk.coEvery
import io.mockk.coVerify
import io.mockk.confirmVerified
import io.mockk.every
import io.mockk.mockk
import io.mockk.slot
import org.junit.Assert
import org.junit.Before
import org.junit.Rule
import org.junit.Test

class SearchViewModelTest {
    @get:Rule
    val mainDispatcherRule = MainDispatcherRule()
    private val searchUseCase: SearchUseCase = mockk(relaxed = true)
    private lateinit var searchViewModel: SearchViewModel

    private companion object {
        const val QUERY = "bogota"
    }

    @Before
    fun setup() {
        searchViewModel = SearchViewModel(searchUseCase)
    }

    @Test
    fun shouldInitializedWithStatusEmpty() {
        scenario("should initialized with status Empty") {
            then {
                Assert.assertEquals(ScreenState.Empty, searchViewModel.uiState.screenState)
                Assert.assertNull(searchViewModel.uiState.errorMessage)
                Assert.assertEquals(
                    emptyList<WeatherModel>(),
                    searchViewModel.uiState.listWeatherModel
                )
            }
        }
    }


    @Test
    fun shouldSearchByQueryCorrectly() {
        scenario("should search by query correctly") {

            val slotSearchUseCaseParams = slot<SearchUseCase.Parameters>()
            val weatherModelList: List<WeatherModel> = mockk(relaxed = true) {
                every { first() } returns WeatherModel(502209, "Bogota", "Colombia")
            }
            val resultSearchUseCase: Result<List<WeatherModel>> =
                Result.Success(weatherModelList)

            given {
                coEvery {
                    searchUseCase(capture(slotSearchUseCaseParams))
                } returns resultSearchUseCase
            }

            execute {
                searchViewModel.search(QUERY)
            }

            then {
                Assert.assertEquals(ScreenState.Success, searchViewModel.uiState.screenState)
                Assert.assertEquals(weatherModelList, searchViewModel.uiState.listWeatherModel)
            }

            then {
                coVerify {
                    searchUseCase(slotSearchUseCaseParams.captured)
                }
                confirmVerified()
            }
        }
    }

    @Test
    fun shouldSearchByQueryUnsuccessfully() {
        scenario("should search by query unsuccessfully") {

            val slotSearchUseCaseParams = slot<SearchUseCase.Parameters>()
            val resultSearchUseCase: Result<List<WeatherModel>> =
                Result.Error(Exception("Error unit text"))

            given {
                coEvery {
                    searchUseCase(capture(slotSearchUseCaseParams))
                } returns resultSearchUseCase
            }

            execute {
                searchViewModel.search(QUERY)
            }

            then {
                Assert.assertEquals(ScreenState.Error, searchViewModel.uiState.screenState)
                Assert.assertNotNull(searchViewModel.uiState.errorMessage)
                Assert.assertEquals(
                    emptyList<List<WeatherModel>>(),
                    searchViewModel.uiState.listWeatherModel
                )
            }

            then {
                coVerify {
                    searchUseCase(slotSearchUseCaseParams.captured)
                }
                confirmVerified()
            }
        }
    }
}
