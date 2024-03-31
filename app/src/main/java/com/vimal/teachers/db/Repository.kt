package com.vimal.teachers.db

import android.content.Context
import androidx.lifecycle.LiveData
import com.vimal.teachers.models.ModelFavorite
import com.vimal.teachers.util.Utils
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.runBlocking
import kotlinx.coroutines.withContext

class Repository(context: Context?) {
    private val favoriteDao: FavoriteDao
    private val liveData: LiveData<List<ModelFavorite>>

    init {
        val database = FavoriteDB.getDatabase(context!!)
        favoriteDao = database.getFavoriteDao()
        liveData = favoriteDao.getAllFavorite()
    }

    fun allFavorite(): LiveData<List<ModelFavorite>> {
        return liveData
    }

    fun deleteFavorite(model: ModelFavorite?) {
        try {
            object : Thread(Runnable { favoriteDao.deleteFavorite(model!!) }) {
            }.start()
        } catch (e: Exception) {
            Utils.getErrors(e)
        }
    }

    fun insertFavorite(model: ModelFavorite?) {
        try {
            object : Thread(Runnable { favoriteDao.insertFavorite(model!!) }) {
            }.start()
        } catch (e: Exception) {
            Utils.getErrors(e)
        }
    }

    fun isFavorite(id: Int): Boolean {
        return runBlocking {
            withContext(Dispatchers.IO) {
                favoriteDao.isFavorite(id)
            }
        }
    }


    companion object {
        private var repository: Repository? = null
        fun getInstance(context: Context?): Repository? {
            if (repository == null) {
                repository = Repository(context)
            }
            return repository
        }
    }
}