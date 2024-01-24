package com.github.gunin_igor75.weatherapp.domain.repositoriy

import com.github.gunin_igor75.weatherapp.domain.entity.City

interface SearchRepository {
    suspend fun search(query: String): List<City>
}