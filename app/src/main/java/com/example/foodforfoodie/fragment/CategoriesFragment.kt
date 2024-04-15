package com.example.foodforfoodie.fragment

import android.content.Intent
import android.os.Binder
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.lifecycle.Observer
import androidx.recyclerview.widget.GridLayoutManager
import com.example.foodforfoodie.R
import com.example.foodforfoodie.activities.CategoryMealActivity
import com.example.foodforfoodie.activities.MainActivity
import com.example.foodforfoodie.adapter.CategoriesAdapter
import com.example.foodforfoodie.databinding.FragmentCategoriesBinding
import com.example.foodforfoodie.databinding.FragmentFavouriteBinding
import com.example.foodforfoodie.databinding.MealtemBinding
import com.example.foodforfoodie.fragment.HomeFragment.Companion.CATEGORY_NAME
import com.example.foodforfoodie.viewModel.HomeViewModel

class CategoriesFragment:Fragment(R.layout.fragment_categories) {
    private lateinit var binding:FragmentCategoriesBinding
    private lateinit var categoriesAdapter: CategoriesAdapter
    private lateinit var viewModel: HomeViewModel



    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        viewModel=(activity as MainActivity).viewModel

    }

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View? {
        binding=FragmentCategoriesBinding.inflate(inflater)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        prepareRecyclerView()
        observeCategory()
        onCategoryClick()
    }


    private fun prepareRecyclerView() {
        categoriesAdapter= CategoriesAdapter()
        binding.recFragmentCategory.apply{
            layoutManager= GridLayoutManager(context,3, GridLayoutManager.VERTICAL,false)
            adapter=categoriesAdapter
        }
    }

    private fun observeCategory() {
        viewModel.observeCategoriesLiveData().observe( viewLifecycleOwner, Observer { categories->
            categoriesAdapter.setCategoryList(categories)
        })
    }

    private fun onCategoryClick() {
        categoriesAdapter.onItemClick={category ->
            val intent= Intent(activity, CategoryMealActivity::class.java)
            intent.putExtra(CATEGORY_NAME,category.strCategory)
            startActivity(intent)
        }
    }
}

