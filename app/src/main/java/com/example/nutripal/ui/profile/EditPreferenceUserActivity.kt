package com.example.nutripal.ui.profile


import android.R
import android.annotation.SuppressLint
import android.app.AlertDialog
import android.content.res.ColorStateList
import android.os.Bundle
import android.util.Log
import android.view.View
import android.widget.AdapterView
import android.widget.ArrayAdapter
import android.widget.CheckBox
import android.widget.RadioButton
import android.widget.SearchView
import android.widget.Toast
import androidx.activity.viewModels
import androidx.appcompat.app.AppCompatActivity
import androidx.core.content.ContextCompat
import androidx.recyclerview.widget.LinearLayoutManager
import com.example.nutripal.databinding.ActivityEditPreferenceUserBinding
import com.example.nutripal.network.response.ApiResult
import com.example.nutripal.network.response.search.Data
import com.example.nutripal.network.response.userpreference.ListUserPreferences
import com.example.nutripal.savepreference.PreferenceUser
import com.example.nutripal.ui.search.SearchFoodAdapter
import com.example.nutripal.ui.userpreference.CustomSpinerLevelActivity
import com.example.nutripal.ui.userpreference.SpinerItemLevelActivity
import com.example.nutripal.ui.userpreference.SpinerItemWeightGoal
import com.example.nutripal.ui.viemodel.NutripalViewModel
import com.example.nutripal.utils.Util
import com.google.android.material.chip.Chip

class EditPreferenceUserActivity : AppCompatActivity() {

    private lateinit var binding: ActivityEditPreferenceUserBinding

    private lateinit var builder: AlertDialog.Builder
    private lateinit var dialog: AlertDialog
    private val nutripalviewModel:NutripalViewModel by viewModels()
    private val alergiList = ArrayList<String>()
    private val favoritFoodList = ArrayList<String>()
    var goal = ""
    var level = ""
    var genderRadio=""



    @SuppressLint("CheckResult")
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityEditPreferenceUserBinding.inflate(layoutInflater)
        setContentView(binding.root)
        val pref = PreferenceUser(this)
        val token = pref.getToken().toString()
        val auth = pref.getTokenAuth().toString()
        setupSpinerWeightGoal()
        Util.setupDatePicker(this, binding.date)
        setupSpinerLevelActivity()
        setupDialogLoading()

        binding.etSearch.setOnQueryTextListener(object: SearchView.OnQueryTextListener{
            override fun onQueryTextSubmit(query: String?): Boolean {

                return false
            }

            override fun onQueryTextChange(newText: String?): Boolean {
                if (newText.isNullOrEmpty()|| newText.isEmpty()){
                    binding.rcListSearch.visibility = View.GONE
                    setupRcListSearch(emptyList())
                }else{
                    nutripalviewModel.getSearchFood(newText)
                    nutripalviewModel.searchFood.observe(this@EditPreferenceUserActivity){search->
                        when(search){
                            is ApiResult.Loading->{
                            }
                            is ApiResult.Error->{
                            }
                            is ApiResult.Success->{
                                binding.rcListSearch.visibility = View.VISIBLE
                                setupRcListSearch(search.data.data)
                            }
                        }
                    }
                }
                return true
            }
        })
        binding.etSearch.setOnCloseListener {
            binding.rcListSearch.visibility = View.GONE
            true
        }



