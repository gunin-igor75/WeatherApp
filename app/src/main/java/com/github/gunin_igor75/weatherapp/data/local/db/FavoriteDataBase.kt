package com.github.gunin_igor75.weatherapp.data.local.db

import android.content.Context
import androidx.room.Database
import androidx.room.Room
import androidx.room.RoomDatabase
import com.github.gunin_igor75.weatherapp.data.local.model.CityDbModel

@Database(entities = [CityDbModel::class], version = 1, exportSchema = false)
abstract class FavoriteDataBase : RoomDatabase() {

    abstract fun favoriteCitiesDa(): FavoriteCitiesDao

    companion object {
        private const val DB_NAME = "FavoriteDataBase"
        private var INSTANCE: FavoriteDataBase? = null
        private val lock = Any()

        fun getInstance(context: Context): FavoriteDataBase {
            INSTANCE?.let { return it }

            synchronized(lock) {
                INSTANCE?.let { return it }
                val dataBase = Room.databaseBuilder(
                    context = context,
                    klass = FavoriteDataBase::class.java,
                    name = DB_NAME,
                ).build()

                INSTANCE = dataBase
                return dataBase
            }
        }
    }
}