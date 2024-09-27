package com.example.weatherapp.viewModel

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.weatherapp.model.ApiState
import com.example.weatherapp.model.Irepo
import com.example.weatherapp.model.LocationResponce
import com.example.weatherapp.model.SharedPreferencesKeys
import com.example.weatherapp.model.WeatherResponse
import kotlinx.coroutines.flow.MutableSharedFlow
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.collectLatest
import kotlinx.coroutines.launch

class MapViewModel(var repo:Irepo):ViewModel() {

    private val _resultInsert=MutableLiveData<Long>()
     val resultInsert:LiveData<Long> = _resultInsert

    private val _cityLocation = MutableStateFlow<LocationResponce?>(null)
    val cityLocatio :StateFlow<LocationResponce?>
        get() = _cityLocation


    private val _filteredCities = MutableLiveData<List<String>>()
    val filteredCities: LiveData<List<String>> = _filteredCities

    private val _forecastWeather = MutableStateFlow<ApiState<WeatherResponse>>(ApiState.Loading)
    val forecastWeather : StateFlow<ApiState<WeatherResponse>> = _forecastWeather


    private val cityList = arrayListOf(
        "القاهرة", "الاسكندرية", "الجيزة", "شبرا الخيمة", "بورسعيد",
        "السويس", "الأقصر", "أسيوط", "المنصورة", "طنطا",
        "إسماعيلية", "الفيوم", "الزقازيق", "دمياط", "أسوان",
        "المنيا", "بني سويف", "قنا", "سوهاج", "الغردقة",
        "نيويورك", "لوس أنجلوس", "لندن", "باريس", "طوكيو",
        "برلين", "موسكو", "سيدني", "تورنتو", "روما",
        "مومباي", "بكين", "دبي", "مكسيكو سيتي", "بانكوك",
        "بوينس آيرس", "إسطنبول", "سيول", "ساو باولو", "جاكرتا",
        "كيب تاون", "مدريد", "فيينا", "برشلونة", "أثينا",
        "لشبونة", "براغ", "وارسو", "أمستردام", "بروكسل",
        "هونج كونج", "شنغهاي", "كوالا لامبور", "سنغافورة", "لاجوس",
        "دبلن", "كوبنهاغن", "ستوكهولم", "هلسنكي", "أوسلو",
        "فانكوفر", "ملبورن", "زيورخ", "جنيف", "إدنبرة",
        "بريسبان", "كلكتا", "كراتشي", "الرياض", "تل أبيب",
        "الدار البيضاء", "مانيلا", "ليما", "هافانا", "كييف",
        "نايروبي", "هانوي", "فيينا", "بودابست", "ميونخ",
        "البندقية", "فلورنسا", "سالفادور", "ريو دي جانيرو", "ليون",
        "مرسيليا", "كراكوف", "كوبنهاغن", "مونتريال", "أوساكا",
        "Cairo", "Alexandria", "Giza", "Shubra El-Kheima", "Port Said",
        "Suez", "Luxor", "Asyut", "Mansoura", "Tanta",
        "Ismailia", "Faiyum", "Zagazig", "Damietta", "Aswan",
        "Minya", "Beni Suef", "Qena", "Sohag", "Hurghada",
        "New York", "Los Angeles", "London", "Paris", "Tokyo",
        "Berlin", "Moscow", "Sydney", "Toronto", "Rome",
        "Mumbai", "Beijing", "Dubai", "Mexico City", "Bangkok",
        "Buenos Aires", "Istanbul", "Seoul", "Sao Paulo", "Jakarta",
        "Cape Town", "Madrid", "Vienna", "Barcelona", "Athens",
        "Lisbon", "Prague", "Warsaw", "Amsterdam", "Brussels",
        "Hong Kong", "Shanghai", "Kuala Lumpur", "Singapore", "Lagos",
        "Dublin", "Copenhagen", "Stockholm", "Helsinki", "Oslo",
        "Vancouver", "Melbourne", "Zurich", "Geneva", "Edinburgh",
        "Brisbane", "Kolkata", "Karachi", "Riyadh", "Tel Aviv",
        "Casablanca", "Manila", "Lima", "Havana", "Kyiv",
        "Nairobi", "Hanoi", "Vienna", "Budapest", "Munich",
        "Venice", "Florence", "Salvador", "Rio de Janeiro", "Lyon",
        "Marseille", "Krakow", "Copenhagen", "Montreal", "Osaka"
    )

//    private val cityList = arrayListOf(
//        "Cairo", "Alexandria", "Giza", "Shubra El-Kheima", "Port Said",
//        "Suez", "Luxor", "Asyut", "Mansoura", "Tanta",
//        "Ismailia", "Faiyum", "Zagazig", "Damietta", "Aswan",
//        "Minya", "Beni Suef", "Qena", "Sohag", "Hurghada",
//        "New York", "Los Angeles", "London", "Paris", "Tokyo",
//        "Berlin", "Moscow", "Sydney", "Toronto", "Rome",
//        "Mumbai", "Beijing", "Dubai", "Mexico City", "Bangkok",
//        "Buenos Aires", "Istanbul", "Seoul", "Sao Paulo", "Jakarta",
//        "Cape Town", "Madrid", "Vienna", "Barcelona", "Athens",
//        "Lisbon", "Prague", "Warsaw", "Amsterdam", "Brussels",
//        "Hong Kong", "Shanghai", "Kuala Lumpur", "Singapore", "Lagos",
//        "Dublin", "Copenhagen", "Stockholm", "Helsinki", "Oslo",
//        "Vancouver", "Melbourne", "Zurich", "Geneva", "Edinburgh",
//        "Brisbane", "Kolkata", "Karachi", "Riyadh", "Tel Aviv",
//        "Casablanca", "Manila", "Lima", "Havana", "Kyiv",
//        "Nairobi", "Hanoi", "Vienna", "Budapest", "Munich",
//        "Venice", "Florence", "Salvador", "Rio de Janeiro", "Lyon",
//        "Marseille", "Krakow", "Copenhagen", "Montreal", "Osaka",
//    )


    private val searchFlow = MutableSharedFlow<String>(replay = 1)

    init {
        // Launch coroutine to listen to searchFlow
        viewModelScope.launch {
            searchFlow.collectLatest { query ->
                _filteredCities.postValue(filterCities(query))
            }
        }
    }

    fun updateSearchQuery(query: String) {
        viewModelScope.launch {
            searchFlow.emit(query.trim().lowercase())
        }
    }

    private fun filterCities(query: String): List<String> {
        return if (query.isNotEmpty()) {
            cityList.filter { it.lowercase().startsWith(query) }
        } else {
            emptyList()
        }
    }


    fun getSettingsPrefs(key:String,default:String):String{
        return repo.getSettingsPrefs(key,default)
    }

    fun insretFavWeather(weatherResponse: WeatherResponse)
    {
        viewModelScope.launch {

            val temp = repo.getFavWeather(weatherResponse)


            val result=  repo.insert(temp)
            _resultInsert.postValue(result)


        }
    }


    fun fetchDataFromApi(lat:Double,lon:Double)
    {
        viewModelScope.launch {

                repo.getForecastWeather(lat,lon,getSettingsPrefs(SharedPreferencesKeys.Language_key,"en")).collect{
                    _forecastWeather.value=it
                }


        }

    }

    fun getLocation(name:String)
    {
        viewModelScope.launch {
            repo.getLocationByName(name).collect{
                _cityLocation.value=it
            }
        }
    }

    fun AddLocationPrefs(key: String,value: Double)
    {
        repo.AddLocationPrefs(key,value)
    }





}