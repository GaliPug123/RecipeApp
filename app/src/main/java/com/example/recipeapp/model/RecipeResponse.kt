package com.example.recipeapp.model

data class RecipeResponse(
    val results: List<Recipe>,
    val totalResults: Int
)
