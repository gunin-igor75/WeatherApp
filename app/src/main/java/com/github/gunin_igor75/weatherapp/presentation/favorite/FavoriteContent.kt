package com.github.gunin_igor75.weatherapp.presentation.favorite

import androidx.compose.foundation.BorderStroke
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.BoxScope
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.sizeIn
import androidx.compose.foundation.lazy.grid.GridCells
import androidx.compose.foundation.lazy.grid.GridItemSpan
import androidx.compose.foundation.lazy.grid.LazyVerticalGrid
import androidx.compose.foundation.lazy.grid.itemsIndexed
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Edit
import androidx.compose.material.icons.filled.Search
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.drawBehind
import androidx.compose.ui.draw.shadow
import androidx.compose.ui.geometry.Offset
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.bumptech.glide.integration.compose.ExperimentalGlideComposeApi
import com.bumptech.glide.integration.compose.GlideImage
import com.github.gunin_igor75.weatherapp.R
import com.github.gunin_igor75.weatherapp.domain.entity.City
import com.github.gunin_igor75.weatherapp.presentation.extensions.convertTemp
import com.github.gunin_igor75.weatherapp.presentation.ui.theme.CardGradients
import com.github.gunin_igor75.weatherapp.presentation.ui.theme.Gradient
import com.github.gunin_igor75.weatherapp.presentation.ui.theme.Orange


@Composable
fun FavoriteContent(
    component: FavoriteComponent
) {
    val state by component.model.collectAsState()

    CitiesCards(
        cityItems = state.cityItems,
        onClickAddFavorite = { component.onClickSearch() },
        onClockSearch = { component.onClickSearch() },
        onClickCityItem = { component.onClickCityItemClick(it) }
    )
}

@Composable
private fun CitiesCards(
    cityItems: List<FavoriteStore.State.CityItem>,
    onClickAddFavorite: () -> Unit,
    onClockSearch: () -> Unit,
    onClickCityItem: (City) -> Unit
) {
    LazyVerticalGrid(
        modifier = Modifier.fillMaxSize(),
        columns = GridCells.Fixed(2),
        contentPadding = PaddingValues(16.dp),
        horizontalArrangement = Arrangement.spacedBy(16.dp),
        verticalArrangement = Arrangement.spacedBy(24.dp)
    ) {
        item(span = { GridItemSpan(2) }) {
            SearchCard(onClick = onClockSearch)
        }
        itemsIndexed(
            items = cityItems,
            key = { _, item -> item.city.id }
        ) { index, item ->
            CityCard(
                cityItem = item,
                index = index,
                onClick = { onClickCityItem(item.city) }
            )
        }
        item {
            AddCityCard(onClick = onClickAddFavorite)
        }
    }
}

@Composable
private fun SearchCard(
    onClick: () -> Unit
) {
    val gradient = CardGradients.gradients[3]
    Card(
        shape = CircleShape
    ) {
        Row(
            modifier = Modifier
                .fillMaxWidth()
                .background(gradient.primaryGradient)
                .clickable { onClick() },
            verticalAlignment = Alignment.CenterVertically
        ) {
            Icon(
                modifier = Modifier.padding(vertical = 8.dp, horizontal = 16.dp),
                imageVector = Icons.Filled.Search,
                contentDescription = stringResource(id = R.string.description_icon_search),
                tint = MaterialTheme.colorScheme.background
            )
            Text(
                text = stringResource(id = R.string.search),
                color = MaterialTheme.colorScheme.background
            )
        }
    }
}


@Preview(showBackground = true)
@Composable
private fun CitiesCardsPreview() {
    CitiesCards(
        cityItems = listCityItem,
        onClickAddFavorite = {},
        onClockSearch = {},
        onClickCityItem = {}
    )
}

@Composable
private fun CityCard(
    cityItem: FavoriteStore.State.CityItem,
    index: Int,
    onClick: () -> Unit
) {
    val gradient = getGradientByIndex(index)
    Card(
        modifier = Modifier
            .fillMaxSize()
            .shadow(
                elevation = 16.dp,
                spotColor = gradient.shadowColor
            ),
        shape = MaterialTheme.shapes.extraLarge
    ) {
        Box(
            modifier = Modifier
                .background(gradient.primaryGradient)
                .fillMaxSize()
                .sizeIn(minHeight = 196.dp)
                .drawBehind {
                    drawCircle(
                        brush = gradient.secondaryGradient,
                        center = Offset(
                            x = center.x - size.width / 10,
                            y = center.y + size.height / 2
                        ),
                        radius = size.maxDimension / 2
                    )
                }
                .clickable { onClick() }
                .padding(24.dp),
        ) {
            Text(
                modifier = Modifier.align(Alignment.BottomStart),
                text = cityItem.city.name,
                style = MaterialTheme.typography.titleMedium,
                color = MaterialTheme.colorScheme.background
            )
            when (val state = cityItem.weatherState) {
                FavoriteStore.State.WeatherState.Error -> {

                }

                FavoriteStore.State.WeatherState.Initial -> {

                }

                is FavoriteStore.State.WeatherState.Loaded -> {
                    ContentLoaded(state = state)
                }

                FavoriteStore.State.WeatherState.Loading -> {
                    ContentLoading()
                }
            }
        }
    }
}

