package com.example.foodforfoodie.fragment

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.core.widget.addTextChangedListener
import androidx.lifecycle.Observer
import androidx.lifecycle.lifecycleScope
import androidx.navigation.fragment.findNavController
import androidx.recyclerview.widget.GridLayoutManager
import androidx.recyclerview.widget.LinearLayoutManager
import com.example.foodforfoodie.R
import com.example.foodforfoodie.activities.MainActivity
import com.example.foodforfoodie.adapter.FavoriteMealAdapter
import com.example.foodforfoodie.databinding.FragmentSearchBinding
import com.example.foodforfoodie.viewModel.HomeViewModel
import kotlinx.coroutines.Job
import kotlinx.coroutines.delay
import kotlinx.coroutines.launch

class SearchFragment : Fragment() {
    private lateinit var binding: FragmentSearchBinding
    private lateinit var viewModel: HomeViewModel
    private lateinit var  searchRecyclerViewAdapter:FavoriteMealAdapter

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        viewModel=(activity as MainActivity).viewModel

    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        binding =FragmentSearchBinding.inflate(inflater)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        onSearchBackClick()

        prepareRecyclerView()
        observeSearchMealsLiveDaa()

        var searchJob: Job?=null
        binding.searchBox.addTextChangedListener {searchQuery->
            searchJob?.cancel()
            searchJob=lifecycleScope.launch {
                delay(300)
                viewModel.searchMeals(searchQuery.toString())
            }
        }
    }

    private fun onSearchBackClick() {
        binding.searchBack.setOnClickListener {
            findNavController().navigate(R.id.action_searchFragment_to_home)

        }
    }

    private fun observeSearchMealsLiveDaa() {
        viewModel.observeSearchMealLiveData().observe(viewLifecycleOwner, Observer {meals->
            searchRecyclerViewAdapter.differ.submitList(meals)

        })
    }

    private fun searchMeals() {
        val searchQuery=binding.searchBox.text.toString()
        if (searchQuery.isNotEmpty()){
            viewModel.searchMeals(searchQuery)
        }
    }

    private fun prepareRecyclerView() {
        searchRecyclerViewAdapter=FavoriteMealAdapter()
        binding.recSearchMeals.apply {
            layoutManager= GridLayoutManager(context,2, GridLayoutManager.VERTICAL,false)
            adapter=searchRecyclerViewAdapter
        }
    }
}