package com.example.paulina.assignmentapplication

import android.app.Application
import android.content.Context
import com.example.paulina.assignmentapplication.di.DaggerMainComponent
import com.example.paulina.assignmentapplication.di.MainComponent
import io.realm.Realm
import io.realm.RealmConfiguration

/**
 * Created by Paulina on 2018-03-08.
 */
object MainModule {

    lateinit var mainComponent: MainComponent

    fun attachApplication(app: Application) {
        createComponent(app)
    }


    private fun createComponent(app: Application) {
        val realmConfig = getRealmConfig(app)
        mainComponent = DaggerMainComponent.builder()
                .realmConfig(realmConfig)
                .application(app)
                .build()
    }

    private fun getRealmConfig(context: Context): RealmConfiguration {
        Realm.init(context)
        return  RealmConfiguration.Builder()
                .name("recipes.realm")
                .schemaVersion(0)
                .build()

    }

}