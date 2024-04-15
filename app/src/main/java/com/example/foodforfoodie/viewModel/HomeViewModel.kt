package com.example.foodforfoodie.viewModel

import android.util.Log
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.foodforfoodie.db.MealDatabase
import com.example.foodforfoodie.pojo.Category
import com.example.foodforfoodie.pojo.CategoryList
import com.example.foodforfoodie.pojo.MealByCategoryList
import com.example.foodforfoodie.pojo.MealByCategory
import com.example.foodforfoodie.pojo.Meal
import com.example.foodforfoodie.pojo.MealList
import com.example.foodforfoodie.retrofit.RetrofitInstance
import kotlinx.coroutines.launch
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response

class HomeViewModel(val mealDatabase: MealDatabase) :ViewModel(){

    private  var randomMealLiveData=MutableLiveData<Meal>()
    private var popularItemLiveData=MutableLiveData<List<MealByCategory>>()
    private var categoriesLiveData=MutableLiveData<List<Category>>()
    private var favoriteMealLiveData=mealDatabase.mealDao().getAllMeals()
    private var bottomSheetLiveData=MutableLiveData<Meal?>()
    private var searchMealLiveData=MutableLiveData<List<Meal>>()

    private var saveStateResponseMeal: Meal ?= null

    fun getRandomMeal() {
        saveStateResponseMeal?.let {randomMeal->
            randomMealLiveData.postValue(randomMeal)
            return
        }

        RetrofitInstance.api.getRandomMeal().enqueue(object : Callback<MealList> {
            override fun onResponse(call: Call<MealList>, response: Response<MealList>) {
                if (response.body() != null) {
                    val randomMeal: Meal = response.body()!!.meals[0]
                    randomMealLiveData.value = randomMeal
                    saveStateResponseMeal=randomMeal

                } else {
                    return
                }
            }

            override fun onFailure(call: Call<MealList>, t: Throwable) {
                Log.d("HomeFragment", t.message.toString())
            }

        })
    }

    fun getPopularItem(){
        RetrofitInstance.api.getPopularItem("Dessert").enqueue(object:Callback<MealByCategoryList> {
            override fun onResponse(call: Call<MealByCategoryList>, response: Response<MealByCategoryList>) {
            if(response.body()!=null){
               popularItemLiveData.value=response.body()!!.meals
               }
            }

            override fun onFailure(call: Call<MealByCategoryList>, t: Throwable) {
                Log.d("HomeFragment",t.message.toString())

            }
        })

    }



    fun getCategories() {
        RetrofitInstance.api.getCategories().enqueue(object : Callback<CategoryList> {
            override fun onResponse(call: Call<CategoryList>, response: Response<CategoryList>) {
              response.body()?.let {categoryList ->
                  categoriesLiveData.postValue(categoryList.categories)
                }
            }

            override fun onFailure(call: Call<CategoryList>, t: Throwable) {
                Log.e("HomeViewModel", t.message.toString())
            }

        })
    }

    fun getMealById(id:String) {
        RetrofitInstance.api.getMealDetails(id).enqueue(object : Callback<MealList> {
            override fun onResponse(call: Call<MealList>, response: Response<MealList>) {
                val meal=response.body()?.meals?.first()
                meal?.let {meals->
                    bottomSheetLiveData.postValue(meal)

                }

            }

            override fun onFailure(call: Call<MealList>, t: Throwable) {
                Log.e("HomeViewModel",t.message.toString())
            }

        })
    }



    fun searchMeals(searchQuery:String) {
        RetrofitInstance.api.searchMeals(searchQuery).enqueue(object : Callback<MealList> {
            override fun onResponse(call: Call<MealList>, response: Response<MealList>) {
                val mealList=response.body()?.meals
                mealList?.let {
                    searchMealLiveData.postValue(it)

                }

            }

            override fun onFailure(call: Call<MealList>, t: Throwable) {
                Log.e("HomeViewModel",t.message.toString())
            }

        })
    }
    fun observeSearchMealLiveData():LiveData<List<Meal>>{
        return searchMealLiveData
    }


    fun insertMeal(meal: Meal){
        viewModelScope.launch {
            mealDatabase.mealDao().upsert(meal)
        }
    }


    fun deleteMeal(meal: Meal){
        viewModelScope.launch {
            mealDatabase.mealDao().delete(meal)
        }
    }
    fun observeRandomMealLiveData():LiveData<Meal?>{
        return randomMealLiveData
                   }

    fun observePopularItemLiveData():LiveData<List<MealByCategory>>{
        return popularItemLiveData
    }
    fun observeCategoriesLiveData(): LiveData<List<Category>> {
        return categoriesLiveData
    }


    fun observeFavoriteMealLiveData(): LiveData<List<Meal>> {
        return  favoriteMealLiveData
    }

    fun observeBottomSheetMeal(): MutableLiveData<Meal?> = bottomSheetLiveData
}