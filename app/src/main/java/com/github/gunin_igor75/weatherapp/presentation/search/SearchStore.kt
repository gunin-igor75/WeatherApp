package com.github.gunin_igor75.weatherapp.presentation.search

import com.arkivanov.mvikotlin.core.store.Reducer
import com.arkivanov.mvikotlin.core.store.Store
import com.arkivanov.mvikotlin.core.store.StoreFactory
import com.arkivanov.mvikotlin.extensions.coroutines.CoroutineBootstrapper
import com.arkivanov.mvikotlin.extensions.coroutines.CoroutineExecutor
import com.github.gunin_igor75.weatherapp.domain.entity.City
import com.github.gunin_igor75.weatherapp.domain.useCase.ChangeFavoriteStateUseCase
import com.github.gunin_igor75.weatherapp.domain.useCase.SearchUseCase
import com.github.gunin_igor75.weatherapp.presentation.search.SearchStore.Intent
import com.github.gunin_igor75.weatherapp.presentation.search.SearchStore.Label
import com.github.gunin_igor75.weatherapp.presentation.search.SearchStore.State
import kotlinx.coroutines.Job
import kotlinx.coroutines.launch
import javax.inject.Inject

interface SearchStore : Store<Intent, State, Label> {

    sealed interface Intent {
        data object ClickBack : Intent
        data class ChangeSearchQuery(val searchQuery: String) : Intent
        data object ClickSearch : Intent
        data class ClickCity(val city: City) : Intent

    }

    data class State(
        val searchQuery: String,
        val searchState: SearchState
    ) {
        sealed interface SearchState {
            data object Initial : SearchState
            data object Loading : SearchState
            data object Error : SearchState
            data object EmptyResult : SearchState
            data class SuccessLoaded(val cities: List<City>) : SearchState
        }
    }

    sealed interface Label {
        data object ClickBack : Label
        data object SavedToFavorite : Label
        data class OpenForecast(val city: City) : Label

    }
}

class SearchStoreFactory @Inject constructor(
    private val storeFactory: StoreFactory,
    private val searchUseCase: SearchUseCase,
    private val changeFavoriteStateUseCase: ChangeFavoriteStateUseCase
) {

    fun create(openReason: OpenReason): SearchStore =
        object : SearchStore, Store<Intent, State, Label> by storeFactory.create(
            name = "SearchStore",
            initialState = State(
                searchQuery = "",
                searchState = State.SearchState.Initial
            ),
            bootstrapper = BootstrapperImpl(),
            executorFactory = { ExecutorImpl(openReason) },
            reducer = ReducerImpl
        ) {}

    private sealed interface Action {
    }

    private sealed interface Msg {
        data class ChangeSearchQuery(val searchQuery: String) : Msg
        data object LoadingSearchError : Msg
        data object LoadingSearchResult : Msg
        data class SearchResultLoaded(val cities: List<City>) : Msg

    }

    private class BootstrapperImpl : CoroutineBootstrapper<Action>() {
        override fun invoke() {
        }
    }

    private inner class ExecutorImpl(
        val openReason: OpenReason
    ) : CoroutineExecutor<Intent, Action, State, Msg, Label>() {

        private var searchJob: Job? = null
        override fun executeIntent(intent: Intent, getState: () -> State) {
            when (intent) {
                is Intent.ChangeSearchQuery -> {
                    dispatch(Msg.ChangeSearchQuery(intent.searchQuery))
                }

                Intent.ClickBack -> {
                    publish(Label.ClickBack)
                }

                is Intent.ClickCity -> {
                    when (openReason) {
                        OpenReason.AddToFavorite -> {
                            publish(Label.SavedToFavorite)
                            scope.launch {
                                changeFavoriteStateUseCase.addToFavorite(intent.city)
                            }
                        }

                        OpenReason.RegularSearch -> {
                            publish(Label.OpenForecast(intent.city))
                        }
                    }
                }

                Intent.ClickSearch -> {
                    searchJob?.cancel()
                    searchJob = scope.launch {
                        dispatch(Msg.LoadingSearchResult)
                        try {
                            val query = getState().searchQuery
                            val cities = searchUseCase(query)
                            dispatch(Msg.SearchResultLoaded(cities = cities))
                        } catch (e: Exception) {
                            dispatch(Msg.LoadingSearchError)
                        }
                    }
                }
            }
        }

    }

    private object ReducerImpl : Reducer<State, Msg> {
        override fun State.reduce(msg: Msg): State =
            when (msg) {
                is Msg.ChangeSearchQuery -> {
                    copy(searchQuery = msg.searchQuery)
                }

                Msg.LoadingSearchError -> {
                    copy(searchState = State.SearchState.Error)
                }

                Msg.LoadingSearchResult -> {
                    copy(searchState = State.SearchState.Loading)
                }

                is Msg.SearchResultLoaded -> {
                    val cities = msg.cities
                    if (cities.isEmpty()) {
                        copy(searchState = State.SearchState.EmptyResult)
                    } else {
                        copy(searchState = State.SearchState.SuccessLoaded(cities = cities))
                    }
                }
            }
    }
}
