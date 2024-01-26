package com.github.gunin_igor75.weatherapp.presentation.search

import com.github.gunin_igor75.weatherapp.domain.entity.City
import kotlinx.coroutines.flow.StateFlow

interface SearchComponent {

    val model: StateFlow<SearchStore.State>
    fun changeSearchQuery(searchQuery: String)
    fun onClickBack()
    fun onClickSearch()
    fun onClickCity(city: City)
}