package com.vimal.teachers.models

import java.io.Serializable

class ModelTeacher(

    val id: String?,
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
