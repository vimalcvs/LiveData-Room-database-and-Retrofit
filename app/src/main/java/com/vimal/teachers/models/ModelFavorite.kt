package com.vimal.teachers.models

import androidx.room.Entity
import androidx.room.PrimaryKey
import java.io.Serializable


@Entity(tableName = "favorite_tables")
class ModelFavorite(

    @field:PrimaryKey
    val id: Int,
    var name: String?,
    var language_name: String?,
    var age: String?,
    var profile_pic: String?,
    var gender: String?,
    var thumbnail: String?,
    var user_busy: String?,
    var per_minute_coins: String?,
    var coins: String?
) : Serializable
