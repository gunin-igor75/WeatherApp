package com.github.gunin_igor75.weatherapp.data.mapper

import com.github.gunin_igor75.weatherapp.data.network.dto.WeatherCurrentDto
import com.github.gunin_igor75.weatherapp.data.network.dto.WeatherDto
import com.github.gunin_igor75.weatherapp.data.network.dto.WeatherForecastDto
import com.github.gunin_igor75.weatherapp.domain.entity.Forecast
import com.github.gunin_igor75.weatherapp.domain.entity.Weather
import java.util.Calendar
import java.util.Date

fun WeatherCurrentDto.toWeather(): Weather = weatherDto.toWeather()

fun WeatherDto.toWeather(): Weather {
    return Weather(
        tempC = tempC,
        conditionText = conditionDto.text,
        conditionUrl = conditionDto.iconUrl.toCorrected(),
        date = date.toCalendar()
    )
}

fun WeatherForecastDto.toForecast(): Forecast {
    val weather = weatherDto.toWeather()
    val weathers = this.forecastDto.forecastDay.drop(1).map { dayDto ->
        val date = dayDto.date
        val tempC = dayDto.dayWeatherDto.tempC
        val conditionDto = dayDto.dayWeatherDto.conditionDto
        Weather(
            tempC = tempC,
            conditionText = conditionDto.text,
            conditionUrl = conditionDto.iconUrl.toCorrected(),
            date = date.toCalendar()
        )
    }
    return Forecast(
        currentWeather = weather,
        upcoming = weathers
    )
}

private fun Long.toCalendar() = Calendar.getInstance().apply {
    time = Date(this@toCalendar * 1000)
}

private fun String.toCorrected() = "https:$this".replace(
    oldValue = "64x64",
    newValue = "128x128"
)
