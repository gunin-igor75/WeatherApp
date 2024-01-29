package com.github.gunin_igor75.weatherapp.presentation.details

import androidx.compose.animation.AnimatedVisibility
import androidx.compose.animation.core.MutableTransitionState
import androidx.compose.animation.core.tween
import androidx.compose.animation.fadeIn
import androidx.compose.animation.slideIn
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.RowScope
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.ArrowBack
import androidx.compose.material.icons.filled.Star
import androidx.compose.material.icons.filled.StarBorder
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.CenterAlignedTopAppBar
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.material3.TopAppBarDefaults
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.IntOffset
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.bumptech.glide.integration.compose.ExperimentalGlideComposeApi
import com.bumptech.glide.integration.compose.GlideImage
import com.github.gunin_igor75.weatherapp.R
import com.github.gunin_igor75.weatherapp.domain.entity.Forecast
import com.github.gunin_igor75.weatherapp.domain.entity.Weather
import com.github.gunin_igor75.weatherapp.presentation.extensions.convertDateFullToString
import com.github.gunin_igor75.weatherapp.presentation.extensions.convertDateToDayWeek
import com.github.gunin_igor75.weatherapp.presentation.extensions.convertTemp
import com.github.gunin_igor75.weatherapp.presentation.ui.theme.CardGradients

@Composable
fun DetailsContent(
    detailsComponent: DetailsComponent
) {
    val state by detailsComponent.model.collectAsState()
    val color = MaterialTheme.colorScheme.background
    val gradient = CardGradients.gradients[1]

    Scaffold(
        containerColor = Color.Transparent,
        contentColor = color,
        topBar = {
            AppTopBar(
                cityName = state.city.name,
                isFavorite = state.isFavorite,
                onclickBack = {
                    detailsComponent.onClickBack()
                },
                onClickChangeFavoriteStatus = {
                    detailsComponent.onClickChangeFavoriteStatus()
                }
            )
        },
        modifier = Modifier
            .fillMaxSize()
            .background(gradient.primaryGradient)
    ) { padding ->
        Box(modifier = Modifier.padding(padding)) {
            when (val currentState = state.forecastState) {
                DetailsStore.State.ForecastState.Error -> {

                }

                DetailsStore.State.ForecastState.Initial -> {

                }

                is DetailsStore.State.ForecastState.Loaded -> {
                    ForecastWeather(forecast = currentState.forecast)
                }

                DetailsStore.State.ForecastState.Loading -> {
                    Loading()
                }
            }
        }
    }
}

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun AppTopBar(
    cityName: String,
    isFavorite: Boolean,
    onclickBack: () -> Unit,
    onClickChangeFavoriteStatus: () -> Unit
) {
    val color = MaterialTheme.colorScheme.background

    CenterAlignedTopAppBar(
        colors = TopAppBarDefaults.topAppBarColors(
            containerColor = Color.Transparent,
            titleContentColor = color
        ),
        title = {
            Text(
                text = cityName,
                style = MaterialTheme.typography.titleMedium,
            )
        },
        navigationIcon = {
            IconButton(onClick = { onclickBack() }) {
                Icon(
                    imageVector = Icons.Filled.ArrowBack,
                    contentDescription = stringResource(R.string.description_icon_arrowback),
                    tint = color
                )
            }
        },
        actions = {
            val icon = if (isFavorite) {
                Icons.Filled.Star
            } else {
                Icons.Filled.StarBorder
            }
            IconButton(onClick = { onClickChangeFavoriteStatus() }) {
                Icon(
                    imageVector = icon,
                    contentDescription = stringResource(R.string.descroption_icon_favorite),
                    tint = color
                )
            }
        }
    )
}

@Composable
fun Loading() {
    Box(
        modifier = Modifier.fillMaxSize()
    ) {
        CircularProgressIndicator(
            color = MaterialTheme.colorScheme.background
        )
    }
}

