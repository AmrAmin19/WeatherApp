package com.example.weatherapp.viewModel

import androidx.test.ext.junit.runners.AndroidJUnit4
import com.example.weatherapp.model.City
import com.example.weatherapp.model.Clouds
import com.example.weatherapp.model.Coord
import com.example.weatherapp.model.CurrentWeatherResponse
import com.example.weatherapp.model.FakeRepo
import com.example.weatherapp.model.FavWeather
import com.example.weatherapp.model.Main
import com.example.weatherapp.model.Rain
import com.example.weatherapp.model.Rain1h
import com.example.weatherapp.model.Sys
import com.example.weatherapp.model.Weather
import com.example.weatherapp.model.WeatherInfo
import com.example.weatherapp.model.WeatherResponse
import com.example.weatherapp.model.Wind
import getOrAwaitValue
import kotlinx.coroutines.ExperimentalCoroutinesApi
import org.hamcrest.MatcherAssert.assertThat
import org.hamcrest.core.IsEqual
import org.junit.Before
import org.junit.Test
import org.junit.runner.RunWith

@RunWith(AndroidJUnit4::class)
@ExperimentalCoroutinesApi
class FavViewModelTest  {

//    lateinit var viewModel: FavViewModel
//    lateinit var repo: FakeRepo

    private val favWeatherOne = FavWeather(name = "New York", lat = 40.7128, lon = -74.0060)
    private val favWeatherTwo = FavWeather(name = "London", lat = 51.5074, lon = -0.1278)
    private val favList=listOf(favWeatherOne, favWeatherTwo)


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
    fun getFavWeather_insertToLiveData() {

      val repo = FakeRepo(currentWeatherResponse = currentWeatherResponse, forecastWeatherResponse = forecastWeather, shouldEmitFavWeather = true)
      val viewModel = FavViewModel(repo)

        viewModel.getFavWeather()
      val result = viewModel.favWeather.getOrAwaitValue()

        assertThat(result, IsEqual(favList))
    }

    @Test
    fun getFavWeather_returnEmpty() {

        val repo = FakeRepo(currentWeatherResponse = currentWeatherResponse, forecastWeatherResponse = forecastWeather)
        val viewModel = FavViewModel(repo)

        viewModel.getFavWeather()
        val result = viewModel.favWeather.getOrAwaitValue()

        assertThat(result, IsEqual(emptyList()))
    }


    @Test
    fun insertFavWeather_insertChek_returnChek() {
        val fav= mutableListOf<FavWeather>()

        val repo = FakeRepo(currentWeatherResponse = currentWeatherResponse, forecastWeatherResponse = forecastWeather,
          favWeatherListInsert =  fav
        )
        val viewModel = FavViewModel(repo)

        viewModel.insert(favWeatherOne)
        assertThat(fav, IsEqual(listOf(favWeatherOne)))

    }
    @Test
    fun deleteFavWeather_deleteFav_returnChek() {
        val fav= mutableListOf<FavWeather>(favWeatherOne,favWeatherTwo)

        val repo = FakeRepo(currentWeatherResponse = currentWeatherResponse, forecastWeatherResponse = forecastWeather,
            favWeatherListInsert =  fav
        )
        val viewModel = FavViewModel(repo)

        viewModel.delet(favWeatherOne)
        assertThat(fav, IsEqual(listOf(favWeatherTwo)))

    }




}