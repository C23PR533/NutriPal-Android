package com.example.nutripal.ui.detail


import android.R
import android.app.AlertDialog
import android.app.DatePickerDialog
import android.content.Intent
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
import androidx.core.content.ContentProviderCompat.requireContext
import com.example.nutripal.MainActivity
import com.example.nutripal.databinding.ActivityDetailBinding
import com.example.nutripal.network.response.ApiResult
import com.example.nutripal.network.response.foodid.ResponseFoodId
import com.example.nutripal.network.response.historiaktifitas.KaloriMasuk
import com.example.nutripal.savepreference.PreferenceUser
import com.example.nutripal.ui.viemodel.NutripalViewModel
import com.example.nutripal.utils.Util.getCalories
import com.example.nutripal.utils.Util.setupDatePicker
import java.text.SimpleDateFormat
import java.util.Calendar
import java.util.Date
import java.util.Locale

class DetailActivity : AppCompatActivity() {

    private lateinit var builder: AlertDialog.Builder
    private lateinit var dialog: AlertDialog
    private lateinit var binding: ActivityDetailBinding
    private val nutripalViewModel:NutripalViewModel by viewModels()
    private var waktu = ""
    lateinit var foodId:ResponseFoodId
    private var calorie:Double = 0.0
    private var token = ""
    private var kaloriTercapai=""
    private var kaloriTersisa=""



    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityDetailBinding.inflate(layoutInflater)
        setContentView(binding.root)
        val pref = PreferenceUser(this)
         token = pref.getToken().toString()

