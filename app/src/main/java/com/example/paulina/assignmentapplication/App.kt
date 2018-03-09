package com.example.paulina.assignmentapplication

import android.app.Application

/**
 * Created by Paulina on 2018-03-08.
 */
class App : Application() {

    override fun onCreate() {
        super.onCreate()

        MainModule.attachApplication(this)


    }

}