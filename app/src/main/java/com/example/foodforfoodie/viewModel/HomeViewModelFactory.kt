package com.example.foodforfoodie.viewModel


import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import com.example.foodforfoodie.db.MealDatabase

class HomeViewModelFactory(private val mealDatabase: MealDatabase): ViewModelProvider.Factory{
    override fun <T : ViewModel> create(modelClass: Class<T>): T {
        return HomeViewModel(mealDatabase) as T
    }
}