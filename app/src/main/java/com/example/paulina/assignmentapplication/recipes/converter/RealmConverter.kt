package com.example.paulina.assignmentapplication.recipes.converter

import com.example.paulina.assignmentapplication.recipes.model.Element
import com.example.paulina.assignmentapplication.recipes.model.ImageInfo
import com.example.paulina.assignmentapplication.recipes.model.Ingredient
import com.example.paulina.assignmentapplication.recipes.model.Recipe
import com.example.paulina.assignmentapplication.recipes.realm_model.RealmElement
import com.example.paulina.assignmentapplication.recipes.realm_model.RealmImageInfo
import com.example.paulina.assignmentapplication.recipes.realm_model.RealmIngredient
import com.example.paulina.assignmentapplication.recipes.realm_model.RealmRecipe
import io.reactivex.Completable
import io.realm.Realm
import io.realm.RealmList

/**
 * Created by Paulina on 2018-03-09.
 */
class RealmConverter {

    val realm = Realm.getDefaultInstance()

    fun saveRecipesToRealmRecipes(recipes: List<Recipe>): Completable {
        realm.beginTransaction()
        realm.delete(RealmRecipe::class.java)
        var realmList = RealmList<RealmRecipe>()
        for(recipe in recipes){
            var realmRecipe = RealmRecipe()
            realmRecipe.id = recipe.id
            realmRecipe.title = recipe.title
            realmRecipe.description = recipe.description
            realmRecipe.ingredients = convertIngredientsToRealmIngredients(recipe.ingredients)
            realmRecipe.images = convertImageInfosToRealmImagesInfo(recipe.images)
            realmList.add(realmRecipe)
        }
        realm.copyToRealm(realmList)
        realm.commitTransaction()
        realm.close()
        return Completable.complete()
    }

    private fun convertIngredientsToRealmIngredients(ingredients: List<Ingredient>): RealmList<RealmIngredient>{
        var realmList = RealmList<RealmIngredient>()
        for(ingredient in ingredients){
            var realmIngredient = RealmIngredient()
            realmIngredient.id = ingredient.id
            realmIngredient.name = ingredient.name
            realmIngredient.elements = convertElementsToRealmElements(ingredient.elements)
            realmList.add(realmIngredient)
        }
        realm.copyToRealm(realmList)
        return realmList
    }

    private fun convertElementsToRealmElements(elements: List<Element>): RealmList<RealmElement>{
        var realmList = RealmList<RealmElement>()
        for(element in elements){
            var realmElement = RealmElement()
            realmElement.id = element.id
            realmElement.name = element.name
            realmList.add(realmElement)
        }
        realm.copyToRealm(realmList)
        return realmList
    }

    private fun convertImageInfosToRealmImagesInfo(images: List<ImageInfo>): RealmList<RealmImageInfo>{
        var realmList = RealmList<RealmImageInfo>()
        for(image in images){
            var realmImageInfo = RealmImageInfo()
            realmImageInfo.imboId = image.imboId
            realmImageInfo.url = image.url
            realmList.add(realmImageInfo)
        }
        realm.copyToRealm(realmList)
        return realmList
    }

/*    private fun convertRealmRecipesToRecipes(realmRecipes: RealmList<RealmRecipe>) : List<Recipe>{

        var recipes: List<Recipe> = ArrayList<Recipe>()
        for(realmRecipe in realmRecipes){
            var imagesInfo: List<ImageInfo> = ArrayList<ImageInfo>()
            for(realmImageInfo in realmRecipe.images){
                var imageInfo = ImageInfo(realmImageInfo.imboId, realmImageInfo.url)
                imagesInfo.add
            }
            var recipe = Recipe(realmRecipe.title!!, realmRecipe.description!!, realmRecipe.id!!, realmRecipe.images!! )
        }
    }*/

}