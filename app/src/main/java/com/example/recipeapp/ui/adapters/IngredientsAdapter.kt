package com.example.recipeapp.ui.adapters

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import com.example.recipeapp.R
import com.example.recipeapp.model.Ingredient

class IngredientsAdapter(private val ingredients: List<Ingredient>) : RecyclerView.Adapter<IngredientsAdapter.IngredientViewHolder>() {

    class IngredientViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {
        val nameTextView: TextView = itemView.findViewById(R.id.ingredientName)
        val amountTextView: TextView = itemView.findViewById(R.id.ingredientAmount)
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): IngredientViewHolder {
        val view = LayoutInflater.from(parent.context).inflate(R.layout.item_ingredient, parent, false)
        return IngredientViewHolder(view)
    }

    override fun onBindViewHolder(holder: IngredientViewHolder, position: Int) {
        val ingredient = ingredients[position]
        holder.nameTextView.text = ingredient.name
        holder.amountTextView.text = "${ingredient.amount} ${ingredient.unit}"
    }

    override fun getItemCount(): Int = ingredients.size
}
