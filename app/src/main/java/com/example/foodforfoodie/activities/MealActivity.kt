package com.example.foodforfoodie.activities

import android.content.Intent
import android.net.Uri
import android.os.Build
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.widget.Toast
import androidx.annotation.RequiresApi
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProvider
import androidx.lifecycle.viewmodel.viewModelFactory
import com.bumptech.glide.Glide
import com.example.foodforfoodie.R
import com.example.foodforfoodie.databinding.ActivityMealBinding
import com.example.foodforfoodie.db.MealDatabase
import com.example.foodforfoodie.fragment.HomeFragment
import com.example.foodforfoodie.pojo.Meal
import com.example.foodforfoodie.viewModel.HomeViewModel
import com.example.foodforfoodie.viewModel.MealViewModel
import com.example.foodforfoodie.viewModel.MealViewModelFactory

@Suppress("DEPRECATION")
class MealActivity : AppCompatActivity() {
    private lateinit var mealId:String
    private lateinit var mealName:String
    private lateinit var mealThumb:String
    private lateinit var binding:ActivityMealBinding
    private lateinit var youtubeLink:String
    private lateinit var mealMvvm:MealViewModel
    


    @RequiresApi(Build.VERSION_CODES.M)
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
          binding=ActivityMealBinding.inflate(layoutInflater)
         setContentView(binding.root)

        val mealDatabase=MealDatabase.getInstance(this)
        val viewModelProviderFactory= MealViewModelFactory(mealDatabase)
        mealMvvm = ViewModelProvider(this, viewModelProviderFactory)[MealViewModel::class.java]


        getMealInformationFromIntent()
        setInformationInViews()

        mealMvvm.getMealDetails(mealId)
        observerMealDetailsLiveData()

        onYoutubeImageClick()
        onFavoriteClick()
    }

    private fun onFavoriteClick() {
        binding.btnFavorite.setOnClickListener{
            mealToSave?.let{
                mealMvvm.insertMeal(it)
                Toast.makeText(this,"Meal Save",Toast.LENGTH_SHORT).show()
            }
        }
    }

    private fun onYoutubeImageClick() {
        binding.imageYoutube.setOnClickListener{
            val intent=Intent (Intent.ACTION_VIEW, Uri.parse(youtubeLink))

            startActivity(intent)
        }
    }

    private var mealToSave:Meal?=null

    private fun observerMealDetailsLiveData() {
        mealMvvm.observerMealDetailsLiveData().observe(this, object : Observer<Meal?> {  // Explicitly specify type here
            override fun onChanged(t: Meal?) {
                val meal = t
                mealToSave=meal

                binding.mealCategory.text = "Category:${meal!!.strCategory}"
                binding.mealArea.text = "Area:${meal!!.strArea}"
                binding.instcuctionStep.text = meal.strInstructions

                youtubeLink= meal.strYoutube.toString()
            }
        })
    }


    @RequiresApi(Build.VERSION_CODES.M)
    private fun setInformationInViews() {
        Glide.with(applicationContext)
            .load(mealThumb)
            .into(binding.imageMealDetail)

        binding.collapsingToolbar.title=mealName
        binding.collapsingToolbar.setCollapsedTitleTextColor(resources.getColor(R.color.white))
        binding.collapsingToolbar.setExpandedTitleColor(resources.getColor(R.color.white))
    }

    private fun getMealInformationFromIntent() {
        val intent=intent
        mealId = intent.getStringExtra(HomeFragment.MEAL_ID)!!
        mealName=intent.getStringExtra(HomeFragment.MEAL_NAME)!!
        mealThumb=intent.getStringExtra(HomeFragment.MEAL_THUMB)!!

    }
}