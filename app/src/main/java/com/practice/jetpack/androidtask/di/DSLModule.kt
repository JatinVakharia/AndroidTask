package com.practice.jetpack.androidtask.di

import com.practice.jetpack.androidtask.api.APIService
import com.practice.jetpack.androidtask.ui.main.MainViewModel
import com.practice.jetpack.androidtask.repo.LoginRepo
import com.practice.jetpack.androidtask.repo.LoginRepoImpl
import com.practice.jetpack.androidtask.ui.login.LoginViewModel
import okhttp3.OkHttpClient
import okhttp3.logging.HttpLoggingInterceptor
import org.koin.android.viewmodel.ext.koin.viewModel
import org.koin.dsl.module.module
import retrofit2.Retrofit
import retrofit2.adapter.rxjava2.RxJava2CallAdapterFactory
import retrofit2.converter.gson.GsonConverterFactory
import java.util.concurrent.TimeUnit

private val SERVER_URL = "https://www.google.com"

val uiModule = module {
    viewModel { LoginViewModel(get()) }
    viewModel { MainViewModel() }
}

val repoModule = module {
    single<LoginRepo> { LoginRepoImpl(get()) }
}

val apiModule = module {
    //    provideWebComponents
    single { createOkHttpClient() }

    single { createWebService<APIService>(get(), SERVER_URL) }
}

fun createOkHttpClient(): OkHttpClient {
    val httpLoggingInterceptor = HttpLoggingInterceptor()
    httpLoggingInterceptor.level = HttpLoggingInterceptor.Level.BASIC
    return OkHttpClient.Builder()
            .connectTimeout(10L, TimeUnit.SECONDS)
            .readTimeout(10L, TimeUnit.SECONDS)
            .addInterceptor(httpLoggingInterceptor).build()
}

inline fun <reified T> createWebService(okHttpClient: OkHttpClient, url: String): T {
    val retrofit = Retrofit.Builder()
            .baseUrl(url)
            .client(okHttpClient)
            .addConverterFactory(GsonConverterFactory.create())
            .addCallAdapterFactory(RxJava2CallAdapterFactory.create()).build()
    return retrofit.create(T::class.java)
}
