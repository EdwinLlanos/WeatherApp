package com.weather.app.domain.usecase

import com.weather.app.domain.model.WeatherModel
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

class SearchUseCaseTest {
    private companion object {
        const val QUERY = "bogota"
    }

    private val repository: WeatherRepository = mockk()

    @ExperimentalCoroutinesApi
    @Test
    fun shouldSearchByQuery() {
        val searchUseCase = SearchUseCase(
            UnconfinedTestDispatcher(), repository
        )

        val response: List<WeatherModel> = mockk(relaxed = true)
        var result: Result<List<WeatherModel>>? = null

        scenario("should search by query correctly") {
            given {
                coEvery {
                    repository.search(QUERY)
                } returns response
            }
            execute {
                runBlocking {
                    result = searchUseCase.invoke(
                        SearchUseCase.Parameters(QUERY)
                    )
                }
            }
            then {
                Assert.assertNotNull(result)
                Assert.assertEquals(result!!.data, response)
                coVerify {
                    repository.search(QUERY)
                }
                confirmVerified(repository)
            }
        }
    }
}
