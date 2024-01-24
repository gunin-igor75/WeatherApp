package com.github.gunin_igor75.weatherapp.data.network.dto

import com.google.gson.annotations.SerializedName

data class WeatherCurrentDto(
    @SerializedName("current") val weatherDto: WeatherDto
)
