package com.example.recipeapp.ui
import android.content.Intent
import android.os.Bundle
import android.util.Log
import android.view.View
import android.widget.Button
import android.widget.EditText
import android.widget.ImageView
import androidx.appcompat.app.AppCompatActivity
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.example.recipeapp.DataManager.DataManager
import com.example.recipeapp.R
import com.example.recipeapp.model.RecipeResponse
import com.example.recipeapp.ui.ErrorActivity
import com.example.recipeapp.ui.MainScreenActivity
import com.example.recipeapp.ui.adapters.RecipeAdapter
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response

class MainActivity : AppCompatActivity() {
    private lateinit var dataManager: DataManager
    private lateinit var searchEditText: EditText
    private lateinit var cuisineEditText: EditText
    private lateinit var maxTimeEditText: EditText
    private lateinit var maxCaloriesEditText: EditText
    private lateinit var searchButton: Button
    private lateinit var recipeRecyclerView: RecyclerView
    private lateinit var titleImageView: ImageView

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)


        searchEditText = findViewById(R.id.searchEditText)
        cuisineEditText = findViewById(R.id.cuisineEditText)
        maxTimeEditText = findViewById(R.id.maxTimeEditText)
        maxCaloriesEditText = findViewById(R.id.maxCaloriesEditText)
        searchButton = findViewById(R.id.searchButton)
        recipeRecyclerView = findViewById(R.id.recipeRecyclerView)
        titleImageView = findViewById(R.id.titleTextView)

        recipeRecyclerView.layoutManager = LinearLayoutManager(this)

        dataManager = DataManager()

        titleImageView.setOnClickListener {
            val intent = Intent(this, MainScreenActivity::class.java)
            intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP or Intent.FLAG_ACTIVITY_SINGLE_TOP)
            startActivity(intent)
        }

        // Set click listener for the search button
        searchButton.setOnClickListener {
            val query = searchEditText.text.toString()
            val cuisine = cuisineEditText.text.toString()
            val maxTime = maxTimeEditText.text.toString().toIntOrNull()
            val maxCalories = maxCaloriesEditText.text.toString().toIntOrNull()
            val apiKey = dataManager.getApiKey()

            dataManager.searchRecipes(query, 10, cuisine, maxTime, maxCalories, apiKey)
                .enqueue(object : Callback<RecipeResponse> {
                    override fun onResponse(
                        call: Call<RecipeResponse>,
                        response: Response<RecipeResponse>
                    ) {
                        if (response.isSuccessful) {
                            val recipes = response.body()?.results
                            if (!recipes.isNullOrEmpty()) {
                                recipeRecyclerView.visibility = View.VISIBLE
                                recipeRecyclerView.adapter = RecipeAdapter(recipes)
                            } else {
                                recipeRecyclerView.visibility = View.GONE
                                val intent = Intent(this@MainActivity, ErrorActivity::class.java)
                                startActivity(intent)
                            }
                        } else {
                            Log.e("RecipeApp", "Response not successful: ${response.code()}")
                        }
                    }

                    override fun onFailure(call: Call<RecipeResponse>, t: Throwable) {
                        Log.e("RecipeApp", "Error: ${t.message}")
                    }
                })
        }
    }
}
