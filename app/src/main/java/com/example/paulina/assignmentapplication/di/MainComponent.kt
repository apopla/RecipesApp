package com.example.paulina.assignmentapplication.di

import android.app.Application
import com.example.paulina.assignmentapplication.network.ConnectionModule
import com.example.paulina.assignmentapplication.recipes.di.RecipesComponent
import com.example.paulina.assignmentapplication.recipes.di.RecipesModule
import dagger.BindsInstance
import dagger.Component
import io.realm.RealmConfiguration
import javax.inject.Singleton

/**
 * Created by Paulina on 2018-03-08.
 */
@Singleton
@Component(modules = arrayOf(ConnectionModule::class))
interface MainComponent {

    fun plusRecipesListComponent(recipesListModule: RecipesModule): RecipesComponent

    @Component.Builder
    interface Builder {
        @BindsInstance
        fun application(application: Application): Builder
        @BindsInstance
        fun realmConfig(realmConfig: RealmConfiguration): Builder
        fun build():MainComponent
    }
}