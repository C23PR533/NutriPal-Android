package com.example.nutripal.ui.detail


import android.R
import android.app.AlertDialog
import android.os.Bundle
import android.util.Log
import android.view.View
import android.widget.AdapterView
import android.widget.ArrayAdapter
import android.widget.LinearLayout
import android.widget.TextView
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

    private fun handleNullNutrition(nut:String?,textView:TextView,ll:LinearLayout,unit:String){
        if (nut.isNullOrEmpty()){
            ll.visibility = View.GONE
        }else{
            val result = decreaseZeroDecimal(nut)+unit
            textView.text = result
        }
    }
    private fun decreaseZeroDecimal(input:String):String{
        val value = input.toDoubleOrNull()
        return if (value != null) {
            val formatted = String.format("%.10f", value)
            formatted.trimEnd('0').removeSuffix(".")
        } else {
            input
        }
    }
    private fun decreaseZeroDecimal(input:String,unit:String):String{
        val value = input.toDoubleOrNull()
        return if (value != null) {
            val formatted = String.format("%.10f", value)
            formatted.trimEnd('0').removeSuffix(".")+unit
        } else {
            input
        }
    }
    private fun setupInformationNutirition(food: ResponseFoodId) {
        binding.apply {
            toolBar.title = food.listUserPreferences.foodName
            val serving = food.listUserPreferences.servings.serving[0]

            tvTopCaolorie.text = decreaseZeroDecimal(serving.calories,"kkal")
            tvTopKarb.text = decreaseZeroDecimal(serving.carbohydrate,"g")
            tvTopLemak.text = decreaseZeroDecimal(serving.fat,"g")
            tvTopProtein.text = decreaseZeroDecimal(serving.protein,"g")

            tvPorsi.text = serving.servingDescription
            tvBerat.text = decreaseZeroDecimal(serving.metricServingAmount,serving.metricServingUnit)

            handleNullNutrition(serving.calories,tvCalorie,llCalorie,"kkal")
            handleNullNutrition(serving.calcium,tvKalsium,llKalsium,serving.metricServingUnit)
            handleNullNutrition(serving.carbohydrate,tvKarb,llKarb,serving.metricServingUnit)
            handleNullNutrition(serving.protein,tvProtein,llProtein,serving.metricServingUnit)
            handleNullNutrition(serving.sugar,tvGula,llGula,serving.metricServingUnit)
            handleNullNutrition(serving.fiber,tvSerat,llSerat,serving.metricServingUnit)
            handleNullNutrition(serving.fat,tvLemak,llLemak,serving.metricServingUnit)
            handleNullNutrition(serving.saturatedFat,tvLemakJenuh,llLemakJenuh,serving.metricServingUnit)
            handleNullNutrition(serving.polyunsaturatedFat,tvLemakTakJenuhGanda,llLemakTakJenuhGanda,serving.metricServingUnit)
            handleNullNutrition(serving.monounsaturatedFat,tvLemakTakJenuhTunggal,llLemakTakJenuhTunggal,serving.metricServingUnit)
            handleNullNutrition(serving.transFat,tvLemakTrans,llLemakTrans,serving.metricServingUnit)
            handleNullNutrition(serving.potassium,tvKalium,llKalium,"mg")
            handleNullNutrition(serving.sodium,tvSodium,llSodium,"mg")
            handleNullNutrition(serving.cholesterol,tvKolestrol,llKolestrol,"mg")

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