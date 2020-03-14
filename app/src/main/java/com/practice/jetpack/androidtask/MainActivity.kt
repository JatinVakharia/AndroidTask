package com.practice.jetpack.androidtask

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import com.practice.jetpack.androidtask.ui.main.MainFragment

class MainActivity : AppCompatActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.main_activity)
    }
}
