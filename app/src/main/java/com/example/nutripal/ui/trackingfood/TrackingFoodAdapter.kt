package com.example.nutripal.ui.trackingfood

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.example.nutripal.databinding.ItemRecomendationBinding
import com.example.nutripal.databinding.ItemTrackingFoodBinding
import com.example.nutripal.network.response.food.ResponseFoodsItem
import com.example.nutripal.network.response.food.Serving
import com.example.nutripal.network.response.historiaktifitas.Aktifitas
import com.example.nutripal.network.response.historiaktifitas.KaloriMasuk

class TrackingFoodAdapter(private val listAktifitas:List<KaloriMasuk>):RecyclerView.Adapter<TrackingFoodAdapter.ViewHolder>() {
    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        val binding = ItemTrackingFoodBinding
            .inflate(LayoutInflater.from(parent.context), parent, false)
        return ViewHolder(binding)
    }

    override fun getItemCount()=listAktifitas.size


    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        val food = listAktifitas[position]

        holder.binding.apply {
            tvFoodName.text = food.nama_makanan
            tvFoodCal.text = "${food.kalori} kkal"
        }

    }
    class ViewHolder(val binding:ItemTrackingFoodBinding):RecyclerView.ViewHolder(binding.root)


}