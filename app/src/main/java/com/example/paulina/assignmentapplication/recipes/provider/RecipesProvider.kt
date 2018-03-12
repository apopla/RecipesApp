package com.example.paulina.assignmentapplication.recipes.provider

import com.example.paulina.assignmentapplication.network.NetworkService
import com.example.paulina.assignmentapplication.realm.RecipesRealmProvider
import com.example.paulina.assignmentapplication.recipes.contract.RecipeContract
import com.example.paulina.assignmentapplication.recipes.model.Recipe
import com.example.paulina.assignmentapplication.recipes.realm_model.RealmRecipe
import com.example.paulina.assignmentapplication.recipes.realm_model.RealmRecipes
import io.reactivex.Completable
import io.reactivex.Observable
import io.reactivex.Single
import javax.inject.Inject

/**
 * Created by Paulina on 2018-03-08.
 */
class RecipesProvider @Inject constructor(var networkService: NetworkService, var realmProvider: RecipesRealmProvider) : RecipeContract.Provider {
    override fun saveRecipesToDb(recipes: List<Recipe>): Completable {
       return realmProvider.saveRecipesToRealmRecipes(recipes)
    }

    override fun getRecipes(): Single<List<Recipe>> = networkService.getRecipes(0, 50, "", "thumbnail-medium")

    override fun getRecepiesFromDb(): Single<List<RealmRecipe>> = realmProvider.getRecipesFromDb()

    override fun getRecipesByFrazeFromDb(fraze: String): Observable<RealmRecipes> = realmProvider.getRecipesFromDbByFraze(fraze)

    override fun checkIfDbIsEmpty(): Boolean {
       return realmProvider.checkIfDbIsEmpty()
    }

}