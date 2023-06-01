package com.example.nutripal.ui.home

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.example.nutripal.databinding.ItemRecomendationBinding
import com.example.nutripal.network.response.foods.ResponseFoodsItem
import com.example.nutripal.network.response.foods.Serving

class FoodRecomendationAdapter(private val listFood:List<ResponseFoodsItem>, private val listner:Recomendation):RecyclerView.Adapter<FoodRecomendationAdapter.ViewHolder>() {

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
            tvTitle.text = food.food_name
            tvCal.text = "${serving.calories} kkal"
        }
        holder.itemView.setOnClickListener {
            listner.onKlik()
        }
    }
    class ViewHolder(val binding:ItemRecomendationBinding):RecyclerView.ViewHolder(binding.root)

    interface Recomendation{
        fun onKlik()
    }
}