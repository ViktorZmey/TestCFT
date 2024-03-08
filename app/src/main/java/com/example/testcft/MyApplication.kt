package com.example.testcft

import android.app.Application
import android.content.Context

class MyApplication : Application() {

    override fun onCreate() {
        super.onCreate()
        _applicationContext = this.applicationContext
    }

    companion object {
        private lateinit var _applicationContext: Context

        val userDataBase: UserDataBase by lazy { UserDataBase.getUserDB(_applicationContext) }
    }
}