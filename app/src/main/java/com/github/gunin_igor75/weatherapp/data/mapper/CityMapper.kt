package com.github.gunin_igor75.weatherapp.data.mapper

import com.github.gunin_igor75.weatherapp.data.network.dto.CityDto
import com.github.gunin_igor75.weatherapp.domain.entity.City


fun CityDto.toCity(): City = City(id, name, country)

fun List<CityDto>.toCities(): List<City> = map { it.toCity() }