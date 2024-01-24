package com.github.gunin_igor75.weatherapp.domain.useCase

import com.github.gunin_igor75.weatherapp.domain.repositoriy.WeatherRepository
import javax.inject.Inject

class GetWeatherUseCase @Inject constructor(
    private val repository: WeatherRepository
) {
    suspend operator fun invoke(cityId: Int) = repository.getWeather(cityId)
}