package com.example.weatherapp.model

import com.example.weatherapp.model.local.FakeLocalData
import com.example.weatherapp.model.local.FakeSharedPrefs
import com.example.weatherapp.model.remote.FakeRemoteData
import junit.framework.TestCase.assertEquals
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.first
import kotlinx.coroutines.flow.toList
import kotlinx.coroutines.test.runTest
import org.hamcrest.MatcherAssert.assertThat
import org.hamcrest.core.IsEqual
import org.junit.Before
import org.junit.Test

class RepoTest {

    // Remote Fake Data

    private val dayOne =  DailyForecast(date = "2024-09-29", dayName = "Sunday", minTemp = "17°C",
        maxTemp = "27°C", avgHumidity = 65.0, weatherDescription = "Sunny", icon = "01d")
    private val dayTwo =  DailyForecast(date = "2024-09-30", dayName = "Monday", minTemp = "17°C",
        maxTemp = "27°C", avgHumidity = 65.0, weatherDescription = "Sunny", icon = "01d")

    private val HourOne =  HourlyForecast(time = "12:00", temp = "22°C", weatherDescription = "Sunny", icon = "01d")
    private val HourTwo =  HourlyForecast(time = "15:00", temp = "22°C", weatherDescription = "Sunny", icon = "01d")

    private val locationOne=LocationResponce(name = "Stockholm",lon = 12.34, lat = 56.78)
    private val locationTwo=LocationResponce(name = "Stockholm",lon = 12.34, lat = 56.78)

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

    private val weatherResponse =WeatherResponse(cod = "200", message = 0, cnt = 40,
        list = listOf(WeatherInfo(dt = 1633024800L, main = Main(temp = 298.15,feels_like = 298.65,
                    temp_min = 296.15, temp_max = 300.15, pressure = 1015, humidity = 83, sea_level = 4, grnd_level = 1015, temp_kf = 4.3),
                weather = listOf(Weather(id = 500, main = "Rain", description = "light rain", icon = "10d")),
                clouds = Clouds(all = 90), wind = Wind(speed = 4.1, deg = 150, gust = 9.7), visibility = 10000,
                pop = 0.36, rain = Rain( 0.26), sys = Sys(pod = "d"), dt_txt = "2024-09-29 18:00:00")),
        city = City(id = 5128581, name = "New York", coord = Coord(lat = 40.7128, lon = -74.0060), country = "US", population = 8175133,
            timezone = -14400, sunrise = 1633006084L, sunset = 1633048884L))

    // Local Fake Data

    private val favWeatherOne = FavWeather(name = "New York", lat = 40.7128, lon = -74.0060)
    private val favWeatherTwo = FavWeather(name = "London", lat = 51.5074, lon = -0.1278)
    private val alarmOne= AlarmData(1, 134327L)
    private val alarmTwo= AlarmData(2, 12354L)
    private val insertchek=1L

    private val favWeathers= listOf(favWeatherOne,favWeatherTwo)
    private val alarms= listOf(alarmOne,alarmTwo)

    // SharedPrefs Fake Data
   private val settingsCheck="English"
    private val locationCheck=12.34
    private val alarmCheck=12



    // Init

    lateinit var fakeLocalData:FakeLocalData
    lateinit var fakeRemoteData:FakeRemoteData
    lateinit var fakesharedPrefs:FakeSharedPrefs
    lateinit var repo: Repo

    @Before
    fun setup() {
        fakeRemoteData= FakeRemoteData(
            currentWeatherResponse = currentWeatherResponse,
            weatherResponse = weatherResponse,
            dailyList = listOf(dayOne,dayTwo),
            hourlyList = listOf(HourOne,HourTwo),
            locationList = listOf(locationOne,locationTwo)
        )
        fakesharedPrefs= FakeSharedPrefs(settingsCheck = settingsCheck,locationCheck = locationCheck,alertCheck = alarmCheck)
        fakeLocalData= FakeLocalData(favWeathers = favWeathers,alarmDatas = alarms, insertChek = insertchek)
        repo= Repo.getInstance(fakeRemoteData,fakeLocalData,fakesharedPrefs)

    }

    // Remote Functions Tests

    @Test
    fun getCurrentWeather_currentWeatherResponse_returnCurrentWeather() = runTest{
        // When
        val result = repo.getCurrentWeather(lat = 12.34, lon = 56.78, lang = "en").first()
        // Then
        assertThat(result, IsEqual(ApiState.Success(currentWeatherResponse)))
    }

    @Test
    fun getForecastWeather_weatherResponce_returnWeather() = runTest{
        // When
        val result = repo.getForecastWeather(lat = 12.34, lon = 56.78, lang = "en").first()
        // Then
        assertThat(result, IsEqual(ApiState.Success(weatherResponse)))
    }

    @Test
   fun getDailyForecasts_weatherResponce_convertToDayAndReturn() {

       //When
        val result = repo.getDailyForecasts(weatherResponse)

        // Then
       assertThat(result, IsEqual(listOf(dayOne,dayTwo)))
    }

    @Test
    fun getHourlyForecasts_weatherResponce_convertToHourAndReturn() {

        //When
        val result = repo.getHourlyForecastForToday(weatherResponse)

        // Then
        assertThat(result, IsEqual(listOf(HourOne,HourTwo)))
    }

    @Test
    fun getLocationByName_locationList_returnLocationList() = runTest {
        // When
        val result = repo.getLocationByName(name = "London").toList()

        // Then
        assertThat(result, IsEqual(listOf(locationOne)))
    }

    @Test
    fun getLocationByName_invalidName_throwsException() = runTest {
        // When
        val result = repo.getLocationByName(name = "error").first()

        // Then
        assertThat(result, IsEqual(LocationResponce(" Error message", 0.0, 0.0)))
    }


    // Local Function Tests


    @Test
    fun getAllLocal_favWeathers_returnFavWeathers() = runTest{
        // When
        val result = repo.getAllLocal().first()
        // Then
        assertThat(result, IsEqual(favWeathers))
    }
    @Test
    fun insertFavWeather_insertChek_returnChek() = runTest{
        // When
        val result = repo.insert(favWeatherOne)
        // Then
        assertThat(result, IsEqual(insertchek))
    }

    @Test
    fun getAllAlarms_alarms_returnAlarms() = runTest{
        // When
        val result = repo.getAllLocalAlarm().first()
        // Then
        assertThat(result, IsEqual(alarms))
    }

    // SharedPrefs Function Tests

    @Test
    fun getSettingsPrefs_settingsCheck_returnSettingsCheck() {

        // When
        val result = repo.getSettingsPrefs(key = "", default = "")
        // Then
        assertThat(result, IsEqual(settingsCheck))
    }
    @Test
    fun getLocationPrefs_locationCheck_returnLocationCheck() {

        // When
        val result = repo.getLocationPrefs(key = "", default = 0.0)
        // Then
        assertThat(result, IsEqual(locationCheck))
    }
    @Test
    fun getAlertPrefs_alertCheck_returnAlertCheck() {

        // When
        val result = repo.getAlertPrefs(key = "", default = 0)
        // Then
        assertThat(result, IsEqual(alarmCheck))
    }




}