        nutripalviewModel.getUserPreference(token)
        nutripalviewModel.userPreference.observe(this){userpreference->
            when(userpreference){
                is ApiResult.Loading->{
                    showDialogLoading(true)
                }
                is ApiResult.Error->{
                    showDialogLoading(false)
                }
                is ApiResult.Success->{
                    val result = userpreference.data.listUserPreferences
                    genderRadio = result.gender
                    when(result.goals){
                        "0"->{ binding.spinerWeight.setSelection(0)}
                        "500"->{  binding.spinerWeight.setSelection(1)}
                        else->{ binding.spinerWeight.setSelection(2)}
                    }
                    when(result.activityLevel){
                        "1.2"->{binding.spinerLevel.setSelection(0)}
                        "1.375"->{binding.spinerLevel.setSelection(1)}
                        "1.55"->{binding.spinerLevel.setSelection(2)}
                        "1.725"->{binding.spinerLevel.setSelection(3)}
                        "1.9"->{binding.spinerLevel.setSelection(4)}
                    }
                    if(result.gender=="Male"){
                        binding.maleRadioButton.isChecked = true
                    }else {
                        binding.maleRadioButton.isChecked = true
                    }
                    binding.apply {
                       date.text = result.birthdate
                        etHeight.setText(result.height)
                        etWeight.setText(result.weight)
                    }
                    setupcbDeseaseFromDb(result)
                    setupChipFavorite(result)
                    setupChipFavorite(result)
                    showDialogLoading(false)
                }
            }
        }
        nutripalviewModel.responPreference.observe(this){response->
            when(response){
                is ApiResult.Loading-> {
                    showDialogLoading(true)
                }
                is ApiResult.Error->{
                    Toast.makeText(this,"Error Edit \n${response.errorMessage}",Toast.LENGTH_SHORT).show()
                    showDialogLoading(false)
                }
                is ApiResult.Success-> {
                    Toast.makeText(this,"Sukses Edit",Toast.LENGTH_SHORT).show()
                    showDialogLoading(false)
                }
                }

        }

