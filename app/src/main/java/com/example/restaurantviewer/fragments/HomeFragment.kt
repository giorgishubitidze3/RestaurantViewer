package com.example.restaurantviewer.fragments

import android.content.Context.MODE_PRIVATE
import android.os.Bundle
import android.util.Log
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.view.animation.AccelerateDecelerateInterpolator
import android.view.animation.AccelerateInterpolator
import android.widget.Button
import androidx.lifecycle.ViewModelProvider
import androidx.recyclerview.widget.RecyclerView
import com.example.restaurantviewer.R
import com.example.restaurantviewer.data.RestaurantViewModel
import com.example.restaurantviewer.data.YelpRestaurant
import com.example.restaurantviewer.ui.CardAdapter
import com.yuyakaido.android.cardstackview.CardStackLayoutManager
import com.yuyakaido.android.cardstackview.CardStackView
import com.yuyakaido.android.cardstackview.Direction
import com.yuyakaido.android.cardstackview.Duration
import com.yuyakaido.android.cardstackview.RewindAnimationSetting
import com.yuyakaido.android.cardstackview.StackFrom
import com.yuyakaido.android.cardstackview.SwipeAnimationSetting

import com.yuyakaido.android.cardstackview.SwipeableMethod


class HomeFragment : Fragment() {

    private lateinit var viewModel: RestaurantViewModel

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        // Inflate the layout for this fragment
        return inflater.inflate(R.layout.fragment_home, container, false)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        viewModel = ViewModelProvider(requireActivity())[RestaurantViewModel::class.java]

        val nextButton = view.findViewById<Button>(R.id.buttonNext)
        val backButton = view.findViewById<Button>(R.id.buttonBack)

        var data : List<YelpRestaurant> = emptyList()
        var favData: List<YelpRestaurant> = emptyList()

        val cardAdapter = CardAdapter(data, viewModel,favData, requireContext())
        viewModel.restaurantList.observe(viewLifecycleOwner){ newData ->
            data = newData
            cardAdapter.setData(newData)
            cardAdapter.notifyDataSetChanged()
        }

        viewModel.favoriteRestaurants.observe(viewLifecycleOwner){ newData ->
            favData = newData
            cardAdapter.setFavData(newData)
            cardAdapter.notifyDataSetChanged()
        }

        val layoutManager= CardStackLayoutManager(requireContext()).apply {
            setDirections(Direction.HORIZONTAL)
            setSwipeableMethod(SwipeableMethod.AutomaticAndManual)
        }


        val cardStackView = view.findViewById<CardStackView>(R.id.card_stack_view)

        cardStackView.adapter = cardAdapter
        cardStackView.layoutManager = layoutManager



        backButton.setOnClickListener {
            val setting = RewindAnimationSetting.Builder()
                .setDirection(Direction.Left)
                .setDuration(Duration.Normal.duration)
                .build()
            layoutManager.setRewindAnimationSetting(setting)
            cardStackView.rewind()
        }

        nextButton.setOnClickListener {
            val setting = SwipeAnimationSetting.Builder()
                .setDirection(Direction.Left)
                .setDuration(Duration.Normal.duration)
                .setInterpolator(AccelerateInterpolator())
                .build()
            layoutManager.setSwipeAnimationSetting(setting)
            cardStackView.swipe()
        }



    }


}