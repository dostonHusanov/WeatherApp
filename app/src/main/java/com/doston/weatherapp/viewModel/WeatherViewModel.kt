package com.doston.weatherapp.viewModel


import android.app.Application
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.doston.weatherapp.CityPreference
import com.doston.weatherapp.api.Constant
import com.doston.weatherapp.api.NetworkResponse
import com.doston.weatherapp.api.RetrofitInstance
import com.doston.weatherapp.data.Weather
import kotlinx.coroutines.launch

class WeatherViewModel(application: Application) :ViewModel() {

    private val weatherApi = RetrofitInstance.weatherApi
    private val _weatherResult = MutableLiveData<NetworkResponse<Weather>>()
    val weatherResult : LiveData<NetworkResponse<Weather>> = _weatherResult
    private val cityPreference = CityPreference(application.applicationContext)


    fun getData(city : String){
        _weatherResult.value = NetworkResponse.Loading
        viewModelScope.launch {
            try{
                val response = weatherApi.getWeather(Constant.apiKey,city)
                if(response.isSuccessful){
                    response.body()?.let {
                        _weatherResult.value = NetworkResponse.Success(it)
                    }
                }else{
                    _weatherResult.value = NetworkResponse.Error("Failed to load data")
                }
            }
            catch (e : Exception){
                _weatherResult.value = NetworkResponse.Error("Failed to load data")
            }

        }
    }
    fun getSavedCityList(): List<String> {
        return cityPreference.getCityList()
    }
    fun saveCity(city: String){
        cityPreference.saveCity(city)
    }
}