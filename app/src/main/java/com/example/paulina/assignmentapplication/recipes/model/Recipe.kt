package com.example.paulina.assignmentapplication.recipes.model

/**
 * Created by Paulina on 2018-03-08.
 */

data class Recipe (val title: String = "", val description: String = "", val id: Long = 0L, val images: List<ImageInfo>, val ingredients: List<Ingredient>)

data class Ingredient (val id: Long = 0L, val name: String = "", val elements: List<Element>)

data class Element (val id: Long = 0L, val name: String = "")

data class ImageInfo (val imboId: String = "", val url: String = "")
