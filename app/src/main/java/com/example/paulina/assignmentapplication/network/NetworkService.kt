package com.example.paulina.assignmentapplication.network

import com.example.paulina.assignmentapplication.recipes.model.Recipe
import io.reactivex.Single
import retrofit2.http.GET
import retrofit2.http.Query

/**
 * Created by Paulina on 2018-03-08.
 */
interface NetworkService {

    @GET("getRecipesListDetailed")
    fun getRecipes(@Query("from") offset: Int,
                   @Query("limit") limit: Int,
                   @Query("tags") tags: String,
                   @Query("size") size: String)
            : Single<List<Recipe>>

}