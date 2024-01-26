package com.github.gunin_igor75.weatherapp.presentation.details

import kotlinx.coroutines.flow.StateFlow

interface DetailsComponent {

    val model: StateFlow<DetailsStore.State>
    fun onClickBack()
    fun onClickChangeFavoriteStatus()

}