        binding.apply {
            toolbar.setNavigationOnClickListener {
                finish()
            }
            val radioGroup = genderRadioGroup


            btnSave.setOnClickListener {
                val selectedRadioButtonId = radioGroup.checkedRadioButtonId
                val selectedRadioButton = findViewById<RadioButton>(selectedRadioButtonId)
                val chip = getAllChipTexts()
                if (etHeight.text.isNullOrEmpty()){
                    etHeight.error = "Insert your height"
                }else if (etWeight.text.isNullOrEmpty()){
                    etWeight.error = "Insert your weight"
                }else if (date.text.isNullOrEmpty()){
                    Toast.makeText(applicationContext,"Isi Tanggal Lahir",Toast.LENGTH_SHORT).show()
                }else if (chip.size<5){
                    Toast.makeText(applicationContext,"Pilih Minimal 5 Makanan Favorite",Toast.LENGTH_SHORT).show()
                }else{
                    val height = etHeight.text.toString()
                    val weight = etWeight.text.toString()
                    val birthDateTv = date.text.toString()
                     genderRadio = selectedRadioButton.text.toString()
                    Log.e("AUTH",auth)
                    nutripalviewModel.editUserPreference(

                        token,
                        goal,
                        height,
                        weight,
                        genderRadio,
                        birthDateTv,
                        level,
                        alergiList,
                        chip
                    )
                }
            }

            val listCbAlergi = listOf(
                cbDiabetes,cbHipertensi,cbJantung,cbObesitas
            )

            getCheckBoxAlergi(listCbAlergi)


        }





    }

    private fun setupChipFavorite(result: ListUserPreferences) {
        val foodFav = result.favoriteFood
        for (i in foodFav.indices){
            addChip(foodFav[i])
        }
    }
    fun addChip(text: String) {
        val chip = Chip(this)
        chip.text = text
        chip.isCloseIconVisible = true
        chip.chipBackgroundColor = ColorStateList.valueOf(ContextCompat.getColor(this, R.color.white))
        chip.setTextColor(
            ContextCompat.getColor(this, R.color.black))

        chip.setOnCloseIconClickListener {
            // Menghapus Chip saat ikon close di klik
            binding.chipWrapper.removeView(chip)
        }

        binding.chipWrapper.addView(chip)
    }
    private fun getAllChipTexts(): List<String> {
        val chipTexts = mutableListOf<String>()

        val chipCount = binding.chipWrapper.childCount
        for (i in 0 until chipCount) {
            val chip = binding.chipWrapper.getChildAt(i) as Chip
            chipTexts.add(chip.text.toString())
        }

        return chipTexts
    }
    private fun setupRcListSearch(listSearch:List<com.example.nutripal.network.response.search.Data>){
        val adapter= SearchFoodAdapter(listSearch,object: SearchFoodAdapter.ListenerSearch{
            override fun onKlik(food: Data) {

                val listChip = getAllChipTexts()
                if (listChip.contains(food.foodName)){
                    Toast.makeText(this@EditPreferenceUserActivity,"Makanan ${food.foodName} sudah ada",Toast.LENGTH_SHORT).show()
                }else{
                    addChip(food.foodName)
                    Toast.makeText(this@EditPreferenceUserActivity,"add ${food.foodName}",Toast.LENGTH_SHORT).show()
                }


            }
        })
        binding.apply {
            rcListSearch.adapter = adapter
            rcListSearch.layoutManager = LinearLayoutManager(this@EditPreferenceUserActivity)
        }
    }


    private fun setupcbDeseaseFromDb(result:ListUserPreferences){
        binding.apply {
            val diseases = result.disease
            if ("Diabetes" in diseases) {
                cbDiabetes.isChecked = true
            }
            if ("Obesity" in diseases) {
                cbObesitas.isChecked = true
            }
            if ("Heart" in diseases) {
                cbJantung.isChecked = true
            }
            if ("Hypertension" in diseases) {
                cbHipertensi.isChecked = true
            }

        }
    }
    private fun getCheckBoxAlergi(cb:List<CheckBox>){
        for (i in cb.indices){
            cb[i].setOnCheckedChangeListener { _, isChecked ->
                if (isChecked){
                    val text = cb[i].text.toString()
                    alergiList.add(text)
                }else{
                    val text = cb[i].text.toString()
                    alergiList.remove(text)
                }
            }
        }
    }

    private fun setupSpinerLevelActivity() {
        val spinerItemLevelActivity= listOf(
            SpinerItemLevelActivity(1.2,"Sedentary|(tidak banyak bergerak)"),
            SpinerItemLevelActivity(1.375,"Lightly active|((aktivitas ringan seperti berjalan kaki))"),
            SpinerItemLevelActivity(1.55,"Moderately active|(aktivitas sedang seperti olahraga ringan)"),
            SpinerItemLevelActivity(1.725,"Very active|(aktivitas berat seperti olahraga intens)"),
            SpinerItemLevelActivity(1.9,"Super active|(aktivitas sangat berat seperti atlet)"),
        )

        val spinnerAdapter = CustomSpinerLevelActivity(this, spinerItemLevelActivity)
        binding.spinerLevel.adapter = spinnerAdapter
        binding.spinerLevel.onItemSelectedListener = object: AdapterView.OnItemSelectedListener{
            override fun onItemSelected(
                parent: AdapterView<*>?,
                view: View?,
                position: Int,
                id: Long
            ) {
                val selectedItem = parent?.getItemAtPosition(position) as SpinerItemLevelActivity
                level = selectedItem.value.toString()

            }

            override fun onNothingSelected(parent: AdapterView<*>?) {
                Toast.makeText(applicationContext,"Pilih dahulu", Toast.LENGTH_LONG).show()
            }
        }
    }

    private fun setupSpinerWeightGoal() {
        val spinerItemWeightGoal = listOf(
            SpinerItemWeightGoal(0,"Mempertahankan berat badan"),
            SpinerItemWeightGoal(500,"Menambah berat badan"),
            SpinerItemWeightGoal(-500,"Menurunkan berat badan"),
        )
        val spinnerAdapter = ArrayAdapter(this, R.layout.simple_spinner_item, spinerItemWeightGoal)
        spinnerAdapter.setDropDownViewResource(R.layout.simple_spinner_dropdown_item)
        binding.spinerWeight.adapter = spinnerAdapter
        binding.spinerWeight.onItemSelectedListener = object: AdapterView.OnItemSelectedListener{
            override fun onItemSelected(
                parent: AdapterView<*>?,
                view: View?,
                position: Int,
                id: Long
            ) {
                val selectedItem = parent?.getItemAtPosition(position) as SpinerItemWeightGoal
                goal = selectedItem.value.toString()
            }

            override fun onNothingSelected(parent: AdapterView<*>?) {
                Toast.makeText(applicationContext,"Pilih dahulu", Toast.LENGTH_LONG).show()
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
}