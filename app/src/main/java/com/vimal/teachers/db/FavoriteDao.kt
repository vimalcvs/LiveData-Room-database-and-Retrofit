package com.vimal.teachers.db

import androidx.lifecycle.LiveData
import androidx.room.Dao
import androidx.room.Delete
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query
import com.vimal.teachers.models.ModelFavorite


@Dao
interface FavoriteDao {

    @Query("Select * from favorite_tables")
    fun getAllProducts(): LiveData<List<ModelFavorite>>

    @Insert(onConflict = OnConflictStrategy.IGNORE)
    fun insertProductToFavorite(modelWallpaper: ModelFavorite)

    @Delete
    fun deleteProductFromFavorite(modelWallpaper: ModelFavorite)

    @Query("SELECT EXISTS (SELECT 1 FROM favorite_tables WHERE id = :id)")
    fun isFavorite(id: Int): Boolean
}