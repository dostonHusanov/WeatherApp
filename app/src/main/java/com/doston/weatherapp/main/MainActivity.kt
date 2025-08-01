package com.doston.weatherapp.main

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Surface
import androidx.compose.ui.Modifier
import androidx.lifecycle.ViewModelProvider
import com.doston.weatherapp.WeatherViewModelFactory
import com.doston.weatherapp.viewModel.WeatherViewModel
import com.doston.weatherapp.ui.theme.WeatherAppTheme

class MainActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)



        val factory = WeatherViewModelFactory(application)
        val weatherViewModel = ViewModelProvider(this, factory)[WeatherViewModel::class.java]

        setContent {
            WeatherAppTheme {
                Surface(
                    modifier = Modifier.fillMaxSize(),
                    color = MaterialTheme.colorScheme.background
                ) {
                    HomeScreen(weatherViewModel)
                }
            }
        }
    }
}