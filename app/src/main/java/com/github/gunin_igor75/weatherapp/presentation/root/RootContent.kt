package com.github.gunin_igor75.weatherapp.presentation.root

import androidx.compose.runtime.Composable
import com.arkivanov.decompose.extensions.compose.jetpack.stack.Children
import com.github.gunin_igor75.weatherapp.presentation.details.DetailsContent
import com.github.gunin_igor75.weatherapp.presentation.favorite.FavoriteContent
import com.github.gunin_igor75.weatherapp.presentation.search.SearchContent

@Composable
fun RootContent(
    rootComponent: RootComponent
){
    Children(stack = rootComponent.stack) {
        when(val instance = it.instance){
            is RootComponent.Child.DetailsContact -> {
                DetailsContent(detailsComponent = instance.detailsComponent)
            }
            is RootComponent.Child.FavoriteContact -> {
                FavoriteContent(component = instance.favoriteComponent)
            }
            is RootComponent.Child.SearchContact -> {
                SearchContent(component = instance.searchComponent)
            }
        }
    }
}