package com.example.nutripal.ui.favorite

import android.content.Context
import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.example.nutripal.databinding.ItemFavoriteBinding
import com.example.nutripal.databinding.ItemSearchBinding
import com.example.nutripal.network.response.favorites.FavoriteFood

class FoodFavoriteAdapter(
    private val context: Context,
    private val listFood:List<String>,
    private val listner:FavoriteFoodsListener):RecyclerView.Adapter<FoodFavoriteAdapter.ViewHolder>() {

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        val binding = ItemFavoriteBinding
            .inflate(LayoutInflater.from(parent.context), parent, false)
        return ViewHolder(binding)
    }

    override fun getItemCount()=listFood.size


    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        val food = listFood[position]
        holder.binding.apply {
            tvTitle.text = food
            tvNo.text = "${position+1}"
        }
        holder.itemView.setOnClickListener {
            listner.onKlik(food)
        }
    }
    class ViewHolder(val binding:ItemFavoriteBinding):RecyclerView.ViewHolder(binding.root)

    interface FavoriteFoodsListener{
        fun onKlik(id:String)
    }
}