package com.github.gunin_igor75.weatherapp.presentation.search

import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.ArrowBack
import androidx.compose.material.icons.filled.Search
import androidx.compose.material3.Card
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.SearchBar
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.focus.FocusRequester
import androidx.compose.ui.focus.focusRequester
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import com.github.gunin_igor75.weatherapp.R
import com.github.gunin_igor75.weatherapp.domain.entity.City

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun SearchContent(
    component: SearchComponent
) {
    val state by component.model.collectAsState()
    val focusRequest = remember {
        FocusRequester()
    }

    LaunchedEffect(key1 = Unit) {
        focusRequest.captureFocus()
    }

    SearchBar(
        modifier = Modifier.focusRequester(focusRequest),
        placeholder = { Text(text = stringResource(R.string.search)) },
        query = state.searchQuery,
        onQueryChange = { component.changeSearchQuery(it) },
        onSearch = { component.onClickSearch() },
        active = true,
        onActiveChange = {},
        leadingIcon = {
            IconButton(onClick = { component.onClickBack() }) {
                Icon(
                    imageVector = Icons.Default.ArrowBack,
                    contentDescription = stringResource(R.string.description_icon_back)
                )
            }
        },
        trailingIcon = {
            IconButton(onClick = { component.onClickSearch() }) {
                Icon(
                    imageVector = Icons.Default.Search,
                    contentDescription = stringResource(R.string.description_icon_search)
                )
            }
        }
    ) {
        when (val currentState = state.searchState) {
            SearchStore.State.SearchState.EmptyResult -> {
                EmptyResult()
            }

            SearchStore.State.SearchState.Error -> {
                ErrorResult()
            }

            SearchStore.State.SearchState.Initial -> {

            }

            SearchStore.State.SearchState.Loading -> {
                Loading()
            }

            is SearchStore.State.SearchState.SuccessLoaded -> {
                SuccessLoaded(
                    cities = currentState.cities,
                    onClick = { city ->
                        component.onClickCity(city)
                    }
                )
            }
        }

    }
}

@Composable
fun EmptyResult() {
    Text(
        modifier = Modifier.padding(8.dp),
        text = stringResource(R.string.search_not_found)
    )
}

@Composable
fun ErrorResult() {

}

@Composable
fun Loading() {
    Box(
        modifier = Modifier.fillMaxSize(),
        contentAlignment = Alignment.Center
    ) {
        CircularProgressIndicator()
    }
}

@Composable
fun SuccessLoaded(
    cities: List<City>,
    onClick: (City) -> Unit
) {
    LazyColumn(
        modifier = Modifier.fillMaxSize(),
        verticalArrangement = Arrangement.spacedBy(8.dp),
        contentPadding = PaddingValues(16.dp)
    ) {
        items(
            items = cities,
            key = { it.id }
        ) {
            CityItem(
                city = it,
                onClick = onClick
            )
        }
    }
}

@Composable
fun CityItem(
    city: City,
    onClick: (City) -> Unit
) {
    Card(
        shape = MaterialTheme.shapes.small,
    ) {
        Column(
            modifier = Modifier
                .fillMaxWidth()
                .padding(8.dp)
                .clickable { onClick(city) }
        ) {
            Text(
                text = city.name,
                style = MaterialTheme.typography.titleMedium
            )
            Spacer(modifier = Modifier.height(8.dp))
            Text(text = city.country)
        }
    }
}

@Preview(showBackground = true)
@Composable
fun CityItemPreview() {
    CityItem(
        city = City.DEFAULT,
        onClick = {}
    )
}


@Preview(showBackground = true)
@Composable
fun SuccessLoadedPreview() {
    SuccessLoaded(
        cities = listCities,
        onClick = {}
    )
}

val listCities = listOf(
    City(
        id = 0,
        name = "Москва",
        country = "Россия"
    ),
    City(
        id = 1,
        name = "Омск",
        country = "Россия"
    ),
    City(
        id = 2,
        name = "Берлин",
        country = "Германия"
    ),
    City(
        id = 3,
        name = "Минск",
        country = "Белоруссия"
    ),
    City(
        id = 4,
        name = "Милан",
        country = "Италия"
    )
)