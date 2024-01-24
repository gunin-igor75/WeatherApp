package com.github.gunin_igor75.weatherapp.domain.useCase

import com.github.gunin_igor75.weatherapp.domain.repositoriy.FavoriteRepository
import javax.inject.Inject

class GetFavoriteCitiesUseCase @Inject constructor(
    private val repository: FavoriteRepository
) {
    operator fun invoke() = repository.favoriteCities
}