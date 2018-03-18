package com.example.paulina.assignmentapplication.recipes.model

import io.realm.RealmObject

/**
 * Created by Paulina on 2018-03-08.
 */

data class Recipe (val title: String = "", val description: String = "", val id: Long = 0L, val images: List<ImageInfo> = listOf(), val ingredients: List<Ingredient> = listOf())

data class Ingredient (val id: Long = 0L, val name: String = "", val elements: List<Element>)

data class Element (val id: Long = 0L, val name: String = "")

data class ImageInfo (val imboId: String = "", val url: String = "")
