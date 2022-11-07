package com.weather.app.data.datasource.remote.repository

import com.weather.app.data.datasource.remote.WeatherNetworkClient
import com.weather.app.data.datasource.remote.model.WeatherDetailResponse
import com.weather.app.data.datasource.remote.model.WeatherResponse
import com.weather.app.data.datasource.remote.model.toDomain
import com.weather.app.domain.model.WeatherDetailModel
import com.weather.app.domain.model.WeatherModel
import com.weather.app.framework.network.Failure
import com.weather.app.framework.network.NetworkHandler
import com.weather.app.framework.scenario
import io.mockk.coEvery
import io.mockk.coVerify
import io.mockk.confirmVerified
import io.mockk.every
import io.mockk.mockk
import io.mockk.verify
import junit.framework.TestCase
import kotlinx.coroutines.runBlocking
import org.junit.Test

class WeatherRepositoryImplTest {
    private companion object {
        const val NETWORK_CONNECTED_TRUE = true
        const val VERIFY_ONE_INTERACTION = 1
        const val NETWORK_CONNECTED_FALSE = false
        const val VERIFY_ZERO_INTERACTIONS = 0
        const val QUERY = "bogota"
        const val DAYS = 3
    }

    private val networkHandler: NetworkHandler = mockk()
    private val weatherNetworkClient: WeatherNetworkClient = mockk()

    @Test
    fun shouldSearchByQueryCorrectly() {
        val response: List<WeatherResponse> = mockk(relaxed = true)
        var result: List<WeatherModel>? = null

        scenario("Should search by query, when network connection is correctly") {
            val repositoryImpl = WeatherRepositoryImpl(
                networkHandler, weatherNetworkClient
            )
            given {
                every { networkHandler.isConnected } returns NETWORK_CONNECTED_TRUE
                coEvery {
                    weatherNetworkClient.search(QUERY)
                } returns response
            }
            execute {
                runBlocking {
                    result = repositoryImpl.search(QUERY)
                }
            }
            then {
                TestCase.assertNotNull(result)
                TestCase.assertEquals(result, response.toDomain())
                verify(exactly = VERIFY_ONE_INTERACTION) { networkHandler.isConnected }
                coVerify {
                    weatherNetworkClient.search(QUERY)
                }
                confirmVerified(networkHandler, weatherNetworkClient)
            }
        }
    }

    @Test(expected = Failure.NetworkConnection::class)
    fun shouldSearchByQueryUnsuccessfully() {
        var result: List<WeatherModel>? = null
        scenario("Should search by query, when network connection is unsuccessfully") {
            val repositoryImpl = WeatherRepositoryImpl(
                networkHandler, weatherNetworkClient
            )
            given {
                every { networkHandler.isConnected } returns NETWORK_CONNECTED_FALSE
            }
            execute {
                runBlocking {
                    result = repositoryImpl.search(QUERY)
                }
            }
            then {
                TestCase.assertEquals(
                    result,
                    Failure.NetworkConnection::class.java.canonicalName
                )
                verify(exactly = VERIFY_ONE_INTERACTION) { networkHandler.isConnected }
                coVerify(exactly = VERIFY_ZERO_INTERACTIONS) {
                    weatherNetworkClient.search(QUERY)
                }
                confirmVerified(networkHandler)
            }
        }
    }

    @Test
    fun shouldGetDetailByQueryCorrectly() {
        val response: WeatherDetailResponse = mockk(relaxed = true)
        var result: WeatherDetailModel? = null

        scenario("Should get detail by query, when network connection is correctly") {
            val repositoryImpl = WeatherRepositoryImpl(
                networkHandler, weatherNetworkClient
            )
            given {
                every { networkHandler.isConnected } returns NETWORK_CONNECTED_TRUE
                coEvery {
                    weatherNetworkClient.getWeatherDetail(QUERY, DAYS)
                } returns response
            }
            execute {
                runBlocking {
                    result = repositoryImpl.getWeatherDetail(QUERY, DAYS)
                }
            }
            then {
                TestCase.assertNotNull(result)
                TestCase.assertEquals(result, response.toDomain())
                verify(exactly = VERIFY_ONE_INTERACTION) { networkHandler.isConnected }
                coVerify {
                    weatherNetworkClient.getWeatherDetail(QUERY, DAYS)
                }
                confirmVerified(networkHandler, weatherNetworkClient)
            }
        }
    }

    @Test(expected = Failure.NetworkConnection::class)
    fun shouldGetDetailByQueryUnsuccessfully() {
        var result: WeatherDetailModel? = null
        scenario("Should get detail by query, when network connection is unsuccessfully") {
            val repositoryImpl = WeatherRepositoryImpl(
                networkHandler, weatherNetworkClient
            )
            given {
                every { networkHandler.isConnected } returns NETWORK_CONNECTED_FALSE
            }
            execute {
                runBlocking {
                    result = repositoryImpl.getWeatherDetail(QUERY, DAYS)
                }
            }
            then {
                TestCase.assertEquals(
                    result,
                    Failure.NetworkConnection::class.java.canonicalName
                )
                verify(exactly = VERIFY_ONE_INTERACTION) { networkHandler.isConnected }
                coVerify(exactly = VERIFY_ZERO_INTERACTIONS) {
                    weatherNetworkClient.getWeatherDetail(QUERY, DAYS)
                }
                confirmVerified(networkHandler)
            }
        }
    }
}
