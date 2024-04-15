package com.example.foodforfoodie.viewModel

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.example.foodforfoodie.pojo.MealByCategory
import com.example.foodforfoodie.pojo.MealByCategoryList
import com.example.foodforfoodie.retrofit.RetrofitInstance
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response

class CategoryMealViewModel():ViewModel() {
    val mealLiveData=MutableLiveData<List<MealByCategory>>()

    fun getMealByCategory(categoryName: String) {
        RetrofitInstance.api.getMealByCategory(categoryName).enqueue(object:Callback<MealByCategoryList>{
            override fun onResponse(
                call: Call<MealByCategoryList>,
                response: Response<MealByCategoryList>
            ) {
                response.body()?.let { mealList->
                    mealLiveData.postValue(mealList.meals)

                }

            }

            override fun onFailure(call: Call<MealByCategoryList>, t: Throwable) {
                TODO("Not yet implemented")
            }


        })
        
    }

    fun observeMealLiveData():LiveData<List<MealByCategory>>{
        return mealLiveData
    }

}
