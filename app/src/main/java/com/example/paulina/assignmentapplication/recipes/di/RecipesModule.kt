package com.example.paulina.assignmentapplication.recipes.di

import android.content.Context
import com.example.paulina.assignmentapplication.recipes.contract.RecipeContract
import com.example.paulina.assignmentapplication.recipes.presenter.RecipePresenter
import com.example.paulina.assignmentapplication.recipes.repository.RecipeRepository
import com.example.paulina.assignmentapplication.recipes.view.MainActivity
import dagger.Module
import dagger.Provides

/**
 * Created by Paulina on 2018-03-08.
 */

@Module
class RecipesModule(private val activity: MainActivity) {

    @Provides
    fun provideContext(): Context = activity

    @Provides
    fun providePresenter(impl: RecipePresenter): RecipeContract.Presenter= impl

    @Provides
    fun provideActivity() = activity

}