package com.example.recipeapp.ui

import android.os.Bundle
import android.util.Log
import android.widget.ImageView
import android.widget.TextView
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import com.bumptech.glide.load.engine.DiskCacheStrategy
import com.example.recipeapp.R
import com.example.recipeapp.model.Recipe
import com.example.recipeapp.remote.client.RetrofitClient
import com.example.recipeapp.ui.adapters.IngredientsAdapter
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response

class RecipeDetailActivity : AppCompatActivity() {

    private lateinit var recipeTitleTextView: TextView
    private lateinit var recipeImageView: ImageView
    private lateinit var recipeSummaryTextView: TextView
    private lateinit var recipeInstructionsTextView: TextView
    private lateinit var recipeReadyTimeTextView: TextView
    private lateinit var recipeServingsTextView: TextView
    private lateinit var ingredientsRecyclerView: RecyclerView


    private var recipeId: Int = 0

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_recipe_detail)

        recipeTitleTextView = findViewById(R.id.recipeDetailTitle)
        recipeImageView = findViewById(R.id.recipeDetailImage)
        recipeSummaryTextView = findViewById(R.id.recipeDetailSummary)
        recipeInstructionsTextView = findViewById(R.id.recipeDetailInstructions)
        recipeReadyTimeTextView = findViewById(R.id.recipeDetailReadyTime)
        recipeServingsTextView = findViewById(R.id.recipeDetailServings)
        ingredientsRecyclerView = findViewById(R.id.ingredientsRecyclerView)


        val recipeTitleImageView: ImageView = findViewById(R.id.recipeTitle)
        recipeTitleImageView.setOnClickListener {
            finish()
        }

        recipeId = intent.getIntExtra("RECIPE_ID", 0)
        Log.d("RecipeDetailActivity", "Fetching details for Recipe ID: $recipeId")
        if (recipeId == 0) {
            Toast.makeText(this, "Invalid Recipe ID", Toast.LENGTH_SHORT).show()
            finish()
        } else {
            fetchRecipeInformation()
        }
        ingredientsRecyclerView.layoutManager = LinearLayoutManager(this)
    }

    private fun fetchRecipeInformation() {
        val apiService = RetrofitClient.instance
        val apiKey = "88d588fcc15a45aaa0bdf088ddf0be6d"
        val call = apiService.getRecipeInformation(recipeId, apiKey, true)

        call.enqueue(object : Callback<Recipe> {
            override fun onResponse(call: Call<Recipe>, response: Response<Recipe>) {
                if (response.isSuccessful) {
                    val recipe = response.body()
                    recipe?.let {
                        recipeTitleTextView.text = it.title
                        recipeSummaryTextView.text = it.summary.stripHtml().ifEmpty { " " }
                        recipeInstructionsTextView.text = it.instructions.stripHtml().ifEmpty { " " }
                        recipeReadyTimeTextView.text = "${it.readyInMinutes} mins"
                        recipeServingsTextView.text = "${it.servings} Servings"

                        Glide.with(this@RecipeDetailActivity)
                            .load(it.image)
                            .skipMemoryCache(true)
                            .diskCacheStrategy(DiskCacheStrategy.NONE)
                            .into(recipeImageView)

                        ingredientsRecyclerView.adapter = IngredientsAdapter(it.extendedIngredients)
                    }
                } else {
                    Log.e("RecipeDetailActivity", "Error: ${response.code()} - ${response.message()}")
                    Toast.makeText(this@RecipeDetailActivity, "Failed to retrieve recipe details.", Toast.LENGTH_SHORT).show()
                }
            }

            override fun onFailure(call: Call<Recipe>, t: Throwable) {
                Toast.makeText(this@RecipeDetailActivity, "Error: ${t.message}", Toast.LENGTH_SHORT).show()
            }
        })
    }
}

private fun String.stripHtml(): String {
    return this.replace(Regex("<[^>]*>"), "") // Quitar los tags de HTML porque aparecian
}
