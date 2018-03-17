package com.example.paulina.assignmentapplication.recipes.repository

import android.util.Log
import com.example.paulina.assignmentapplication.network.NetworkService
import com.example.paulina.assignmentapplication.realm.RecipesRealmProvider
import com.example.paulina.assignmentapplication.recipes.model.Recipe
import io.reactivex.Maybe
import io.reactivex.Observable
import io.reactivex.schedulers.Schedulers
import javax.inject.Inject

/**
 * Created by Paulina on 2018-03-17.
 */
class RecipeRepository @Inject constructor(var networkService: NetworkService, var realmProvider: RecipesRealmProvider) {

    fun getRecipse(): Maybe<List<Recipe>> {
        return Observable.concat(
                getUsersFromDb(),
                getUsersFromApi()
                ).firstElement()
    }


    fun getUsersFromDb(): Observable<List<Recipe>> {
        if(realmProvider.getRecipesFromDb().size == 0){
            return Observable.empty()
        }
        return Observable.fromArray(realmProvider.getRecipesFromDb())
    }

    fun getUsersFromApi(): Observable<List<Recipe>> {
        Log.d ("RecipeRepository", "Thread get users from api: " + Thread.currentThread().name)
        return networkService.getRecipes(0, 50, "", "thumbnail-medium")
                .doOnNext {
                    storeUsersInDb(it)
                }
            //    .onErrorResumeNext(Observable.empty())
            //    .onErrorReturn { e -> emptyList() }
    }

    fun storeUsersInDb(recipes: List<Recipe>) {
        Log.d ("RecipeRepository", "Thread store in db: " + Thread.currentThread().name)
        Observable.fromCallable { realmProvider.saveRecipesToRealmRecipes(recipes) }
                .subscribe()
    }

}