package com.github.gunin_igor75.weatherapp.presentation.favorite

import com.github.gunin_igor75.weatherapp.domain.entity.City
import kotlinx.coroutines.flow.StateFlow

interface FavoriteComponent {
    val model: StateFlow<FavoriteStore.State>
    fun onClickSearch()
    fun onClickFavorite()
    fun onClickCityItemClick(city: City)
}