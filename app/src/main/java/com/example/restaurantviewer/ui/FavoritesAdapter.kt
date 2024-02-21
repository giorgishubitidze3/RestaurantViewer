package com.example.restaurantviewer.ui

import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Button
import android.widget.ImageButton
import android.widget.ImageView
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import com.example.restaurantviewer.R
import com.example.restaurantviewer.data.RestaurantViewModel
import com.example.restaurantviewer.data.YelpRestaurant
import org.w3c.dom.Text

class FavoritesAdapter( var favList: List<YelpRestaurant>, val viewModel:RestaurantViewModel): RecyclerView.Adapter<FavoritesAdapter.FavoritesViewHolder>() {


    inner class FavoritesViewHolder(itemView: View): RecyclerView.ViewHolder(itemView){
        val deleteButton = itemView.findViewById<ImageButton>(R.id.fav_delete_button)
        val favName = itemView.findViewById<TextView>(R.id.fav_name_tv)
        val favRating = itemView.findViewById<TextView>(R.id.fav_rating_tv)
        val favImg = itemView.findViewById<ImageView>(R.id.fav_img)

    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): FavoritesViewHolder {
        val view = LayoutInflater.from(parent.context).inflate(R.layout.fav_item, parent, false)
        return FavoritesViewHolder(view)
    }

    override fun getItemCount(): Int {
        return favList.size
    }

    override fun onBindViewHolder(holder: FavoritesViewHolder, position: Int) {
       val favorite = favList[position]

        holder.favName.text =   favorite.name
        holder.favRating.text = favorite.rating.toString()

        Glide.with(holder.favImg)
            .load(favorite.image_url)
            .centerCrop()
            .into(holder.favImg)


        Log.d("ViewHolderFav","$favList")

        holder.deleteButton.setOnClickListener {
            viewModel.removeFromFavs(favorite)
        }
    }



    fun setData(newData: List<YelpRestaurant>) {
        favList = newData
        notifyDataSetChanged()
        Log.d("FavAdapterData","$newData")
        Log.d("FavAdapterData","$favList")
    }

}

