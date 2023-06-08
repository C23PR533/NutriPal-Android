package com.example.nutripal.ui.profile

import android.content.Intent
import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.example.nutripal.databinding.ItemProfileBinding

class ProfileAdapter(private val listTitle:List<DataIntentProfile>, private val listner:ProfileListener):RecyclerView.Adapter<ProfileAdapter.ViewHolder>() {

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        val binding = ItemProfileBinding
            .inflate(LayoutInflater.from(parent.context), parent, false)
        return ViewHolder(binding)
    }

    override fun getItemCount()=listTitle.size


    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        val list = listTitle[position]

        holder.binding.apply {
            tvTitle.text = list.title
        }
        holder.itemView.setOnClickListener {
            listner.onKlik(list.intent)
        }
    }
    class ViewHolder(val binding:ItemProfileBinding):RecyclerView.ViewHolder(binding.root)

    interface ProfileListener{
        fun onKlik(intent: Intent)
    }
}