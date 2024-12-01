package com.example.recipeapp.model

data class Recipe(
    val id: Int,
    val title: String,
    val image: String,
    val readyInMinutes: Int,
    val servings: Int,
    val summary: String,
    val instructions: String,
    val ingredients: List<Ingredient>,
    val extendedIngredients: List<Ingredient>,
    val nutrition: Nutrition?
)

data class Nutrition(
    val nutrients: List<Nutrient>
)

data class Nutrient(
    val title: String,
    val amount: Float,
    val unit: String
)


