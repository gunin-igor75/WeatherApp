package com.github.gunin_igor75.weatherapp.domain.repositoriy

import com.github.gunin_igor75.weatherapp.domain.entity.City
import kotlinx.coroutines.flow.Flow

interface FavoriteRepository {

    val favoriteCities: Flow<List<City>>

    fun observeIsFavorite(cityId: Int): Flow<Boolean>

    suspend fun addToFavorite(city: City)

    suspend fun removeFromFavorite(cityId: Int)
}