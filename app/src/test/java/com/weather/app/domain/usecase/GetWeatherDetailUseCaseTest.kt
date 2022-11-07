package com.weather.app.domain.usecase

import com.weather.app.domain.model.WeatherDetailModel
import com.weather.app.domain.repository.WeatherRepository
import com.weather.app.framework.scenario
import com.weather.app.framework.usecase.result.Result
import com.weather.app.framework.usecase.result.data
import io.mockk.coEvery
import io.mockk.coVerify
import io.mockk.confirmVerified
import io.mockk.mockk
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.runBlocking
import kotlinx.coroutines.test.UnconfinedTestDispatcher
import org.junit.Assert
import org.junit.Test

class GetWeatherDetailUseCaseTest {
    private companion object {
        const val QUERY = "bogota"
        const val DAYS = 3
    }

    private val repository: WeatherRepository = mockk()

    @ExperimentalCoroutinesApi
    @Test
    fun shouldGetDetailByQuery() {
        val searchUseCase = GetWeatherDetailUseCase(
            UnconfinedTestDispatcher(), repository
        )

        val response: WeatherDetailModel = mockk(relaxed = true)
        var result: Result<WeatherDetailModel>? = null

        scenario("should get detail by query correctly") {
            given {
                coEvery {
                    repository.getWeatherDetail(QUERY, DAYS)
                } returns response
            }
            execute {
                runBlocking {
                    result = searchUseCase.invoke(
                        GetWeatherDetailUseCase.Parameters(QUERY, DAYS)
                    )
                }
            }
            then {
                Assert.assertNotNull(result)
                Assert.assertEquals(result!!.data, response)
                coVerify {
                    repository.getWeatherDetail(QUERY, DAYS)
                }
                confirmVerified(repository)
            }
        }
    }
}
