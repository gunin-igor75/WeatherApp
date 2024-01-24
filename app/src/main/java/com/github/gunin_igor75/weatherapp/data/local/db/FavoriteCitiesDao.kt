package com.github.gunin_igor75.weatherapp.data.local.db

import androidx.room.Dao
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query
import com.github.gunin_igor75.weatherapp.data.local.model.CityDbModel
import kotlinx.coroutines.flow.Flow

@Dao
interface FavoriteCitiesDao {

    @Query("SELECT * FROM favorite_cities")
    fun getFavoriteCities(): Flow<List<CityDbModel>>

    @Query("SELECT EXISTS(SELECT * FROM favorite_cities WHERE id = :cityId LIMIT 1)")
    fun observeIsFavorite(cityId: Int): Flow<Boolean>

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun addToFavorite(cityDbModel: CityDbModel)

    @Query("DELETE FROM favorite_cities WHERE id = :cityId")
    suspend fun removeFromFavorite(cityId: Int)
}