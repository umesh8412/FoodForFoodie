package com.example.foodforfoodie.fragment.bottomSheet

import android.content.Intent
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.lifecycle.Observer
import com.bumptech.glide.Glide
import com.example.foodforfoodie.R
import com.example.foodforfoodie.activities.MainActivity
import com.example.foodforfoodie.activities.MealActivity
import com.example.foodforfoodie.databinding.FragmentMealBottomSheetBinding
import com.example.foodforfoodie.fragment.HomeFragment
import com.example.foodforfoodie.viewModel.HomeViewModel
import com.google.android.material.bottomsheet.BottomSheetDialogFragment

// TODO: Rename parameter arguments, choose names that match
// the fragment initialization parameters, e.g. ARG_ITEM_NUMBER
private const val MEAL_ID = "param1"


class MealBottomSheetFragment : BottomSheetDialogFragment() {
    private var mealId: String? = null
    private lateinit var binding:FragmentMealBottomSheetBinding
    private lateinit var viewModel: HomeViewModel

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        arguments?.let {
            mealId = it.getString(MEAL_ID)
        }
        viewModel=(activity as MainActivity).viewModel
    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        binding=FragmentMealBottomSheetBinding.inflate(inflater)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        mealId?.let {
            viewModel.getMealById(it)
        }
        observeBottomSheetMeal()
        onBottomDialogClick()
    }

    private fun onBottomDialogClick() {
        binding.bottomSheet.setOnClickListener{
            if (mealName !=null && mealThumb !=null){
                val intent=Intent(activity,MealActivity::class.java)
                intent.apply {
                    putExtra(HomeFragment.MEAL_ID,mealId)
                    putExtra(HomeFragment.MEAL_NAME,mealName)
                    putExtra(HomeFragment.MEAL_THUMB,mealThumb)
                }
                startActivity(intent)
            }
        }
    }

    private var mealName:String?=null
    private var mealThumb:String?=null

    private fun observeBottomSheetMeal() {
        viewModel.observeBottomSheetMeal().observe(viewLifecycleOwner, Observer { meal->
            if (meal != null) {
                Glide.with(this).load(meal.strMealThumb).into(binding.imgBottomSheet)
                binding.tvBottomSheetArea.text=meal.strArea
                binding.tvBottomSheetCategory.text=meal.strCategory
                binding.tvBottomSheetMealName.text=meal.strMeal

                mealName=meal.strMeal
                mealThumb=meal.strMealThumb
            }
        })

    }

    companion object {


        @JvmStatic
        fun newInstance(param1: String) =
            MealBottomSheetFragment().apply {
                arguments = Bundle().apply {
                    putString(MEAL_ID, param1)

                }
            }




    }
}