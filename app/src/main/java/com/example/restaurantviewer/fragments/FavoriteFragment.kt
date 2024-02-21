package com.example.restaurantviewer.fragments

import android.os.Bundle
import android.util.Log
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.lifecycle.ViewModelProvider
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.example.restaurantviewer.R
import com.example.restaurantviewer.data.RestaurantViewModel
import com.example.restaurantviewer.data.YelpRestaurant
import com.example.restaurantviewer.ui.CardAdapter
import com.example.restaurantviewer.ui.FavoritesAdapter


class FavoriteFragment : Fragment() {
    private lateinit var viewModel: RestaurantViewModel

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        return inflater.inflate(R.layout.fragment_favorite, container, false)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        viewModel = ViewModelProvider(requireActivity())[RestaurantViewModel::class.java]

        var data : List<YelpRestaurant> = emptyList()
        val favAdapter = FavoritesAdapter(data, viewModel)

        viewModel.favoriteRestaurants.observe(viewLifecycleOwner) { newData ->
            data = newData
            favAdapter.setData(newData)
            favAdapter.notifyDataSetChanged()
            Log.d("FavData1-FavoriteFragment","${newData}")
            Log.d("FavData2-FavoriteFragment","${data}")
        }


        val favRecyclerView = view.findViewById<RecyclerView>(R.id.favorites_recycler_view)
        favRecyclerView.adapter = favAdapter


    }

}