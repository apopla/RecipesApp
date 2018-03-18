package com.example.paulina.assignmentapplication.recipes.contract

import com.example.paulina.assignmentapplication.recipes.model.Recipe
import com.example.paulina.assignmentapplication.recipes.realm_model.RealmRecipe
import com.example.paulina.assignmentapplication.recipes.realm_model.RealmRecipes
import io.reactivex.Completable
import io.reactivex.Observable
import io.reactivex.Single

/**
 * Created by Paulina on 2018-03-08.
 */
interface RecipeContract {

    interface View {
        fun setAdapterWithData(recipeList: List<Recipe>)
        fun showError(errorMessage: String)
        fun showLoader(show: Boolean)
    }

    interface Presenter {
        fun attachView(view: View)
        fun detachView()
        fun getRecipes()
        fun searchRecipes(fraze: String) : List<Recipe>

    }


}