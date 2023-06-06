package com.example.nutripal.ui.detail


import android.app.AlertDialog
import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.AdapterView
import android.widget.ArrayAdapter
import android.widget.LinearLayout
import android.widget.TextView
import android.widget.Toast
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import androidx.navigation.fragment.findNavController
import com.example.nutripal.R
import com.example.nutripal.databinding.FragmentDetailfoodBinding
import com.example.nutripal.network.response.ApiResult
import com.example.nutripal.network.response.foodid.ResponseFoodId
import com.example.nutripal.savepreference.PreferenceUser
import com.example.nutripal.ui.viemodel.NutripalViewModel
import com.example.nutripal.utils.Util
import com.example.nutripal.utils.Util.setupDatePicker

class DetailFragment : Fragment() {

    private lateinit var builder: AlertDialog.Builder
    private lateinit var dialog: AlertDialog
    private val nutripalViewModel: NutripalViewModel by viewModels()
    private var waktu = ""
    lateinit var foodId: ResponseFoodId
    private var calorie:Double = 0.0
    private var kaloriMakanan = ""
    private var _binding: FragmentDetailfoodBinding? = null
    private val binding get() = _binding!!

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        _binding = FragmentDetailfoodBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        val pref = PreferenceUser(requireContext())
        val token = pref.getToken().toString()
        val idFood = arguments?.getString("foodId")

        nutripalViewModel.getUserPreference(token)
        nutripalViewModel.userPreference.observe(viewLifecycleOwner) { preference ->
            when (preference) {
                is ApiResult.Success -> {
                    calorie = Util.getCalories(preference.data.listUserPreferences)
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


        binding.apply {
            btnSave.setOnClickListener {
                val food = foodId.listUserPreferences
                uploadDataHistoryAktifitas(token,tvDateDetail.text.toString(),calorie.toInt().toString(),"0",food.foodId,food.foodName,food.servings.serving[0].calories,waktu)
            }
        }

        setupDatePicker(requireContext(), binding.tvDateDetail)
        setupSpinerMakan()
        setupDialogLoading()

        nutripalViewModel.getFoodId(idFood.toString())

        nutripalViewModel.food.observe(viewLifecycleOwner){food->
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
                }
            }
        }
        nutripalViewModel.responRegister.observe(viewLifecycleOwner){response->
            when(response){
                is ApiResult.Loading->{
                    showDialogLoading(true)
                }
                is ApiResult.Error->{
                    showDialogLoading(false)
                    Log.e("ERROR",response.errorMessage)
                }
                is ApiResult.Success->{
                    findNavController().navigate(R.id.action_navigation_detail_to_navigation_tracking_food)
                    showDialogLoading(false)

                }
            }
        }
    }


    private fun handleNullNutrition(nut:String?, textView: TextView, ll:LinearLayout, unit:String){
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
        builder = AlertDialog.Builder(requireContext())
        val view = layoutInflater.inflate(R.layout.custom_dialog_loading,null)
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
        val spinnerAdapter = ArrayAdapter(requireContext(), android.R.layout.simple_spinner_item, list)
        spinnerAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item)
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
                Toast.makeText(requireContext(),"Pilih dahulu", Toast.LENGTH_LONG).show()
            }
        }
    }

    private fun uploadDataHistoryAktifitas(iduser:String,tanggal:String,kaloriHarian:String,sisaKalori:String,idMakanan:String,namaMakanan:String,kalori:String,waktu:String){
//        nutripalViewModel.postHistoryAktifitas(iduser,tanggal,sisaKalori,kaloriHarian,idMakanan,namaMakanan,kalori,waktu)
    }



    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }
}