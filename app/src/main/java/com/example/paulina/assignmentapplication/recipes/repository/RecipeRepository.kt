package com.example.paulina.assignmentapplication.recipes.repository

import android.util.Log
import com.example.paulina.assignmentapplication.network.NetworkService
import com.example.paulina.assignmentapplication.realm.RecipesRealmProvider
import com.example.paulina.assignmentapplication.recipes.model.Recipe
import com.example.paulina.assignmentapplication.recipes.realm_model.RealmRecipe
import io.reactivex.Maybe
import io.reactivex.Observable
import io.reactivex.schedulers.Schedulers
import javax.inject.Inject

/**
 * Created by Paulina on 2018-03-17.
 */
class RecipeRepository @Inject constructor(var networkService: NetworkService, var realmProvider: RecipesRealmProvider) {

    fun getRecipse(): Observable<List<Recipe>> {
        return Observable.concat(
                getUsersFromDb(),
                getUsersFromApi()
                ).firstElement()
                .toObservable()
    }


    fun getUsersFromDb(): Observable<List<Recipe>> {
        if(realmProvider.getRecipesFromDb().size == 0){
            return Observable.empty()
        }
        return Observable.fromArray(realmProvider.getRecipesFromDb())
    }

    fun getUsersFromApi(): Observable<List<Recipe>> {
        return networkService.getRecipes(0, 5, "", "thumbnail-medium")
                .doOnNext {
                    storeUsersInDb(it)
                }
            //    .onErrorResumeNext(Observable.empty())
            //    .onErrorReturn { e -> emptyList() }
    }

    fun storeUsersInDb(recipes: List<Recipe>) {
        Observable.fromCallable { realmProvider.saveRecipesToRealmRecipes(recipes) }
                .subscribe()
    }

    fun getRecipesFromDbByFraze(fraze: String): List<Recipe> {
        return realmProvider.getRecipesFromDbByFraze(fraze)
    }

}