package com.practice.jetpack.androidtask

import androidx.lifecycle.LifecycleOwner
import androidx.lifecycle.Observer
import com.practice.jetpack.androidtask.api.APIService
import com.practice.jetpack.androidtask.repo.LoginRepo
import com.practice.jetpack.androidtask.repo.LoginRepoImpl
import com.practice.jetpack.androidtask.ui.login.LoginViewModel
import okhttp3.OkHttpClient
import okhttp3.logging.HttpLoggingInterceptor
import org.junit.After
import org.junit.Test

import org.junit.Assert.*
import org.junit.Before
import org.koin.android.viewmodel.ext.koin.viewModel
import org.koin.dsl.module.Module
import org.koin.dsl.module.module
import org.koin.standalone.StandAloneContext
import org.koin.standalone.inject
import org.koin.test.KoinTest
import retrofit2.Retrofit
import retrofit2.adapter.rxjava2.RxJava2CallAdapterFactory
import retrofit2.converter.gson.GsonConverterFactory
import java.util.concurrent.TimeUnit

class LoginUnitTest : KoinTest {

    //Injecting viewmodel
    val loginViewModel : LoginViewModel by inject()
    val uiModule : Module = module {
        viewModel { LoginViewModel(get())}
    }

    val repoModule = module {
        single<LoginRepo> { LoginRepoImpl(get()) }
    }
    private val SERVER_URL = "https://www.google.com"
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


    @Before
    fun before(){
        StandAloneContext.startKoin(listOf(uiModule, repoModule, apiModule))
    }

    @After
    fun after() {
        StandAloneContext.stopKoin()
    }

    @Test
    fun emailNegativeTest() {
        assertNotEquals("myEmail@gmail.com", loginViewModel.validUsername)
    }

    @Test
    fun emailPositiveTest() {
        assertEquals("test@worldofplay.in", loginViewModel.validUsername)
    }

    @Test
    fun passwordNegativeTest() {
        assertNotEquals("Pass@123", loginViewModel.validPassword)
    }

    @Test
    fun passwordPositiveTest() {
        assertEquals("Worldofplay@2020", loginViewModel.validPassword)
    }

    @Test
    fun credentialNegativeTest() {
        assertNotEquals(true, loginViewModel.validCredentials("myEmail@gmail.com","Pass@123"))
    }

    @Test
    fun credentialPositiveTest() {
        assertEquals(true, loginViewModel.validCredentials("test@worldofplay.in","Worldofplay@2020"))
    }
}
