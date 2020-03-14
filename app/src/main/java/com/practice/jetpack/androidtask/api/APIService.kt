package com.practice.jetpack.androidtask.api

import com.practice.jetpack.androidtask.api.request.LoginRequest
import com.practice.jetpack.androidtask.api.response.LoginResponse
import io.reactivex.Single
import retrofit2.Response
import retrofit2.http.Body
import retrofit2.http.Headers
import retrofit2.http.POST


interface APIService {

    @POST("login")
    @Headers("Content-type: application/json")
    fun login(@Body loginRequest: LoginRequest) : Single<Response<LoginResponse>>

}