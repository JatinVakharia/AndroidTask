package com.practice.jetpack.androidtask

import android.app.Application
import com.practice.jetpack.androidtask.di.apiModule
import com.practice.jetpack.androidtask.di.repoModule
import com.practice.jetpack.androidtask.di.uiModule
import org.koin.android.ext.android.startKoin


class MyApplication : Application() {

    override fun onCreate() {
        super.onCreate()

        startKoin(this, listOf(uiModule, repoModule, apiModule))
    }
}