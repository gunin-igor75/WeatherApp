package com.github.gunin_igor75.weatherapp.presentation.details

import com.arkivanov.mvikotlin.core.store.Reducer
import com.arkivanov.mvikotlin.core.store.Store
import com.arkivanov.mvikotlin.core.store.StoreFactory
import com.arkivanov.mvikotlin.extensions.coroutines.CoroutineBootstrapper
import com.arkivanov.mvikotlin.extensions.coroutines.CoroutineExecutor
import com.github.gunin_igor75.weatherapp.domain.entity.City
import com.github.gunin_igor75.weatherapp.domain.entity.Forecast
import com.github.gunin_igor75.weatherapp.domain.useCase.ChangeFavoriteStateUseCase
import com.github.gunin_igor75.weatherapp.domain.useCase.GetForecastUseCase
import com.github.gunin_igor75.weatherapp.domain.useCase.ObserveFavoriteStateUseCase
import com.github.gunin_igor75.weatherapp.presentation.details.DetailsStore.Intent
import com.github.gunin_igor75.weatherapp.presentation.details.DetailsStore.Label
import com.github.gunin_igor75.weatherapp.presentation.details.DetailsStore.State
import kotlinx.coroutines.launch
import javax.inject.Inject

interface DetailsStore : Store<Intent, State, Label> {

    sealed interface Intent {
        data object ClickBack : Intent
        data object ClickChangeFavoriteStatus : Intent
    }

    data class State(
        val city: City,
        val isFavorite: Boolean,
        val forecastState: ForecastState
    ) {

        sealed interface ForecastState {
            data object Initial : ForecastState
            data object Loading : ForecastState
            data object Error : ForecastState
            data class Loaded(val forecast: Forecast) : ForecastState
        }
    }

    sealed interface Label {
        data object ClickBack : Label
    }
}

class DetailsStoreFactory @Inject constructor(
    private val storeFactory: StoreFactory,
    private val getForecastUseCase: GetForecastUseCase,
    private val observeFavoriteStateUseCase: ObserveFavoriteStateUseCase,
    private val changeFavoriteStateUseCase: ChangeFavoriteStateUseCase
) {

    fun create(city: City): DetailsStore =
        object : DetailsStore, Store<Intent, State, Label> by storeFactory.create(
            name = "DetailsStore",
            initialState = State(
                city = city,
                isFavorite = false,
                forecastState = State.ForecastState.Initial
            ),
            bootstrapper = BootstrapperImpl(city),
            executorFactory = ::ExecutorImpl,
            reducer = ReducerImpl
        ) {}

    private sealed interface Action {
        data class FavoriteStatusChange(val isFavorite: Boolean) : Action
        data class ForecastLoaded(val forecast: Forecast) : Action
        data object ForecastLoading : Action
        data object ForecastLoadingError : Action

    }

    private sealed interface Msg {
        data class FavoriteStatusChange(val isFavorite: Boolean) : Msg
        data class ForecastLoaded(val forecast: Forecast) : Msg
        data object ForecastLoading : Msg
        data object ForecastLoadingError : Msg
    }

    private inner class BootstrapperImpl(val city: City) : CoroutineBootstrapper<Action>() {
        override fun invoke() {
            scope.launch {
                observeFavoriteStateUseCase(city.id).collect {
                    dispatch(Action.FavoriteStatusChange(it))
                }
            }
            scope.launch {
                dispatch(Action.ForecastLoading)
                try {
                    val forecast = getForecastUseCase(city.id)
                    dispatch(Action.ForecastLoaded(forecast = forecast))
                } catch (e: Exception) {
                    dispatch(Action.ForecastLoadingError)
                }
            }
        }
    }

    private inner class ExecutorImpl : CoroutineExecutor<Intent, Action, State, Msg, Label>() {
        override fun executeIntent(intent: Intent, getState: () -> State) {
            when (intent) {
                Intent.ClickBack -> {
                    publish(Label.ClickBack)
                }

                Intent.ClickChangeFavoriteStatus -> {
                    scope.launch {
                        val state = getState()
                        val city = state.city
                        if (state.isFavorite) {
                            changeFavoriteStateUseCase.removeFromFavorite(city.id)
                        } else {
                            changeFavoriteStateUseCase.addToFavorite(city)
                        }
                    }
                }
            }
        }

        override fun executeAction(action: Action, getState: () -> State) {
            when (action) {
                is Action.FavoriteStatusChange -> {
                    dispatch(Msg.FavoriteStatusChange(action.isFavorite))
                }

                is Action.ForecastLoaded -> {
                    dispatch(Msg.ForecastLoaded(action.forecast))
                }

                Action.ForecastLoading -> {
                    dispatch(Msg.ForecastLoading)
                }

                Action.ForecastLoadingError -> {
                    dispatch(Msg.ForecastLoadingError)
                }
            }
        }
    }

    private object ReducerImpl : Reducer<State, Msg> {
        override fun State.reduce(msg: Msg): State =
            when (msg) {
                is Msg.FavoriteStatusChange -> {
                    copy(isFavorite = msg.isFavorite)
                }

                is Msg.ForecastLoaded -> {
                    copy(
                        forecastState = State.ForecastState.Loaded(
                            forecast = msg.forecast
                        )
                    )
                }

                Msg.ForecastLoading -> {
                    copy(forecastState = State.ForecastState.Loading)
                }

                Msg.ForecastLoadingError -> {
                    copy(forecastState = State.ForecastState.Error)
                }
            }
    }
}
