package com.github.gunin_igor75.weatherapp.data.repository

import com.github.gunin_igor75.weatherapp.data.local.db.FavoriteCitiesDao
import com.github.gunin_igor75.weatherapp.data.mapper.toCities
import com.github.gunin_igor75.weatherapp.data.mapper.toDbModel
import com.github.gunin_igor75.weatherapp.domain.entity.City
import com.github.gunin_igor75.weatherapp.domain.repositoriy.FavoriteRepository
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.map
import javax.inject.Inject

class FavoriteRepositoryImpl @Inject constructor(
    private val favoriteCitiesDao: FavoriteCitiesDao
) : FavoriteRepository {

    override val favoriteCities: Flow<List<City>> = favoriteCitiesDao.getFavoriteCities()
        .map {it.toCities() }

    override fun observeIsFavorite(cityId: Int): Flow<Boolean> = favoriteCitiesDao.observeIsFavorite(cityId)

    override suspend fun addToFavorite(city: City) {
        favoriteCitiesDao.addToFavorite(city.toDbModel())
    }

    override suspend fun removeFromFavorite(cityId: Int) {
        favoriteCitiesDao.removeFromFavorite(cityId)
    }
}