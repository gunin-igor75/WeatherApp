package com.github.gunin_igor75.weatherapp.data.mapper

import com.github.gunin_igor75.weatherapp.data.local.model.CityDbModel
import com.github.gunin_igor75.weatherapp.domain.entity.City


fun City.toDbModel(): CityDbModel = CityDbModel(id, name, country)

fun CityDbModel.toCity(): City = City(id, name, country)

fun List<CityDbModel>.toCities(): List<City> = map { it.toCity() }