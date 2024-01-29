package com.github.gunin_igor75.weatherapp.presentation

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import com.arkivanov.decompose.defaultComponentContext
import com.github.gunin_igor75.weatherapp.WeatherApp
import com.github.gunin_igor75.weatherapp.presentation.root.DefaultRootComponent
import com.github.gunin_igor75.weatherapp.presentation.root.RootContent
import com.github.gunin_igor75.weatherapp.presentation.ui.theme.WeatherAppTheme
import javax.inject.Inject

class MainActivity : ComponentActivity() {

    @Inject
    lateinit var defaultRootComponentFactory: DefaultRootComponent.Factory

    override fun onCreate(savedInstanceState: Bundle?) {
        (applicationContext as WeatherApp).component.inject(this)
        super.onCreate(savedInstanceState)
        setContent {
            WeatherAppTheme {
                RootContent(
                    rootComponent = defaultRootComponentFactory.create(
                        defaultComponentContext()
                    )
                )
            }
        }
    }
}

