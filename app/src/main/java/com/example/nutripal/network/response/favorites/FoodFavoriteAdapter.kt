package com.example.nutripal.network.response.favorites

import android.content.Context
import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import com.example.nutripal.databinding.ItemRecomendationBinding
import com.example.nutripal.databinding.ItemSearchBinding
import com.example.nutripal.network.response.favorites.Data
import com.example.nutripal.network.response.food.Serving

class FoodFavoriteAdapter(
    private val context: Context,
    private val listFood:List<FavoriteFood>,
    private val listner:FavoriteFoodsListener):RecyclerView.Adapter<FoodFavoriteAdapter.ViewHolder>() {

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        val binding = ItemSearchBinding
            .inflate(LayoutInflater.from(parent.context), parent, false)
        return ViewHolder(binding)
    }

    override fun getItemCount()=listFood.size


    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        val food = listFood[position]
        val calori = food.calories

        holder.binding.apply {
            tvTitle.text = food.foodName
            tvCal.text = "${calori} kkal"
        }
        holder.itemView.setOnClickListener {
            listner.onKlik(food.foodId)
        }
    }
    class ViewHolder(val binding:ItemSearchBinding):RecyclerView.ViewHolder(binding.root)

    interface FavoriteFoodsListener{
        fun onKlik(id:String)
    }
}