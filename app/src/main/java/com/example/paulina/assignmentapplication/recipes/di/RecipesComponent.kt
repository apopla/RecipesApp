package com.example.paulina.assignmentapplication.recipes.di

import com.example.paulina.assignmentapplication.recipes.view.MainActivity
import dagger.Subcomponent

/**
 * Created by Paulina on 2018-03-08.
 */
@Subcomponent(modules = arrayOf(RecipesModule::class))
interface RecipesComponent {
    fun inject(recipesActivity: MainActivity)
}