package com.example.recipeapp.DataManager

import com.example.recipeapp.remote.api.SpoonacularApiService
import com.example.recipeapp.remote.client.RetrofitClient
import com.example.recipeapp.model.RecipeResponse
import retrofit2.Call

class DataManager {

    private val apiService: SpoonacularApiService = RetrofitClient.instance
    private val apiKey: String = "88d588fcc15a45aaa0bdf088ddf0be6d"

    fun getApiKey(): String {
        return apiKey
    }

    fun searchRecipes(
        query: String,                 //ingredient or name
        number: Int,                   //total of recipes
        cuisine: String? = null,      //cuisine
        maxReadyTime: Int? = null,    //time
        maxCalories: Int? = null,     //calories
        apiKey: String
    ): Call<RecipeResponse> {
        return apiService.searchRecipes(query, number, cuisine, maxReadyTime, maxCalories, apiKey)
    }
}

