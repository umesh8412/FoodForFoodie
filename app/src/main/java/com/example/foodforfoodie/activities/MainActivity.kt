package com.example.foodforfoodie.activities

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import androidx.lifecycle.ViewModelProvider
import androidx.navigation.ui.NavigationUI
import com.google.android.material.bottomnavigation.BottomNavigationView
import androidx.navigation.Navigation
import com.example.foodforfoodie.R
import com.example.foodforfoodie.db.MealDatabase
import com.example.foodforfoodie.viewModel.HomeViewModel
import com.example.foodforfoodie.viewModel.HomeViewModelFactory

class MainActivity : AppCompatActivity() {
    val viewModel: HomeViewModel by lazy {
        val mealDatabase=MealDatabase.getInstance(this)
        val homeViewModelProvider=HomeViewModelFactory(mealDatabase)
        ViewModelProvider(this,homeViewModelProvider)[HomeViewModel::class.java]
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        val bottomNavigationView = findViewById<BottomNavigationView>(R.id.bottomNavigation)
        val navController=Navigation.findNavController(this, R.id.container)
        NavigationUI.setupWithNavController(bottomNavigationView,navController)



    }

}