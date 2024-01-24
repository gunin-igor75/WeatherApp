package com.github.gunin_igor75.weatherapp.presentation

import android.os.Bundle
import android.util.Log
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import com.github.gunin_igor75.weatherapp.data.network.api.ApiFactory
import com.github.gunin_igor75.weatherapp.presentation.ui.theme.WeatherAppTheme
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch

class MainActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        val apiService = ApiFactory.apiService
        val scope = CoroutineScope(Dispatchers.Main.immediate)
        scope.launch {
            val currentWeather = apiService.loadCurrentWeather("london")
            Log.d("MainActivity", currentWeather.toString())
        }
        scope.launch {
            val forecast = apiService.loadForecast("london")
            Log.d("MainActivity", forecast.toString())
        }

        scope.launch {
            val cities = apiService.searchCity("lon")
            Log.d("MainActivity", cities.toString())
        }

        setContent {
            WeatherAppTheme {

            }
        }
    }
}

