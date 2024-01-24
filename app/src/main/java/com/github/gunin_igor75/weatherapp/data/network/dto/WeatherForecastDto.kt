package com.github.gunin_igor75.weatherapp.data.network.dto

import com.google.gson.annotations.SerializedName

data class WeatherForecastDto(
    @SerializedName("current") val weatherDto: WeatherDto,
    @SerializedName("forecast") val forecastDto: ForecastDto

)