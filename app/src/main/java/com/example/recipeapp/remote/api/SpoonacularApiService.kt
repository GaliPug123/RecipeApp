package com.example.recipeapp.remote.api

import com.example.recipeapp.model.RandomRecipeResponse
import com.example.recipeapp.model.Recipe
import com.example.recipeapp.model.RecipeResponse
import retrofit2.Call
import retrofit2.http.GET
import retrofit2.http.Path
import retrofit2.http.Query

interface SpoonacularApiService {
    @GET("recipes/complexSearch")
    fun searchRecipes(
        @Query("query") query: String,
        @Query("number") number: Int,
        @Query("cuisine") cuisine: String?,
        @Query("maxReadyTime") maxReadyTime: Int?,
        @Query("maxCalories") maxCalories: Int?,
        @Query("apiKey") apiKey: String
    ): Call<RecipeResponse>

    @GET("recipes/{id}/information")
    fun getRecipeInformation(
        @Path("id") id: Int,
        @Query("apiKey") apiKey: String,
        @Query("includeNutrition") includeNutrition: Boolean = true
    ): Call<Recipe>

    @GET("recipes/random")
    fun getRandomRecipes(
        @Query("apiKey") apiKey: String,
        @Query("number") number: Int
    ): Call<RandomRecipeResponse>

}


