package com.example.nutripal.ui.detail


import android.R
import android.app.AlertDialog
import android.os.Bundle
import android.util.Log
import android.view.View
import android.widget.AdapterView
import android.widget.ArrayAdapter
import android.widget.Toast
import androidx.activity.viewModels
import androidx.appcompat.app.AppCompatActivity
import com.example.nutripal.databinding.ActivityDetailBinding
import com.example.nutripal.network.response.ApiResult
import com.example.nutripal.network.response.foodid.ResponseFoodId
import com.example.nutripal.ui.viemodel.NutripalViewModel
import com.example.nutripal.utils.Util.setupDatePicker

class DetailActivity : AppCompatActivity() {

    private lateinit var builder: AlertDialog.Builder
    private lateinit var dialog: AlertDialog
    private lateinit var binding: ActivityDetailBinding
    private val nutripalViewModel:NutripalViewModel by viewModels()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityDetailBinding.inflate(layoutInflater)
        setContentView(binding.root)

        setupDatePicker(this,binding.tvDateDetail)
        setupSpinerMakan()
        setupDialogLoading()
        val foodId = intent.getStringExtra("DATA")
        nutripalViewModel.getFoodId(foodId.toString())
        Log.e("FOOD ID",foodId.toString())

        nutripalViewModel.food.observe(this){food->
            when(food){
                is ApiResult.Loading->{
                    showDialogLoading(true)
                }
                is ApiResult.Error->{
                    showDialogLoading(false)
                }
                is ApiResult.Success->{
                    showDialogLoading(false)
                    setupInformationNutirition(food.data)
                }
            }
        }



    }
    private fun setupInformationNutirition(food: ResponseFoodId) {
        binding.apply {
            toolBar.title = food.listUserPreferences.foodName
            val serving = food.listUserPreferences.servings.serving[0]
            tvTopCaolorie.text = serving.calories
            tvTopKarb.text = serving.carbohydrate
            tvTopLemak.text = serving.fat
            tvTopProtein.text = serving.protein

            tvPorsi.text = serving.servingDescription
            tvBerat.text = serving.metricServingAmount+"g"


            tvCalorie.text = serving.calories
            tvKalsium.text = serving.calcium
            tvKarb.text = serving.carbohydrate

            tvGula.text = serving.sugar+"g"
            tvSerat.text = serving.fiber+"g"
            tvLemakJenuh.text = serving.saturatedFat+"g"
            tvLemak.text = serving.fat+"g"
            tvLemakTrans.text = serving.transFat+"g"
            tvLemakTakJenuhGanda.text = serving.polyunsaturatedFat+"g"
            tvLemakTakJenuhTunggal.text = serving.monounsaturatedFat+"g"
            tvKalium.text = serving.potassium+"mg"
            tvSodium.text = serving.sodium+"mg"
            tvKolestrol.text = serving.cholesterol+"mg"


        }
    }
    private fun setupDialogLoading(){
        builder = AlertDialog.Builder(this)
        val view = layoutInflater.inflate(com.example.nutripal.R.layout.custom_dialog_loading,null)
        builder.setView(view)
        dialog = builder.create()
    }

    private fun showDialogLoading(isLoading:Boolean) {
        if (isLoading){
            dialog.show()
        }else{
            dialog.dismiss()
        }
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