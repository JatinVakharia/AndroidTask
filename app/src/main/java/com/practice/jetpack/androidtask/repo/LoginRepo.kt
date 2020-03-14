package com.practice.jetpack.androidtask.repo

import com.practice.jetpack.androidtask.api.APIService
import com.practice.jetpack.androidtask.api.request.LoginRequest
import com.practice.jetpack.androidtask.api.response.LoginResponse
import io.reactivex.Observable
import okhttp3.MediaType
import okhttp3.ResponseBody
import retrofit2.Response
import java.lang.Error


interface LoginRepo{
    fun loginSuccess(loginrequest : LoginRequest) : Observable<Response<LoginResponse>>
    fun loginFailure400(loginrequest : LoginRequest) : Observable<Response<LoginResponse>>
    fun loginFailure401(loginrequest : LoginRequest) : Observable<Response<LoginResponse>>
}

class LoginRepoImpl(val apiService: APIService) : LoginRepo {

    override fun loginSuccess(loginrequest: LoginRequest): Observable<Response<LoginResponse>> {
        val loginResponse  = LoginResponse()
        loginResponse.token = "ASDASDAS#$%@##$%#$"
        return Observable.just(Response.success(loginResponse))
    }

    // TODO : Not ready to use
    override fun loginFailure400(loginrequest: LoginRequest): Observable<Response<LoginResponse>> {
        val response : String = "{\"error\":\"invalid_credentials\"," +
                "\"message\": \"Email address and password is not a valid combination.\"}"
        return Observable.just(Response.error(400, ResponseBody.create(MediaType.parse(String()), response)))
    }

    // TODO : Not ready to use
    override fun loginFailure401(loginrequest: LoginRequest): Observable<Response<LoginResponse>> {
        val response : String = "{\"error\":\"bad_request\"," +
                "\"description\": \"Network Communication Error\"}"
        return Observable.error(Throwable(response))
//        return Observable.just(Response.error(401, ResponseBody.create(MediaType.parse(String()), response)))
    }
}