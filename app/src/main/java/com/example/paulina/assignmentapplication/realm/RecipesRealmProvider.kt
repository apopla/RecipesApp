package com.example.paulina.assignmentapplication.realm

import android.util.Log
import com.example.paulina.assignmentapplication.recipes.model.Element
import com.example.paulina.assignmentapplication.recipes.model.ImageInfo
import com.example.paulina.assignmentapplication.recipes.model.Ingredient
import com.example.paulina.assignmentapplication.recipes.model.Recipe
import com.example.paulina.assignmentapplication.recipes.realm_model.*
import io.reactivex.Completable
import io.reactivex.Observable
import io.reactivex.Single
import io.reactivex.schedulers.Schedulers
import io.realm.Case
import io.realm.Realm
import io.realm.RealmList
import javax.inject.Inject


/**
 * Created by Paulina on 2018-03-09.
 */
class RecipesRealmProvider @Inject constructor() {

    var realm = Realm.getDefaultInstance()

    fun getRecipesFromRealmRecipes(realmRecipes: List<RealmRecipe>): List<Recipe> {
        Log.d ("RecipeRepository", "Thread get converted users " + Thread.currentThread().name)
            val recipeList = mutableListOf<Recipe>()
            for (realmRecipe in realmRecipes) {
                val ingredients = mutableListOf<Ingredient>()
                val images = mutableListOf<ImageInfo>()
                for (realmIngredient in realmRecipe.ingredients) {
                    val elements = mutableListOf<Element>()
                    realmIngredient.elements!!.mapTo(elements) { Element(it.id, it.name) }
                    realmRecipe.ingredients.mapTo(ingredients) { Ingredient(it.id, it.name, elements) }
                }
                for (realmImageInfo in realmRecipe.images) {
                    realmRecipe.images.mapTo(images) { ImageInfo(it.imboId, it.url) }
                }
                recipeList.add(Recipe(realmRecipe.title, realmRecipe.description, realmRecipe.id, images, ingredients))
            }
        Log.d("Repository", "Getting ${recipeList.size} users from DB...")
           return recipeList
    }


    fun getRecipesFromDb(): List<Recipe> {
        Log.d(RecipesRealmProvider::class.java.simpleName, "Thread get recipes from db: " + Thread.currentThread().name )
        realm = Realm.getDefaultInstance()
        val results = realm.where(RealmRecipe::class.java).findAll()
        Log.d("Repository", "Getting ${results.size} users from DB...")
        return getRecipesFromRealmRecipes(results)
    }

    fun checkIfDbIsEmpty(): Boolean {
        realm = Realm.getDefaultInstance()
        val results = realm.where(RealmRecipe::class.java).findAll()
        return results == null || results.isEmpty()
    }

    fun getRecipesFromDbByFraze(fraze: String): List<RealmRecipe> {
        realm = Realm.getDefaultInstance()
        val resultsByTitle = realm.where(RealmRecipe::class.java).contains("title", fraze, Case.INSENSITIVE).findAll()
        val resultsByIngredient = realm.where(RealmRecipe::class.java).contains("ingredients.name", fraze, Case.INSENSITIVE).findAll()
        var combined = ArrayList<RealmRecipe>()
        combined.addAll(resultsByIngredient.toList())
        combined.addAll(resultsByTitle.toList())
        return combined
    }

    fun saveRecipesToRealmRecipes(recipes: List<Recipe>) {
        Log.d(RecipesRealmProvider::class.java.simpleName, "Thread saving: " + Thread.currentThread().name )
        realm = Realm.getDefaultInstance()
        realm.beginTransaction()
        realm.delete(RealmRecipe::class.java)
        var realmList = RealmList<RealmRecipe>()
        for (recipe in recipes) {
            var realmRecipe = RealmRecipe()
            realmRecipe.id = recipe.id
            realmRecipe.title = recipe.title
            realmRecipe.description = recipe.description
            realmRecipe.ingredients = convertIngredientsToRealmIngredients(recipe.ingredients)
            realmRecipe.images = convertImageInfosToRealmImagesInfo(recipe.images)
            realmList.add(realmRecipe)
        }
        Log.d("Repository", "Saving ${realmList.size} users from DB...")
        realm.copyToRealm(realmList)
        realm.commitTransaction()
        realm.close()
    }

    private fun convertIngredientsToRealmIngredients(ingredients: List<Ingredient>): RealmList<RealmIngredient> {
        var realmList = RealmList<RealmIngredient>()
        for (ingredient in ingredients) {
            var realmIngredient = RealmIngredient()
            realmIngredient.id = ingredient.id
            realmIngredient.name = ingredient.name
            realmIngredient.elements = convertElementsToRealmElements(ingredient.elements)
            realmList.add(realmIngredient)
        }
        realm.copyToRealm(realmList)
        return realmList
    }

    private fun convertElementsToRealmElements(elements: List<Element>): RealmList<RealmElement> {
        var realmList = RealmList<RealmElement>()
        for (element in elements) {
            var realmElement = RealmElement()
            realmElement.id = element.id
            realmElement.name = element.name
            realmList.add(realmElement)
        }
        realm.copyToRealm(realmList)
        return realmList
    }

    private fun convertImageInfosToRealmImagesInfo(images: List<ImageInfo>): RealmList<RealmImageInfo> {
        var realmList = RealmList<RealmImageInfo>()
        for (image in images) {
            var realmImageInfo = RealmImageInfo()
            realmImageInfo.imboId = image.imboId
            realmImageInfo.url = image.url
            realmList.add(realmImageInfo)
        }
        realm.copyToRealm(realmList)
        return realmList
    }

}