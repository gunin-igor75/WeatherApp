package com.github.gunin_igor75.weatherapp.data.repository

import com.github.gunin_igor75.weatherapp.data.mapper.toForecast
import com.github.gunin_igor75.weatherapp.data.mapper.toWeather
import com.github.gunin_igor75.weatherapp.data.network.api.ApiService
import com.github.gunin_igor75.weatherapp.domain.entity.Forecast
import com.github.gunin_igor75.weatherapp.domain.entity.Weather
import com.github.gunin_igor75.weatherapp.domain.repositoriy.WeatherRepository
import javax.inject.Inject

class WeatherRepositoryImpl @Inject constructor(
    private val apiService: ApiService
) : WeatherRepository {

    override suspend fun getWeather(cityId: Int): Weather =
        apiService.loadCurrentWeather("$PREFIX_CITY_ID$cityId").toWeather()

    override suspend fun getForecast(cityId: Int): Forecast  =
        apiService.loadForecast("$PREFIX_CITY_ID$cityId").toForecast()

    companion object {
        private const val PREFIX_CITY_ID = "id:"
    }
}