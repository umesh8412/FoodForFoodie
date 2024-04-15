package com.example.foodforfoodie.adapter

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.AsyncListDiffer
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import com.example.foodforfoodie.databinding.MealtemBinding
import com.example.foodforfoodie.pojo.Meal
import java.util.Objects

class FavoriteMealAdapter : RecyclerView.Adapter<FavoriteMealAdapter.FavoriteMealAdapterViewHolder>() {
    class FavoriteMealAdapterViewHolder(val binding: MealtemBinding) : RecyclerView.ViewHolder(binding.root)

    private val diffUtil = object : DiffUtil.ItemCallback<Meal>() {
        override fun areItemsTheSame(oldItem: Meal, newItem: Meal): Boolean {
            return oldItem.idMeal == newItem.idMeal
        }

        override fun areContentsTheSame(oldItem: Meal, newItem: Meal): Boolean {
            return oldItem == newItem
        }

    }

    val differ = AsyncListDiffer(this, diffUtil)

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): FavoriteMealAdapterViewHolder {
        return FavoriteMealAdapterViewHolder(
            MealtemBinding.inflate(
                LayoutInflater.from(parent.context), parent, false
            )
        )
    }

    override fun onBindViewHolder(holder: FavoriteMealAdapterViewHolder, position: Int) {
        val meal = differ.currentList[position]
        Glide.with(holder.itemView).load(meal.strMealThumb).into(holder.binding.imageMeal)
        holder.binding.tvMealName.text = meal.strMeal
    }

    override fun getItemCount(): Int {
        return differ.currentList.size
    }


}