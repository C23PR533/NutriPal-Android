package com.example.nutripal.ui.home

import android.content.Context
import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import com.example.nutripal.databinding.ItemRecomendationBinding
import com.example.nutripal.network.response.food.Data
import com.example.nutripal.network.response.food.Serving

class FoodRecomendationAdapter(
    private val context: Context,
    private val listFood:List<Data>,
    private val listner:Recomendation):RecyclerView.Adapter<FoodRecomendationAdapter.ViewHolder>() {

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        val binding = ItemRecomendationBinding
            .inflate(LayoutInflater.from(parent.context), parent, false)
        return ViewHolder(binding)
    }

    override fun getItemCount()=listFood.size


    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        val food = listFood[position]
        val serving: Serving = food.servings.serving[0]

        holder.binding.apply {
            Glide.with(context)
                .load(food.foodImg)
                .into(ivFood)

            tvTitle.text = food.foodName
            tvCal.text = "${serving.calories} kkal"
        }
        holder.itemView.setOnClickListener {
            listner.onKlik(food)
        }
    }
    class ViewHolder(val binding:ItemRecomendationBinding):RecyclerView.ViewHolder(binding.root)

    interface Recomendation{
        fun onKlik(food:Data)
    }
}