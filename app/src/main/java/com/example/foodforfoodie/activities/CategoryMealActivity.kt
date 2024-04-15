package com.example.foodforfoodie.activities

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.util.Log
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProvider
import androidx.recyclerview.widget.GridLayoutManager
import com.example.foodforfoodie.R
import com.example.foodforfoodie.adapter.CategoryMealAdapter
import com.example.foodforfoodie.databinding.ActivityCategoryMealBinding
import com.example.foodforfoodie.databinding.ActivityMealBinding
import com.example.foodforfoodie.fragment.HomeFragment
import com.example.foodforfoodie.viewModel.CategoryMealViewModel

class CategoryMealActivity : AppCompatActivity() {
    lateinit var binding:ActivityCategoryMealBinding
    lateinit var categoryMealViewModel: CategoryMealViewModel
    lateinit var  categoryMealAdapter: CategoryMealAdapter

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding=ActivityCategoryMealBinding.inflate(layoutInflater)
        setContentView(binding.root)

        prepareRecyclerView()

        categoryMealViewModel= ViewModelProvider(this)[CategoryMealViewModel::class.java]

        categoryMealViewModel.getMealByCategory(intent.getStringExtra(HomeFragment.CATEGORY_NAME)!!)

        categoryMealViewModel.observeMealLiveData().observe(this, Observer {  mealList->
            binding.tvCategoryCount.text=mealList.size.toString()
            categoryMealAdapter.setMealList(mealList)
        })


    }

    private fun prepareRecyclerView() {
        categoryMealAdapter= CategoryMealAdapter()
        binding.recMeal.apply {
            layoutManager=GridLayoutManager(context,2,GridLayoutManager.VERTICAL,false)
        adapter=categoryMealAdapter
        }
    }
}