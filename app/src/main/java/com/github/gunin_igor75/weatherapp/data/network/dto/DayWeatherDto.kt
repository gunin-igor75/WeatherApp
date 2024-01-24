package com.github.gunin_igor75.weatherapp.data.network.dto

import com.google.gson.annotations.SerializedName

data class DayWeatherDto(
    @SerializedName("avgtemp_c") val tempC: Float,
    @SerializedName("condition") val conditionDto: ConditionDto
)