        setupDatePicker()
        setupSpinerMakan()
        setupDialogLoading()
        val foodIdReceive = intent.getStringExtra("DATA")
        nutripalViewModel.getFoodId(foodIdReceive.toString())
        nutripalViewModel.responRegister.observe(this){response->
            when(response){
                is ApiResult.Loading->{
                    showDialogLoading(true)
                }
                is ApiResult.Error->{
                    showDialogLoading(false)
                    Log.e("ERROR",response.errorMessage)
                }
                is ApiResult.Success->{
                    val intent = Intent(this,MainActivity::class.java)
                    startActivity(intent)
                    showDialogLoading(false)

                }
            }
        }
        nutripalViewModel.getUserPreference(token)
        nutripalViewModel.userPreference.observe(this) { preference ->
            when (preference) {
                is ApiResult.Success -> {
                    calorie = getCalories(preference.data.listUserPreferences)
                    showDialogLoading(false)
                }

                is ApiResult.Loading -> {
                    showDialogLoading(true)
                }

                is ApiResult.Error -> {
                    showDialogLoading(false)
                }
            }
        }
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
                    foodId = food.data
                    nutripalViewModel.history.observe(this){history->
                        when(history){
                            is ApiResult.Loading->{
                                Toast.makeText(this,"LOADING",Toast.LENGTH_LONG).show()
                            }
                            is ApiResult.Error->{
                                kaloriTercapai=food.data.listUserPreferences.servings.serving[0].calories
                                val result = calorie-food.data.listUserPreferences.servings.serving[0].calories.toInt()
                                kaloriTersisa=result.toInt().toString()
                            }
                            is ApiResult.Success->{
                                val listKalori = history.data.History[0].aktifitas.kalori_masuk
                                kaloriTercapai=hitungJumlahKaloriTercapai(listKalori).toString()
                                kaloriTersisa=(calorie-hitungJumlahKaloriTercapai(listKalori)).toInt().toString()
                            }
                        }
                    }
                }
            }
        }




        binding.apply {
            btnSave.setOnClickListener {
                val food = foodId.listUserPreferences
                uploadDataHistoryAktifitas(token,
                    tvDateDetail.text.toString(),
                    calorie.toInt(),
                    food.foodId,
                    food.foodName,
                    food.servings.serving[0].calories,
                    waktu)
            }
        }





    }









    private fun hitungJumlahKaloriTercapai(listKalori:List<KaloriMasuk>):Int{
        var result = 0
        for (i in listKalori.indices){
            result+=listKalori[i].kalori.toInt()
        }
        Log.e("TERCAPAI","$result"
        )
        return result
    }
    private fun setupDatePicker() {
        val currentDate = Date()
        val dateFormat = SimpleDateFormat("dd-MM-yyyy", Locale.getDefault())
        val now=  dateFormat.format(currentDate)
        val cal = Calendar.getInstance()
        binding.tvDateDetail.text = now
        nutripalViewModel.getHistoryAktifitas(token,now)
        val dateSetListener = DatePickerDialog.OnDateSetListener { _, year, monthOfYear, dayOfMonth ->
            cal.set(Calendar.YEAR, year)
            cal.set(Calendar.MONTH, monthOfYear)
            cal.set(Calendar.DAY_OF_MONTH, dayOfMonth)
            val myFormat = "dd-MM-yyyy" // mention the format you need
            val sdf = SimpleDateFormat(myFormat, Locale.US)
            binding.tvDateDetail.text = sdf.format(cal.time)
            nutripalViewModel.getHistoryAktifitas(token,sdf.format(cal.time))
        }
        binding.tvDateDetail.setOnClickListener {
            DatePickerDialog(this, dateSetListener,
                cal.get(Calendar.YEAR),
                cal.get(Calendar.MONTH),
                cal.get(Calendar.DAY_OF_MONTH)).show()
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
            try {
                toolBar.title = food.listUserPreferences.foodName
                val serving = food.listUserPreferences.servings.serving[0]
                tvTopCaolorie.text = decreaseZeroDecimal(serving.calories, "kkal")
                tvTopKarb.text = decreaseZeroDecimal(serving.carbohydrate, "g")
                tvTopLemak.text = decreaseZeroDecimal(serving.fat, "g")
                tvTopProtein.text = decreaseZeroDecimal(serving.protein, "g")
                tvPorsi.text = serving.servingDescription
                tvBerat.text =
                    decreaseZeroDecimal(serving.metricServingAmount, serving.metricServingUnit)
                handleNullNutrition(serving.calories, tvCalorie, llCalorie, "kkal")
                handleNullNutrition(
                    serving.calcium,
                    tvKalsium,
                    llKalsium,
                    serving.metricServingUnit
                )
                handleNullNutrition(serving.carbohydrate, tvKarb, llKarb, serving.metricServingUnit)
                handleNullNutrition(
                    serving.protein,
                    tvProtein,
                    llProtein,
                    serving.metricServingUnit
                )
                handleNullNutrition(serving.sugar, tvGula, llGula, serving.metricServingUnit)
                handleNullNutrition(serving.fiber, tvSerat, llSerat, serving.metricServingUnit)
                handleNullNutrition(serving.fat, tvLemak, llLemak, serving.metricServingUnit)
                handleNullNutrition(
                    serving.saturatedFat,
                    tvLemakJenuh,
                    llLemakJenuh,
                    serving.metricServingUnit
                )
                handleNullNutrition(
                    serving.polyunsaturatedFat,
                    tvLemakTakJenuhGanda,
                    llLemakTakJenuhGanda,
                    serving.metricServingUnit
                )
                handleNullNutrition(
                    serving.monounsaturatedFat,
                    tvLemakTakJenuhTunggal,
                    llLemakTakJenuhTunggal,
                    serving.metricServingUnit
                )
                handleNullNutrition(
                    serving.transFat,
                    tvLemakTrans,
                    llLemakTrans,
                    serving.metricServingUnit
                )
                handleNullNutrition(serving.potassium, tvKalium, llKalium, "mg")
                handleNullNutrition(serving.sodium, tvSodium, llSodium, "mg")
                handleNullNutrition(serving.cholesterol, tvKolestrol, llKolestrol, "mg")
            }catch (e:Exception){
                Log.e("FOODID",e.toString())
            }
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
                waktu=selectedItem
            }
            override fun onNothingSelected(parent: AdapterView<*>?) {
                Toast.makeText(applicationContext,"Pilih dahulu", Toast.LENGTH_LONG).show()
            }
        }
    }

    private fun uploadDataHistoryAktifitas(iduser:String,tanggal:String,kaloriHarian:Int,idMakanan:String,namaMakanan:String,kalori:String,waktu:String){
        nutripalViewModel.postHistoryAktifitas(iduser,tanggal,kaloriHarian,idMakanan,namaMakanan,kalori,waktu)
    }
}