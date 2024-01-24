package com.github.gunin_igor75.weatherapp.domain.useCase

import com.github.gunin_igor75.weatherapp.domain.repositoriy.FavoriteRepository
import javax.inject.Inject

class ObserveFavoriteStateUseCase @Inject constructor(
    private val repository: FavoriteRepository
) {
    operator fun invoke(cityId: Int) = repository.observeIsFavorite(cityId)
}