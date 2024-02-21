package com.example.restaurantviewer.ui

import android.content.Context
import android.graphics.drawable.Drawable
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.TextView
import android.widget.ToggleButton
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import com.example.restaurantviewer.R
import com.example.restaurantviewer.R.drawable.favorite_icon_on
import com.example.restaurantviewer.R.drawable.favorite_icon_off
import com.example.restaurantviewer.data.RestaurantViewModel
import com.example.restaurantviewer.data.YelpRestaurant

class CardAdapter(var restaurantList: List<YelpRestaurant>, val viewModel: RestaurantViewModel, var currentFavs: List<YelpRestaurant>, context: Context): RecyclerView.Adapter<CardAdapter.CardViewHolder>() {

    inner class CardViewHolder(itemView: View): RecyclerView.ViewHolder(itemView){
        val name= itemView.findViewById<TextView>(R.id.item_name)
        val rating = itemView.findViewById<TextView>(R.id.item_rating)
        val picture = itemView.findViewById<ImageView>(R.id.item_image)
        val favIcon = itemView.findViewById<ImageView>(R.id.fav_icon)
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): CardViewHolder {
        val view = LayoutInflater.from(parent.context).inflate(R.layout.item_card, parent, false)
        return CardViewHolder(view)
    }

    override fun getItemCount(): Int {
        return restaurantList.size
    }

    override fun onBindViewHolder(holder: CardViewHolder, position: Int) {
        var restaurant = restaurantList[position]

        holder.name.text = restaurant.name
        holder.rating.text = restaurant.rating.toString()

        Glide.with(holder.picture)
            .load(restaurant.image_url)
            .centerCrop()
            .into(holder.picture)

        if(restaurant.name?.toLowerCase() in currentFavs.map { it.name?.toLowerCase() }) {
            holder.favIcon.setImageResource(R.drawable.favorite_icon_on)
            Log.d("FavCheck-CardAdapter", "Restaurant is in the favorites")
        } else {
            holder.favIcon.setImageResource(R.drawable.favorite_icon_off)
            Log.d("FavCheck-CardAdapter", "Restaurant is not in the favorites")
            Log.d("FavCheck-CardAdapter", "$currentFavs")
        }


        holder.favIcon.setOnClickListener{
            viewModel.addToFavs(restaurant)
        }
    }


    fun setData(newData: List<YelpRestaurant>) {
        restaurantList = newData
        notifyDataSetChanged()

    }

    fun setFavData(newData: List<YelpRestaurant>){
        currentFavs = newData
        notifyDataSetChanged()
    }

}