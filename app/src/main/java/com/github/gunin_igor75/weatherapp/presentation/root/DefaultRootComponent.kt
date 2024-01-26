package com.github.gunin_igor75.weatherapp.presentation.root

import android.os.Parcelable
import com.arkivanov.decompose.ComponentContext
import com.arkivanov.decompose.router.stack.ChildStack
import com.arkivanov.decompose.router.stack.StackNavigation
import com.arkivanov.decompose.router.stack.childStack
import com.arkivanov.decompose.router.stack.pop
import com.arkivanov.decompose.router.stack.push
import com.arkivanov.decompose.value.Value
import com.github.gunin_igor75.weatherapp.domain.entity.City
import com.github.gunin_igor75.weatherapp.presentation.details.DefaultDetailsComponent
import com.github.gunin_igor75.weatherapp.presentation.favorite.DefaultFavoriteComponent
import com.github.gunin_igor75.weatherapp.presentation.search.DefaultSearchComponent
import com.github.gunin_igor75.weatherapp.presentation.search.OpenReason
import dagger.assisted.Assisted
import dagger.assisted.AssistedFactory
import dagger.assisted.AssistedInject
import kotlinx.parcelize.Parcelize

class DefaultRootComponent @AssistedInject constructor(
    private val detailsComponentFactory: DefaultDetailsComponent.Factory,
    private val favoriteComponentFactory: DefaultFavoriteComponent.Factory,
    private val searchComponentFactory: DefaultSearchComponent.Factory,
    @Assisted("componentContext") componentContext: ComponentContext,
) : RootComponent, ComponentContext by componentContext {

    private val navigation = StackNavigation<Config>()

    override val stack: Value<ChildStack<*, RootComponent.Child>> = childStack(
        source = navigation,
        initialConfiguration = Config.FavoriteContact,
        handleBackButton = true,
        childFactory = ::child
    )

    private fun child(
        config: Config,
        componentContext: ComponentContext
    ): RootComponent.Child {
        return when (config) {
            is Config.DetailsContact -> {
                val component = detailsComponentFactory.create(
                    city = config.city,
                    onBackClicked = {
                        navigation.pop()
                    },
                    componentContext = componentContext
                )
                RootComponent.Child.DetailsContact(component)
            }

            Config.FavoriteContact -> {
                val component = favoriteComponentFactory.create(
                    onCityItemClicked = {
                        navigation.push(Config.DetailsContact(it))
                    },
                    onAddFavoriteClicked = {
                        navigation.push(Config.SearchContact(OpenReason.AddToFavorite))
                    },
                    onSearchClicked = {
                        navigation.push(Config.SearchContact(OpenReason.RegularSearch))
                    },
                    componentContext = componentContext
                )
                RootComponent.Child.FavoriteContact(component)
            }

            is Config.SearchContact -> {
                val component = searchComponentFactory.create(
                    openReason = config.openReason,
                    onBackClicked = {
                        navigation.pop()
                    },
                    onCitySavedToFavorite = {
                        navigation.pop()
                    },
                    onForecastForCityRequested = {
                        navigation.push(Config.DetailsContact(it))
                    },
                    componentContext = componentContext
                )
                RootComponent.Child.SearchContact(component)
            }
        }
    }

    private sealed interface Config : Parcelable {
        @Parcelize
        data class DetailsContact(val city: City) : Config

        @Parcelize
        data object FavoriteContact : Config

        @Parcelize
        data class SearchContact(val openReason: OpenReason) : Config
    }

    @AssistedFactory
    interface Factory {
        fun create(
            @Assisted("componentContext") componentContext: ComponentContext,
        ): DefaultRootComponent
    }
}