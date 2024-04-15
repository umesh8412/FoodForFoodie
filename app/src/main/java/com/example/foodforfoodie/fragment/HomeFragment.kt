package com.example.foodforfoodie.fragment

import android.content.Intent
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.lifecycle.Observer
import androidx.navigation.fragment.findNavController
import androidx.recyclerview.widget.GridLayoutManager
import androidx.recyclerview.widget.LinearLayoutManager
import com.bumptech.glide.Glide
import com.example.foodforfoodie.R
import com.example.foodforfoodie.activities.CategoryMealActivity
import com.example.foodforfoodie.activities.MainActivity
import com.example.foodforfoodie.activities.MealActivity
import com.example.foodforfoodie.adapter.CategoriesAdapter
import com.example.foodforfoodie.adapter.MostPopularAdapter
import com.example.foodforfoodie.databinding.FragmentHomeBinding
import com.example.foodforfoodie.fragment.bottomSheet.MealBottomSheetFragment
import com.example.foodforfoodie.pojo.MealByCategory
import com.example.foodforfoodie.pojo.Meal

import com.example.foodforfoodie.viewModel.HomeViewModel

class HomeFragment : Fragment(R.layout.fragment_home) {

    private lateinit var binding: FragmentHomeBinding
    private lateinit var viewModel: HomeViewModel
    private lateinit var randomMeal: Meal
    private lateinit var meal: MealByCategory
    private lateinit var popularItemAdapter: MostPopularAdapter
    private lateinit var categoriesAdapter: CategoriesAdapter

    companion object {
        const val MEAL_ID = "com.example.foodforfoodie.fragment.idMeal"
        const val MEAL_NAME = "com.example.foodforfoodie.fragment.nameMeal"
        const val MEAL_THUMB = "com.example.foodforfoodie.fragment.thumbMeal"
        const val CATEGORY_NAME = " com.example.foodforfoodie.fragment.categoryName"

    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        viewModel = (activity as MainActivity).viewModel
        popularItemAdapter = MostPopularAdapter()

    }


    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View {
        binding = FragmentHomeBinding.inflate(inflater, container, false)
        // Inflate the layout for this fragment
        return binding.root

    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        val swipeRefreshLayout = binding.swipeRefreshLayout

        swipeRefreshLayout.setOnRefreshListener {
            // Perform refresh action here
            viewModel.getRandomMeal()
            observerRandomMeal()

        }

        viewModel.getRandomMeal()
        observerRandomMeal()
        onRandomMealClick()

        preparePopularItemRecyclerView()
        viewModel.getPopularItem()
        observePopularItemLiveData()
        onPopularItemClick()

        prepareCategoriesRecyclerView()
        viewModel.getCategories()
        observeCategoriesLiveData()

        onCategoryClick()
        onPopularItemLonglick()

        onSearchIconClick()

    }

    private fun onSearchIconClick() {
        binding.searchMeals.setOnClickListener {
            findNavController().navigate(R.id.action_home_to_searchFragment)
        }
    }

    private fun onPopularItemLonglick() {
        popularItemAdapter.onLongItemClick = { meal ->
            val mealBottomSheetFragment = MealBottomSheetFragment.newInstance(meal.idMeal)
            mealBottomSheetFragment.show(childFragmentManager, "Meal Info")

        }
    }

    private fun onCategoryClick() {
        categoriesAdapter.onItemClick = { category ->
            val intent = Intent(activity, CategoryMealActivity::class.java)
            intent.putExtra(CATEGORY_NAME, category.strCategory)
            startActivity(intent)
        }
    }

    private fun prepareCategoriesRecyclerView() {
        categoriesAdapter = CategoriesAdapter()
        binding.recCategory.apply {
            layoutManager = GridLayoutManager(context, 3, GridLayoutManager.VERTICAL, false)
            adapter = categoriesAdapter
        }
    }

    private fun observeCategoriesLiveData() {
        viewModel.observeCategoriesLiveData().observe(viewLifecycleOwner, Observer { categories ->
            categoriesAdapter.setCategoryList(categories)
        })

    }

    private fun onPopularItemClick() {
        popularItemAdapter.onItemClick = { clickedMeal ->
            // Assign the clicked meal to the 'meal' property
            meal = clickedMeal

            val intent = Intent(activity, MealActivity::class.java)
            intent.putExtra(MEAL_ID, meal.idMeal)
            intent.putExtra(MEAL_NAME, meal.strMeal)
            intent.putExtra(MEAL_THUMB, meal.strMealThumb)
            startActivity(intent)
        }
    }

    private fun preparePopularItemRecyclerView() {
        binding.recPopItem.apply {
            layoutManager = LinearLayoutManager(activity, LinearLayoutManager.HORIZONTAL, false)
            adapter = popularItemAdapter
        }
    }

    private fun observePopularItemLiveData() {
        viewModel.observePopularItemLiveData().observe(viewLifecycleOwner) { mealList ->
            popularItemAdapter.setMeals(mealsList = mealList as ArrayList<MealByCategory>)

        }

    }

    private fun onRandomMealClick() {
        binding.randomMealCard.setOnClickListener {
            if (::randomMeal.isInitialized && randomMeal != null) {
                val intent = Intent(activity, MealActivity::class.java)
                intent.putExtra(MEAL_ID, randomMeal.idMeal)
                intent.putExtra(MEAL_NAME, randomMeal.strMeal)
                intent.putExtra(MEAL_THUMB, randomMeal.strMealThumb)
                startActivity(intent)
            } else {
                // Handle the case where randomMeal is not initialized yet
                // You may want to show a message or log an error.
            }
        }
    }

    private fun observerRandomMeal() {
        viewModel.observeRandomMealLiveData().observe(viewLifecycleOwner) { meal ->
            Glide.with(this@HomeFragment).load(meal!!.strMealThumb).into(binding.imageRandomMeal)

            this.randomMeal = meal
            binding.swipeRefreshLayout.isRefreshing = false

        }


    }


}


