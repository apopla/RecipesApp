package com.example.paulina.assignmentapplication.realm

import dagger.Module
import dagger.Provides
import io.realm.Realm
import javax.inject.Singleton

/**
 * Created by Paulina on 2018-03-12.
 */
@Module
class DbModule {

    @Provides
    @Singleton
    fun provideRecipesRealmProvider(): RecipesRealmProvider =
            RecipesRealmProvider()



}