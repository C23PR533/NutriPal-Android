package com.example.nutripal.ui.trackingfood

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.example.nutripal.databinding.ItemTrackingFoodBinding
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
            tvFoodName.text = "${position+1}. ${food.nama_makanan}"
            tvFoodCal.text = "${food.kalori}"
        }

    }
    class ViewHolder(val binding:ItemTrackingFoodBinding):RecyclerView.ViewHolder(binding.root)


}