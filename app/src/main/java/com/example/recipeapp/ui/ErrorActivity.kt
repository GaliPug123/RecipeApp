package com.example.recipeapp.ui

import android.os.Bundle
import android.widget.ImageView
import androidx.appcompat.app.AppCompatActivity
import com.example.recipeapp.R

class ErrorActivity : AppCompatActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_error)

        val myIcon = findViewById<ImageView>(R.id.myicono9)

        myIcon.setOnClickListener {

            onBackPressed()
        }
    }
}
