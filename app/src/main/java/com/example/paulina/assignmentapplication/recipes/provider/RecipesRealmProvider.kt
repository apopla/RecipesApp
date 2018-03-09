package com.example.paulina.assignmentapplication.recipes.provider

import android.util.Log
import com.example.paulina.assignmentapplication.recipes.realm_model.RealmIngredient
import com.example.paulina.assignmentapplication.recipes.realm_model.RealmRecipe
import com.example.paulina.assignmentapplication.recipes.realm_model.RealmRecipes
import io.reactivex.Observable
import io.reactivex.Single
import io.realm.Case
import io.realm.Realm



/**
 * Created by Paulina on 2018-03-09.
 */
class RecipesRealmProvider{


    fun getRecipesFromDb (): Single<List<RealmRecipe>>{
        val realm = Realm.getDefaultInstance()
        val results = realm.where(RealmRecipe::class.java).findAll()
        return Single.just(results.toList())
    }

    fun checkIfDbIsEmpty(): Boolean{
        val realm = Realm.getDefaultInstance()
        val results = realm.where(RealmRecipe::class.java).findAll()
        return results==null || results.isEmpty()
    }

    fun getRecipesFromDbByFraze(fraze: String): Observable<RealmRecipes>{
        val realm = Realm.getDefaultInstance()
        val resultsByTitle = realm.where(RealmRecipe::class.java).contains("title", fraze, Case.INSENSITIVE).findAll()
        val resultsByIngredient = realm.where(RealmRecipe::class.java).contains("ingredients.name", fraze, Case.INSENSITIVE).findAll()
        var combined = ArrayList<RealmRecipe>()
        combined.addAll(resultsByIngredient.toList())
        combined.addAll(resultsByTitle.toList())
        var realmRecipes = RealmRecipes(combined)
        return Observable.just(realmRecipes)
    }
}