@Composable
fun ForecastWeather(
    forecast: Forecast
) {
    val currentWeather = forecast.currentWeather
    Column(
        modifier = Modifier.fillMaxWidth(),
        horizontalAlignment = Alignment.CenterHorizontally
    ) {
        Spacer(modifier = Modifier.weight(1f))
        CurrentWeather(weather = currentWeather)
        Spacer(modifier = Modifier.weight(1f))
        AnimatedForecasts(weathers = forecast.upcoming)
        Spacer(modifier = Modifier.weight(0.5f))
    }
}


@Composable
fun AnimatedForecasts(
    weathers: List<Weather>
) {
    val state = remember {
        MutableTransitionState(false).apply {
            targetState = true
        }
    }
    AnimatedVisibility(
        visibleState = state,
        enter = fadeIn(animationSpec = tween(500)) + slideIn(
            animationSpec = tween(500),
            initialOffset = { IntOffset(0, it.height)}
        )
    ) {
        Forecasts(weathers = weathers)
    }
}
@OptIn(ExperimentalGlideComposeApi::class)
@Composable
fun CurrentWeather(
    weather: Weather
) {
    Text(
        text = weather.conditionText,
        style = MaterialTheme.typography.titleLarge
    )
    Row(
        verticalAlignment = Alignment.CenterVertically
    ) {
        Text(
            text = weather.tempC.convertTemp(),
            style = MaterialTheme.typography.headlineLarge.copy(fontSize = 70.sp)
        )
        GlideImage(
            model = weather.conditionUrl,
            contentDescription = stringResource(R.string.description_icon_weather),
            modifier = Modifier.size(70.dp)
        )
    }
    Text(
        text = weather.date.convertDateFullToString(),
        style = MaterialTheme.typography.titleLarge
    )
}

@Composable
fun Forecasts(
    weathers: List<Weather>
) {
    Card(
        modifier = Modifier.padding(24.dp),
        colors =CardDefaults.cardColors(
            containerColor = MaterialTheme.colorScheme.background.copy(
                alpha = 0.25f
            )
        )
    ) {
        Column(
            modifier = Modifier
                .fillMaxWidth()
                .padding(24.dp),
            horizontalAlignment = Alignment.CenterHorizontally
        ) {
            Text(
                modifier = Modifier.padding(bottom = 16.dp),
                text = stringResource(R.string.forecast_3_day),
                style = MaterialTheme.typography.titleMedium
            )
            Row(
                horizontalArrangement = Arrangement.spacedBy(16.dp)
            ) {
                weathers.forEach {
                    ForecastDay(weather = it)
                }
            }
        }

    }
}

@OptIn(ExperimentalGlideComposeApi::class)
@Composable
fun RowScope.ForecastDay(
    weather: Weather
) {
    Card(
        modifier = Modifier
            .height(128.dp)
            .weight(1f),
        colors = CardDefaults.cardColors(
            containerColor = MaterialTheme.colorScheme.background
        )
    ) {
        Column(
            modifier = Modifier
                .fillMaxSize()
                .padding(8.dp),
            horizontalAlignment = Alignment.CenterHorizontally,
            verticalArrangement = Arrangement.SpaceBetween
        ) {
            Text(text = weather.tempC.convertTemp())
            GlideImage(
                model = weather.conditionUrl,
                contentDescription = stringResource(id = R.string.description_icon_weather)
            )
            Text(text = weather.date.convertDateToDayWeek())
        }
    }
}
@Preview(showBackground = true)
@Composable
fun AppTopBarPreview() {
    val gradient = CardGradients.gradients[1]
    Scaffold(
        containerColor = Color.Transparent,
        contentColor = MaterialTheme.colorScheme.background,
        topBar = {
            AppTopBar(
                cityName = "Москва",
                isFavorite = true,
                onclickBack = { },
                onClickChangeFavoriteStatus = {}
            )
        },
        modifier = Modifier
            .fillMaxSize()
            .background(gradient.primaryGradient)
    ) { padding ->
        Text(
            text = "",
            modifier = Modifier.padding(padding)
        )
    }
}
