package com.example.foodforfoodie.fragment

import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.lifecycle.Observer
import androidx.recyclerview.widget.GridLayoutManager
import androidx.recyclerview.widget.ItemTouchHelper
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.example.foodforfoodie.R
import com.example.foodforfoodie.activities.MainActivity
import com.example.foodforfoodie.adapter.FavoriteMealAdapter
import com.example.foodforfoodie.databinding.FragmentFavouriteBinding
import com.example.foodforfoodie.databinding.FragmentHomeBinding
import com.example.foodforfoodie.viewModel.HomeViewModel
import com.google.android.material.snackbar.Snackbar

class FavouriteFragment : Fragment(R.layout.fragment_favourite) {
    private lateinit var binding: FragmentFavouriteBinding
    private lateinit var viewModel: HomeViewModel
    private lateinit var favoriteAdapter: FavoriteMealAdapter

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        viewModel = (activity as MainActivity).viewModel
    }

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        binding = FragmentFavouriteBinding.inflate(inflater)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        prepareRecyclerView()

        observeFavorites()

        val itemTouchHelper = object : ItemTouchHelper.SimpleCallback(
            ItemTouchHelper.UP or ItemTouchHelper.DOWN,
            ItemTouchHelper.RIGHT or ItemTouchHelper.LEFT
        ) {
            override fun onMove(
                recyclerView: RecyclerView,
                viewHolder: RecyclerView.ViewHolder,
                target: RecyclerView.ViewHolder
            ) = true

            override fun onSwiped(viewHolder: RecyclerView.ViewHolder, direction: Int) {
                val position = viewHolder.adapterPosition

                if (position != RecyclerView.NO_POSITION && position < favoriteAdapter.differ.currentList.size) {
                    val deletedMeal = favoriteAdapter.differ.currentList[position]

                    viewModel.deleteMeal(deletedMeal)

                    Snackbar.make(requireView(), "Meal deleted", Snackbar.LENGTH_SHORT)
                        .setAction("Undo", View.OnClickListener {
                            viewModel.insertMeal(deletedMeal)
                        }).show()
                }
            }


        }
        ItemTouchHelper(itemTouchHelper).attachToRecyclerView(binding.recFavorite)


    }

    private fun prepareRecyclerView() {
        favoriteAdapter = FavoriteMealAdapter()
        binding.recFavorite.apply {
            layoutManager = GridLayoutManager(context, 2, GridLayoutManager.VERTICAL, false)
            adapter = favoriteAdapter
        }
    }

    private fun observeFavorites() {
        viewModel.observeFavoriteMealLiveData().observe(requireActivity(), Observer { meals ->
            favoriteAdapter.differ.submitList(meals)


        })
    }

}
