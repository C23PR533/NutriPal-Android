package com.example.nutripal.ui.detail

import android.R
import android.os.Bundle
import android.view.View
import android.widget.AdapterView
import android.widget.ArrayAdapter
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import com.example.nutripal.databinding.ActivityDetailBinding
import com.example.nutripal.ui.userpreference.SpinerItemWeightGoal
import com.example.nutripal.utils.Util.setupDatePicker

class DetailActivity : AppCompatActivity() {

    private lateinit var binding: ActivityDetailBinding

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityDetailBinding.inflate(layoutInflater)
        setContentView(binding.root)

        setupDatePicker(this,binding.tvDateDetail)
        setupSpinerMakan()

    }
    private fun setupSpinerMakan() {
        val list = listOf(
            "Sarapan","Makan Siang","Makan Malam","Cemilan"
        )
        val spinnerAdapter = ArrayAdapter(this, R.layout.simple_spinner_item, list)
        spinnerAdapter.setDropDownViewResource(R.layout.simple_spinner_dropdown_item)
        binding.spinerMakan.adapter = spinnerAdapter
        binding.spinerMakan.onItemSelectedListener = object: AdapterView.OnItemSelectedListener{
            override fun onItemSelected(
                parent: AdapterView<*>?,
                view: View?,
                position: Int,
                id: Long
            ) {
                val selectedItem = parent?.getItemAtPosition(position).toString()
                Toast.makeText(this@DetailActivity,selectedItem,Toast.LENGTH_SHORT).show()
            }

            override fun onNothingSelected(parent: AdapterView<*>?) {
                Toast.makeText(applicationContext,"Pilih dahulu", Toast.LENGTH_LONG).show()
            }
        }
    }
}