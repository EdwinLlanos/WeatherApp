package com.weather.app.data.datasource.remote

import com.weather.app.data.datasource.remote.model.WeatherDetailResponse
import com.weather.app.data.datasource.remote.model.WeatherResponse
import com.weather.app.framework.ktorHttpClientTest
import com.weather.app.framework.scenario
import io.ktor.client.engine.mock.MockEngine
import io.ktor.client.engine.mock.respond
import io.ktor.http.HttpHeaders
import io.ktor.http.HttpStatusCode
import io.ktor.http.headersOf
import io.ktor.utils.io.ByteReadChannel
import kotlinx.coroutines.runBlocking
import org.junit.Assert
import org.junit.Test

private const val WEATHER_SEARCH_FAKE = """
[
    {
        "id": 502209,
        "name": "Bogota",
        "country": "Colombia"
      
    },
    {
        "id": 524008,
        "name": "Soacha",
        "country": "Colombia"
    }
]
"""

private const val WEATHER_FORECAST_FAKE = """
{
  "location": {
    "name": "Bogota",
    "localtime": "2022-11-06 21:41"
  },
  "current": {
    "temp_c": 12.0,
    "condition": {
      "text": "Partly cloudy",
      "icon": "//cdn.weatherapi.com/weather/64x64/night/116.png",
      "code": 1003
    }
  },
  "forecast": {
    "forecastday": [
      {
        "date": "2022-11-06",
        "day": {
          "avgtemp_c": 12.8,
          "condition": {
            "text": "Moderate rain",
            "icon": "//cdn.weatherapi.com/weather/64x64/day/302.png"
          }
        }
      }
    ]
  }
}
"""

class WeatherNetworkClientTest {
    @Test
    fun shouldSearchQueryCorrectly() {
        var apiClient: WeatherNetworkClient? = null
        var response: List<WeatherResponse>? = null
        val query = "bogota"

        scenario("should search by query correctly") {
            given {
                val mockEngine = MockEngine {
                    respond(
                        content = ByteReadChannel(WEATHER_SEARCH_FAKE),
                        status = HttpStatusCode.OK,
                        headers = headersOf(HttpHeaders.ContentType, "application/json"),
                    )
                }
                val httpClient = ktorHttpClientTest(mockEngine)
                apiClient = WeatherNetworkClient(httpClient)
            }
            execute {
                runBlocking {
                    response = apiClient?.search(query)
                }
            }
            then {
                Assert.assertNotNull(response)
                Assert.assertEquals(2, response!!.count())
                Assert.assertEquals(502209, response!!.first().id)
                Assert.assertEquals("Colombia", response!!.first().country)
                Assert.assertEquals("Bogota", response!!.first().name)

                Assert.assertEquals(524008, response!!.last().id)
                Assert.assertEquals("Colombia", response!!.last().country)
                Assert.assertEquals("Soacha", response!!.last().name)
            }
        }
    }

    @Test
    fun shouldGetDetailByQueryCorrectly() {
        var apiClient: WeatherNetworkClient? = null
        var response: WeatherDetailResponse? = null
        val query = "bogota"
        val days = 3

        scenario("should get detail by query correctly") {
            given {
                val mockEngine = MockEngine {
                    respond(
                        content = ByteReadChannel(WEATHER_FORECAST_FAKE),
                        status = HttpStatusCode.OK,
                        headers = headersOf(HttpHeaders.ContentType, "application/json"),
                    )
                }
                val httpClient = ktorHttpClientTest(mockEngine)
                apiClient = WeatherNetworkClient(httpClient)
            }
            execute {
                runBlocking {
                    response = apiClient?.getWeatherDetail(query, days)
                }
            }
            then {
                Assert.assertNotNull(response)
                Assert.assertNotNull(response!!.location)
                Assert.assertNotNull(response!!.forecast)
                Assert.assertNotNull(response!!.current)
                Assert.assertEquals("2022-11-06 21:41", response!!.location.localtime)
                Assert.assertEquals("Bogota", response!!.location.name)
                Assert.assertNotNull(response!!.current.condition)
                Assert.assertEquals(1, response!!.forecast.forecastDay.count())
                Assert.assertNotNull(response!!.forecast.forecastDay.first().day)
            }
        }
    }
}
