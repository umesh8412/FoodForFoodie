package com.example.foodforfoodie.adapter

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import com.example.foodforfoodie.databinding.PopularItemBinding
import com.example.foodforfoodie.pojo.MealByCategory

class MostPopularAdapter :RecyclerView.Adapter<MostPopularAdapter.PopularMealViewHolder>(){
     lateinit var onItemClick:((MealByCategory)->Unit)
     var onLongItemClick:((MealByCategory)->Unit)?=null
    private var mealsList=ArrayList<MealByCategory>()


    fun setMeals(mealsList:ArrayList<MealByCategory>){
        this.mealsList=mealsList
        notifyDataSetChanged()
    }
    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): PopularMealViewHolder {
     return PopularMealViewHolder(PopularItemBinding.inflate(LayoutInflater.from(parent.context),parent,false))
    }

    override fun onBindViewHolder(holder: PopularMealViewHolder, position: Int) {
        Glide.with(holder.itemView)
            .load(mealsList[position].strMealThumb)
            .into(holder.binding.popularItemImage)

        holder.itemView.setOnClickListener{
            onItemClick?.invoke(mealsList[position])
        }

        holder.itemView.setOnLongClickListener {
            onLongItemClick?.invoke(mealsList[position])
            true
        }

    }
    override fun getItemCount(): Int {
        return mealsList.size
    }

    class PopularMealViewHolder( val binding: PopularItemBinding):RecyclerView.ViewHolder(binding.root)

}




