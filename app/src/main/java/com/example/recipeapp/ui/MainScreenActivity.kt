package com.example.recipeapp.ui

import com.example.recipeapp.ui.MainActivity
import android.content.Intent
import android.os.Bundle
import android.view.View
import android.widget.Button
import android.widget.EditText
import android.widget.ImageView
import androidx.recyclerview.widget.RecyclerView
import androidx.appcompat.app.AppCompatActivity
import androidx.recyclerview.widget.LinearLayoutManager
import com.example.recipeapp.R
import com.example.recipeapp.model.RandomRecipeResponse
import com.example.recipeapp.model.RecipeResponse
import com.example.recipeapp.ui.adapters.RandomRecipeAdapter
import com.example.recipeapp.DataManager.DataManager
import com.example.recipeapp.remote.client.RetrofitClient
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response

class MainScreenActivity : AppCompatActivity() {

    private lateinit var recipeRecyclerView: RecyclerView
    private lateinit var searchRecyclerView: RecyclerView
    private lateinit var searchButton: Button
    private lateinit var advancedSearchButton: Button
    private lateinit var searchBar: EditText
    private lateinit var refreshButton: ImageView // Nuevo
    private lateinit var adapter: RandomRecipeAdapter
    private lateinit var dataManager: DataManager

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_mainscreen)

        dataManager = DataManager()

        recipeRecyclerView = findViewById(R.id.RandomrecipeRecyclerView)
        searchRecyclerView = findViewById(R.id.SearchrecipeRecyclerView)
        searchButton = findViewById(R.id.searchButton)
        advancedSearchButton = findViewById(R.id.AdvancedsearchButton)
        searchBar = findViewById(R.id.search_bar)
        refreshButton = findViewById(R.id.Refresh) // Nuevo

        recipeRecyclerView.layoutManager = LinearLayoutManager(this)
        adapter = RandomRecipeAdapter(emptyList()) { recipe ->
            val intent = Intent(this, RecipeDetailActivity::class.java)
            intent.putExtra("RECIPE_ID", recipe.id)
            startActivity(intent)
        }
        recipeRecyclerView.adapter = adapter

        searchRecyclerView.layoutManager = LinearLayoutManager(this)
        val searchAdapter = RandomRecipeAdapter(emptyList()) { recipe ->
            val intent = Intent(this, RecipeDetailActivity::class.java)
            intent.putExtra("RECIPE_ID", recipe.id)
            startActivity(intent)
        }
        searchRecyclerView.adapter = searchAdapter

        searchButton.setOnClickListener {
            val query = searchBar.text.toString()
            if (query.isNotEmpty()) {
                recipeRecyclerView.visibility = View.GONE
                searchRecyclerView.visibility = View.VISIBLE
                fetchSearchedRecipes(query, searchAdapter)
            }
        }

        advancedSearchButton.setOnClickListener {
            val intent = Intent(this, MainActivity::class.java)
            startActivity(intent)
        }

        // Nuevo: Listener para refrescar recetas aleatorias
        refreshButton.setOnClickListener {
            fetchRandomRecipes()
        }

        fetchRandomRecipes()
    }

    private fun fetchRandomRecipes() {
        val apiService = RetrofitClient.instance
        val apiKey = dataManager.getApiKey()
        val call = apiService.getRandomRecipes(apiKey, 20)

        call.enqueue(object : Callback<RandomRecipeResponse> {
            override fun onResponse(
                call: Call<RandomRecipeResponse>,
                response: Response<RandomRecipeResponse>
            ) {
                if (response.isSuccessful) {
                    val recipes = response.body()?.recipes
                    if (!recipes.isNullOrEmpty()) {
                        recipeRecyclerView.visibility = View.VISIBLE
                        adapter.updateRecipes(recipes)
                    }
                }
            }

            override fun onFailure(call: Call<RandomRecipeResponse>, t: Throwable) {
            }
        })
    }

    private fun fetchSearchedRecipes(query: String, searchAdapter: RandomRecipeAdapter) {
        val apiService = RetrofitClient.instance
        val apiKey = dataManager.getApiKey()
        val call = apiService.searchRecipes(
            query,
            15,
            cuisine = null,
            maxReadyTime = null,
            maxCalories = null,
            apiKey = apiKey
        )

        call.enqueue(object : Callback<RecipeResponse> {
            override fun onResponse(
                call: Call<RecipeResponse>,
                response: Response<RecipeResponse>
            ) {
                if (response.isSuccessful) {
                    val recipes = response.body()?.results
                    if (!recipes.isNullOrEmpty()) {
                        searchRecyclerView.visibility = View.VISIBLE
                        searchAdapter.updateRecipes(recipes)
                    } else {
                        searchRecyclerView.visibility = View.GONE
                        val intent = Intent(this@MainScreenActivity, ErrorActivity::class.java)
                        startActivity(intent)
                    }
                } else {
                    val intent = Intent(this@MainScreenActivity, ErrorActivity::class.java)
                    startActivity(intent)
                }
            }

            override fun onFailure(call: Call<RecipeResponse>, t: Throwable) {
                val intent = Intent(this@MainScreenActivity, ErrorActivity::class.java)
                startActivity(intent)
            }
        })
    }
}
