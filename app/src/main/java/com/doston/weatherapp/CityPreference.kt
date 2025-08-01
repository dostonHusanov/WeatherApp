package com.doston.weatherapp

// package: com.doston.weatherapp.data

import android.content.Context
import com.google.gson.Gson
import com.google.gson.reflect.TypeToken

class CityPreference(context: Context) {

    private val prefs = context.getSharedPreferences("weather_prefs", Context.MODE_PRIVATE)
    private val gson = Gson()

    companion object {
        private const val CITY_LIST_KEY = "city_list"
    }

    fun saveCity(city: String) {
        val currentList = getCityList().toMutableList()
        if (!currentList.contains(city)) {
            currentList.add(0, city) // recent on top
            prefs.edit().putString(CITY_LIST_KEY, gson.toJson(currentList.take(5))).apply()
        }
    }

    fun getCityList(): List<String> {
        val json = prefs.getString(CITY_LIST_KEY, null) ?: return emptyList()
        val type = object : TypeToken<List<String>>() {}.type
        return gson.fromJson(json, type)
    }
}
