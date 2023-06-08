package com.example.nutripal.ui.search

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.example.nutripal.databinding.ItemSearchBinding
import com.example.nutripal.network.response.search.Data

class SearchFoodAdapter(private val listFood:List<Data>, private val listner:ListenerSearch):RecyclerView.Adapter<SearchFoodAdapter.ViewHolder>() {

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        val binding = ItemSearchBinding
            .inflate(LayoutInflater.from(parent.context), parent, false)
        return ViewHolder(binding)
    }

    override fun getItemCount()=listFood.size


    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        val food = listFood[position]
        val serving = food.servings.serving[0]
        holder.binding.apply {
            tvTitle.text = food.foodName
            tvCal.text = "${serving.calories} kkal"
        }
        holder.itemView.setOnClickListener {
            listner.onKlik(food)
        }
    }
    class ViewHolder(val binding:ItemSearchBinding):RecyclerView.ViewHolder(binding.root)

    interface ListenerSearch{
        fun onKlik(food:Data)
    }
}