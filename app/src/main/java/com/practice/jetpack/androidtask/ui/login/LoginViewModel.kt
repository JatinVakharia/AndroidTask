package com.practice.jetpack.androidtask.ui.login

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import com.practice.jetpack.androidtask.AbstractViewModel
import com.practice.jetpack.androidtask.api.request.LoginRequest
import com.practice.jetpack.androidtask.repo.LoginRepo
import io.reactivex.android.schedulers.AndroidSchedulers
import io.reactivex.schedulers.Schedulers

class LoginViewModel(val loginRepo: LoginRepo) : AbstractViewModel() {
    val validUsername = "test@worldofplay.in"
    val validPassword = "Worldofplay@2020"
    val loginLiveData = MutableLiveData<Int>()

    fun validCredentials(username : String, password: String) : Boolean{
        return username.equals(validUsername) && password.equals(validPassword)
    }

    fun login(username : String, password: String) : LiveData<Int> {
        if(validCredentials(username, password)){
            val loginrequest = LoginRequest()
            loginrequest.password = password
            loginrequest.username = username
            loginRepo.loginSuccess(loginrequest)
                .observeOn(AndroidSchedulers.mainThread())
                .subscribeOn(Schedulers.io())
                .doOnError {
                    loginLiveData.value = 400
                }
                .subscribe {
                    loginLiveData.value = it.code()
                }
        }else
            loginLiveData.value = 401

        return loginLiveData
    }
}
