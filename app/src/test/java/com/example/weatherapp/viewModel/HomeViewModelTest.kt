package com.example.weatherapp.viewModel

import androidx.test.ext.junit.runners.AndroidJUnit4
import app.cash.turbine.test
import com.example.weatherapp.model.ApiState
import com.example.weatherapp.model.City
import com.example.weatherapp.model.Clouds
import com.example.weatherapp.model.Coord
import com.example.weatherapp.model.CurrentWeatherResponse
import com.example.weatherapp.model.FakeRepo
import com.example.weatherapp.model.Main
import com.example.weatherapp.model.Rain
import com.example.weatherapp.model.Rain1h
import com.example.weatherapp.model.Sys
import com.example.weatherapp.model.Weather
import com.example.weatherapp.model.WeatherInfo
import com.example.weatherapp.model.WeatherResponse
import com.example.weatherapp.model.Wind
import junit.framework.TestCase.assertEquals
import junit.framework.TestCase.assertTrue
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.flow.collect
import kotlinx.coroutines.flow.first
import kotlinx.coroutines.flow.flow
import kotlinx.coroutines.flow.last
import kotlinx.coroutines.flow.takeWhile
import kotlinx.coroutines.test.runTest
import org.hamcrest.MatcherAssert.assertThat
import org.hamcrest.core.IsEqual
import org.junit.Before
import org.junit.Test
import org.junit.runner.RunWith
import org.mockito.ArgumentMatchers.anyBoolean
import org.mockito.ArgumentMatchers.anyDouble
import org.mockito.ArgumentMatchers.anyString
import org.mockito.kotlin.whenever


@RunWith(AndroidJUnit4::class)
@ExperimentalCoroutinesApi
class HomeViewModelTest {



    private val currentWeatherResponse = CurrentWeatherResponse( coord = Coord(lon = 12.34, lat = 56.78),
        weather = listOf(Weather(main = "Clouds", description = "broken clouds", icon = "04n", id = 803)),
        main = Main(temp = 12.34, feels_like = 12.34, temp_min = 12.34, temp_max = 12.34, pressure = 1013, humidity = 68, sea_level = 7, grnd_level = 9, temp_kf = 9.5),
        wind = Wind(speed = 7.22, deg = 300, gust = 3.8),
        clouds = Clouds(all = 75),
        sys = Sys(pod = "ghsgd"),
        timezone = 3600,
        id = 2673730,
        name = "Stockholm",
        cod = 200,
        base = "stations",
        dt = 9L,
        rain = Rain1h(volume = 6.8),
        visibility = 9
    )

    private val forecastWeather = WeatherResponse(cod = "200", message = 0, cnt = 40,
        list = listOf(
            WeatherInfo(dt = 1633024800L, main = Main(temp = 298.15,feels_like = 298.65,
            temp_min = 296.15, temp_max = 300.15, pressure = 1015, humidity = 83, sea_level = 4, grnd_level = 1015, temp_kf = 4.3),
            weather = listOf(Weather(id = 500, main = "Rain", description = "light rain", icon = "10d")),
            clouds = Clouds(all = 90), wind = Wind(speed = 4.1, deg = 150, gust = 9.7), visibility = 10000,
            pop = 0.36, rain = Rain( 0.26), sys = Sys(pod = "d"), dt_txt = "2024-09-29 18:00:00")
        ),
        city = City(id = 5128581, name = "New York", coord = Coord(lat = 40.7128, lon = -74.0060), country = "US", population = 8175133,
            timezone = -14400, sunrise = 1633006084L, sunset = 1633048884L)
    )





    @Test
    fun fetchCurrentWeather_currentWeatherResponse_returnCurrentWeather() = runTest {

        val repo = FakeRepo(currentWeatherResponse = currentWeatherResponse, forecastWeatherResponse = forecastWeather)
        val viewModel = HomeViewModel(repo)

        // When
        viewModel.fetchCurrentWeather(lat = 12.34, lon = 56.78)


        viewModel.currentWeatherState.test {

            val successState = awaitItem() as ApiState.Success
            assertEquals(currentWeatherResponse, successState.data)

            cancelAndIgnoreRemainingEvents()

        }

    }

    @Test
    fun fetchCurrentWeather_errorState_shouldEmitError() = runTest {
        // Given a repo that emits an error
        val fakeRepo = FakeRepo(
            currentWeatherResponse = currentWeatherResponse,
            shouldEmitError = true,
            errorMessage = "Network error",
            forecastWeatherResponse = forecastWeather
        )

        // Assign repo to viewModel
        val viewModel = HomeViewModel(fakeRepo)

        // When fetching weather
        viewModel.fetchCurrentWeather(lat = 12.34, lon = 56.78)

        // Then check the emitted state
        viewModel.currentWeatherState.test {
            val errorState = awaitItem() as ApiState.Error
            assertEquals("Network error", errorState.message)

            cancelAndIgnoreRemainingEvents()
        }
    }

    @Test
   fun getSharedPrefs_KeyAndDefault_returnUnit() {

        val repo = FakeRepo(currentWeatherResponse = currentWeatherResponse, forecastWeatherResponse = forecastWeather)
        val viewModel = HomeViewModel(repo)
      val result =  viewModel.getSettingsPrefs("unit","metric")
        assertThat(result,IsEqual("metric"))

   }

    @Test
    fun getSharedPrefs_KeyAndDefault_returnLanguage() {

        val repo = FakeRepo(currentWeatherResponse = currentWeatherResponse, forecastWeatherResponse = forecastWeather)
        val viewModel = HomeViewModel(repo)
        val result =  viewModel.getSettingsPrefs("language","en")
        assertThat(result,IsEqual("en"))
    }

    @Test
    fun getForecastWeather_weatherResponce_returnWeather() = runTest {
        val repo = FakeRepo(currentWeatherResponse = currentWeatherResponse, forecastWeatherResponse = forecastWeather)
        val viewModel = HomeViewModel(repo)

        // When
        viewModel.fetchForecastWeather(lat = 12.34, lon = 56.78)

        // Then check the emitted state
        viewModel.forecastWeather.test {
            val successState = awaitItem() as ApiState.Success
            assertEquals(forecastWeather, successState.data)

            cancelAndIgnoreRemainingEvents()
        }
    }
    @Test
    fun getForecastWeather_errorState_shouldEmitError() = runTest {
        // Given a repo that emits an error
        val fakeRepo = FakeRepo(
            currentWeatherResponse = currentWeatherResponse,
            shouldEmitError = true,
            errorMessage = "Network error",
            forecastWeatherResponse = forecastWeather
        )

        // Assign repo to viewModel
        val viewModel = HomeViewModel(fakeRepo)

        // When fetching weather
        viewModel.fetchForecastWeather(lat = 12.34, lon = 56.78)

        // Then check the emitted state
        viewModel.forecastWeather.test {
            val errorState = awaitItem() as ApiState.Error
            assertEquals("Network error", errorState.message)

            cancelAndIgnoreRemainingEvents()
        }
    }
}




