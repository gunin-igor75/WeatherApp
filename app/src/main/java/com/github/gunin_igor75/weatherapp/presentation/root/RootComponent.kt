package com.github.gunin_igor75.weatherapp.presentation.root

import com.arkivanov.decompose.router.stack.ChildStack
import com.arkivanov.decompose.value.Value
import com.github.gunin_igor75.weatherapp.presentation.details.DetailsComponent
import com.github.gunin_igor75.weatherapp.presentation.favorite.FavoriteComponent
import com.github.gunin_igor75.weatherapp.presentation.search.SearchComponent

interface RootComponent {

    val stack: Value<ChildStack<*, Child>>

    sealed interface Child{
        data class DetailsContact(val detailsComponent: DetailsComponent): Child
        data class FavoriteContact(val favoriteComponent: FavoriteComponent): Child
        data class SearchContact(val searchComponent: SearchComponent): Child

    }
}