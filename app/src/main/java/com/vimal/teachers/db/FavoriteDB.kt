package com.vimal.teachers.db;



import android.content.Context
import androidx.room.Database
import androidx.room.Room
import androidx.room.RoomDatabase
import com.vimal.teachers.models.ModelFavorite

@Database(entities = [ModelFavorite::class], version = 1)
abstract class FavoriteDB : RoomDatabase() {

    abstract fun getFavoriteDao(): FavoriteDao

    companion object {
        @Volatile
        private var instance: FavoriteDB? = null

        fun getDatabase(context: Context): FavoriteDB {
            return instance ?: synchronized(this) {
                val newInstance = Room.databaseBuilder(
                    context.applicationContext,
                    FavoriteDB::class.java,
                    "database.db"
                ).build()
                instance = newInstance
                newInstance
            }
        }
    }
}

