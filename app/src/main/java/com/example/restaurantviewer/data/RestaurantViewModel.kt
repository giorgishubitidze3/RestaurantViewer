package com.example.restaurantviewer.data

import android.util.Log
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.restaurantviewer.network.RetrofitInstance
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import okhttp3.Dispatcher
import retrofit2.Call
import retrofit2.Response


class RestaurantViewModel: ViewModel() {

    private val _restaurantList: MutableLiveData<List<YelpRestaurant>> = MutableLiveData()
    val restaurantList: LiveData<List<YelpRestaurant>> = _restaurantList


    private val _favoriteRestaurants: MutableLiveData<List<YelpRestaurant>> = MutableLiveData(emptyList())
    val favoriteRestaurants: LiveData<List<YelpRestaurant>> = _favoriteRestaurants

    private val _isLoading = MutableLiveData<Boolean>(true)
    val isLoading: LiveData<Boolean> = _isLoading

    fun fetchData(city: String) {
        _isLoading.value = true
        viewModelScope.launch(Dispatchers.IO) {
            try {
                val response = RetrofitInstance.api.searchRestaurants(city)

                if (response.isSuccessful) {
                    val businesses = response.body()?.businesses
                    businesses?.let {
                        _restaurantList.postValue(it)
                    }
                    _isLoading.postValue(false)

                    Log.e("API_ViewModel", "Successfully fetched the data")
                    Log.e("API_ViewModel", "response: ${response.body()}")
                    Log.d("API_ViewModel", _restaurantList.value?.size.toString())
                } else {
                    Log.e("API_ViewModel", "Error: ${response.code()} - ${response.message()}")
                    _isLoading.postValue(false)
                }
            } catch (e: Exception) {
                _isLoading.postValue(false)
                Log.e("API_ViewModel", "Error fetching data: ${e.message}")
            }
        }
    }

    fun cancelLoading(){
        _isLoading.postValue(false)
    }


    fun addToFavs(restaurant: YelpRestaurant){
        val currentFavorites = _favoriteRestaurants.value ?: emptyList()
        val newFavorites = currentFavorites.toMutableList().apply {
            add(restaurant)
        }
        _favoriteRestaurants.postValue(newFavorites)
        Log.d("FavRestaurants_ViewModel","${_favoriteRestaurants.value}")
    }

    fun removeFromFavs(restaurant: YelpRestaurant){
        val currentFavorites = _favoriteRestaurants.value ?: emptyList()
        val newFavorites = currentFavorites.toMutableList().apply{
            remove(restaurant)
        }
        _favoriteRestaurants.postValue(newFavorites)
    }
}