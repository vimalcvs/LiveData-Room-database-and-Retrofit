package com.vimal.teachers.rest

import com.vimal.teachers.callback.CallbackTeacher
import retrofit2.Call
import retrofit2.http.GET
import retrofit2.http.Headers

interface ApiInterface {

    @get:GET("app_api/dummy_users_list")
    @get:Headers(CACHE, AGENT)
    val getList: Call<CallbackTeacher?>?

    companion object {
        const val CACHE: String = "Cache-Control: max-age=640000"
        const val AGENT: String = "User-Agent: Police-Exam-App"
    }
}
