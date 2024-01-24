package com.github.gunin_igor75.weatherapp.data.repository

import com.github.gunin_igor75.weatherapp.data.mapper.toCities
import com.github.gunin_igor75.weatherapp.data.network.api.ApiService
import com.github.gunin_igor75.weatherapp.domain.entity.City
import com.github.gunin_igor75.weatherapp.domain.repositoriy.SearchRepository
import javax.inject.Inject

class SearchRepositoryImpl @Inject constructor(
    private val apiService: ApiService
) : SearchRepository {
    override suspend fun search(query: String): List<City> =
        apiService.searchCity(query).toCities()
}