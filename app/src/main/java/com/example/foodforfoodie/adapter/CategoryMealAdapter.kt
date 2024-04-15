package com.example.foodforfoodie.adapter

import android.view.LayoutInflater
import android.view.ViewGroup

import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import com.example.foodforfoodie.databinding.MealtemBinding
import com.example.foodforfoodie.pojo.MealByCategory


class CategoryMealAdapter: RecyclerView.Adapter<CategoryMealAdapter.CategoryMealViewModel>() {


    private var mealList=ArrayList<MealByCategory>()

    fun setMealList(mealList: List<MealByCategory>){
        this.mealList=mealList as ArrayList<MealByCategory>
        notifyDataSetChanged()

    }

    inner class CategoryMealViewModel(val binding: MealtemBinding):RecyclerView.ViewHolder(binding.root)

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): CategoryMealViewModel {
        return CategoryMealViewModel(
            MealtemBinding.inflate(
                LayoutInflater.from(parent.context)
            )
        )

    }


    override fun onBindViewHolder(holder:  CategoryMealViewModel, position: Int) {
        Glide.with(holder.itemView).load(mealList[position].strMealThumb).into(holder.binding.imageMeal)
        holder.binding.tvMealName.text=mealList[position].strMeal

    }

    override fun getItemCount(): Int {
        return mealList.size
    }


}

