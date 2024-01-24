package com.github.gunin_igor75.weatherapp.domain.repositoriy

import com.github.gunin_igor75.weatherapp.domain.entity.Forecast
import com.github.gunin_igor75.weatherapp.domain.entity.Weather

interface WeatherRepository {

    suspend fun getWeather(cityId:Int): Weather

    suspend fun getForecast(cityId:Int): Forecast
}