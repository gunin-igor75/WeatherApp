package com.github.gunin_igor75.weatherapp.domain.useCase

import com.github.gunin_igor75.weatherapp.domain.entity.City
import com.github.gunin_igor75.weatherapp.domain.repositoriy.FavoriteRepository
import javax.inject.Inject

class ChangeFavoriteStateUseCase @Inject constructor(
    private val repository: FavoriteRepository
) {

    suspend fun addToFavorite(city: City) = repository.addToFavorite(city)

    suspend fun removeFromFavorite(cityId: Int) = repository.removeFromFavorite(cityId)

}