@OptIn(ExperimentalGlideComposeApi::class)
@Composable
fun BoxScope.ContentLoaded(state: FavoriteStore.State.WeatherState.Loaded) {
    Text(
        modifier = Modifier
            .align(Alignment.BottomStart)
            .padding(bottom = 24.dp),
        text = state.tempC.convertTemp(),
        maxLines = 1,
        style = MaterialTheme.typography.headlineLarge.copy(
            fontSize = 48.sp
        ),
        color = MaterialTheme.colorScheme.background
    )
    GlideImage(
        modifier = Modifier
            .align(Alignment.TopEnd)
            .size(56.dp),
        model = state.iconUrl,
        contentDescription = stringResource(id = R.string.description_icon_weather)
    )
}

@Composable
fun ContentLoading() {
    Box(
        modifier = Modifier.fillMaxSize()
    ) {
        CircularProgressIndicator(
            color = MaterialTheme.colorScheme.background
        )
    }
}

@Preview(showBackground = true)
@Composable
private fun CityCardView() {

    CityCard(
        cityItem = FavoriteStore.State.CityItem(
            city = City.DEFAULT,
            weatherState = FavoriteStore.State.WeatherState.Initial
        ),
        index = 0,
        onClick = {}
    )
}

@Composable
private fun AddCityCard(
    onClick: () -> Unit
) {
    Card(
        colors = CardDefaults.cardColors(
            containerColor = Color.Transparent
        ),
        shape = MaterialTheme.shapes.extraLarge,
        border = BorderStroke(
            width = 1.dp,
            color = MaterialTheme.colorScheme.onBackground
        )
    ) {
        Column(
            modifier = Modifier
                .fillMaxWidth()
                .sizeIn(minHeight = 196.dp)
                .padding(24.dp)
                .clickable { onClick() }
        ) {
            Icon(
                modifier = Modifier
                    .size(48.dp)
                    .align(Alignment.CenterHorizontally),
                imageVector = Icons.Filled.Edit,
                contentDescription = stringResource(R.string.icon_edit),
                tint = Orange
            )
            Spacer(modifier = Modifier.weight(1f))
            Text(
                modifier = Modifier.align(Alignment.CenterHorizontally),
                text = stringResource(R.string.add_favorite),
                style = MaterialTheme.typography.titleMedium
            )
        }

    }
}

private fun getGradientByIndex(index: Int): Gradient {
    val gradients = CardGradients.gradients
    return gradients[index % gradients.size]
}

val listCityItem = listOf(
    FavoriteStore.State.CityItem(
        city = City(
            id = 0,
            name = "Москва",
            country = "Россия"
        ),
        weatherState = FavoriteStore.State.WeatherState.Loaded(
            tempC = 10.3F,
            iconUrl = "https://cdn.weatherapi.com/weather/128x128/day/113.png"
        )
    ),
    FavoriteStore.State.CityItem(
        city = City(
            id = 1,
            name = "Омск",
            country = "Россия"
        ),
        weatherState = FavoriteStore.State.WeatherState.Loaded(
            tempC = 10.3F,
            iconUrl = "https://cdn.weatherapi.com/weather/128x128/night/116.png"
        )
    ),
    FavoriteStore.State.CityItem(
        city = City(
            id = 2,
            name = "Берлин",
            country = "Германия"
        ),
        weatherState = FavoriteStore.State.WeatherState.Loaded(
            tempC = 25.3F,
            iconUrl = "https://cdn.weatherapi.com/weather/128x128/night/113.png"
        )
    ),
    FavoriteStore.State.CityItem(
        city = City(
            id = 3,
            name = "Минск",
            country = "Белоруссия"
        ),
        weatherState = FavoriteStore.State.WeatherState.Loaded(
            tempC = 10.3F,
            iconUrl = "https://cdn.weatherapi.com/weather/128x128/day/113.png"
        )
    ),
    FavoriteStore.State.CityItem(
        city = City(
            id = 4,
            name = "Милан",
            country = "Италия"
        ),
        weatherState = FavoriteStore.State.WeatherState.Loaded(
            tempC = -14.3F,
            iconUrl = "https://cdn.weatherapi.com/weather/128x128/night/116.png"
        )
    )
)