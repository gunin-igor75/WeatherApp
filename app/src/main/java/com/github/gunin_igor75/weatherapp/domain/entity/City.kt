package com.github.gunin_igor75.weatherapp.domain.entity

import android.os.Parcelable
import kotlinx.parcelize.Parcelize

@Parcelize
data class City(
    val id: Int,
    val name: String,
    val country: String
): Parcelable{

    companion object{
        val DEFAULT:City = City(
            id = 0,
            name = "Москва",
            country = "Россия"
        )
    }
}
