package com.example.nutripal.ui.userpreference


import android.R
import android.app.AlertDialog
import android.content.Intent
import android.os.Bundle
import android.os.Handler
import android.os.Looper
import android.view.View
import android.widget.AdapterView
import android.widget.ArrayAdapter
import android.widget.CheckBox
import android.widget.RadioButton
import android.widget.Toast
import androidx.activity.viewModels
import androidx.appcompat.app.AppCompatActivity
import com.example.nutripal.MainActivity
import android.R
import android.annotation.SuppressLint
import android.content.res.ColorStateList
import android.graphics.Color
import android.util.Log
import android.widget.SearchView
import androidx.core.content.ContextCompat
import androidx.recyclerview.widget.LinearLayoutManager
import com.example.nutripal.databinding.ActivityUserPreferencesBinding
import com.example.nutripal.network.response.ApiResult
import com.example.nutripal.network.response.datadiri.Data
import com.example.nutripal.savepreference.PreferenceUser
import com.example.nutripal.ui.detail.DetailActivity
import com.example.nutripal.ui.search.SearchFoodAdapter
import com.example.nutripal.ui.viemodel.NutripalViewModel
import com.example.nutripal.utils.DialogUtil.showDialogSuccesError
import com.example.nutripal.utils.Util.setupDatePicker
import com.google.android.material.chip.Chip

class UserPreferencesActivity : AppCompatActivity() {
    private lateinit var binding: ActivityUserPreferencesBinding
    companion object{
        const val SUCCESS = "SUCCESS"
        const val ERROR = "ERROR"
    }
    private lateinit var builder:AlertDialog.Builder
    private lateinit var dialog:AlertDialog
    private val viewModel:NutripalViewModel by viewModels()

    private val alergiList = ArrayList<String>()
    private val favoritFoodList = ArrayList<String>()
    var goal = ""
    var level = ""

    var idUser = ""
    var nama = ""
    var noHp = ""
    var email = ""
    var gender = ""
    var birthDate = ""

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityUserPreferencesBinding.inflate(layoutInflater)
        setContentView(binding.root)
        setupSpinerWeightGoal()
        setupRadioGender()
        setupDatePicker(this,binding.date)
        setupSpinerLevelActivity()
        setupDialogLoading()

        val pref = PreferenceUser(this)
        val token = pref.getToken().toString()
        val auth = pref.getTokenAuth().toString()


        binding.etSearch.setOnQueryTextListener(object: SearchView.OnQueryTextListener{
            override fun onQueryTextSubmit(query: String?): Boolean {

                return false
            }

            override fun onQueryTextChange(newText: String?): Boolean {
                if (newText.isNullOrEmpty()|| newText.isEmpty()){
                    binding.rcListSearch.visibility = View.GONE
                    setupRcListSearch(emptyList())
                }else{
                    viewModel.getSearchFood(newText)
                    viewModel.searchFood.observe(this@UserPreferencesActivity){search->
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


        viewModel.getDatadiri(token)
        viewModel.dataDiri.observe(this){dataDiri->
            when(dataDiri){
                is ApiResult.Loading->{
                    showDialogLoading(true)
                }
                is ApiResult.Error->{
                    showDialogLoading(false)
                }
                is ApiResult.Success->{
                    showDialogLoading(false)
                   dataDiri.data.apply {
                       idUser = data.id_user
                       nama = data.nama
                       noHp = data.nomor_hp
                       email = data.email
                   }
                }
            }
        }
        viewModel.responPreference.observe(this){response->
            when(response){
                is ApiResult.Loading->{
                    showDialogLoading(true)
                }
                is ApiResult.Error->{
                    showDialogLoading(false)
                    showDialogSuccesError(ERROR,this,"Gagal menyimpan,\nSilahkan Ulangi !","")
                }
                is ApiResult.Success->{
                    Handler(Looper.getMainLooper()).postDelayed({
                        showDialogLoading(false)
                        showDialogSuccesError(SUCCESS,this,"","Berhasil Menyimpan")
                        val intent = Intent(this@UserPreferencesActivity,MainActivity::class.java)
                        intent.flags = Intent.FLAG_ACTIVITY_NEW_TASK or Intent.FLAG_ACTIVITY_CLEAR_TASK
                        startActivity(intent)
                    },2000)

                }
            }
        }


        binding.apply {
            val listCbAlergi = listOf(
                cbDiabetes,cbHipertensi,cbJantung,cbObesitas
            )
//            val listCbFoods = listOf(
//                cbAyam,cbGoreng,cbIkan,cbNasi,cbKue,cbTahu,cbSambal,cbBakar,cbMie,cbTelur,cbDaging,cbTumis,cbUdang,cbSayur,cbPisang,cbEs,cbSapi,cbPuding,cbBolu,cbBumbu
//            )
            getCheckBoxAlergi(listCbAlergi)
//            getCheckBoxFood(listCbFoods)

        }
        binding.apply {
            btnSaveUserPreference.setOnClickListener {
                val radioGroup = genderRadioGroup
                val selectedRadioButtonId = radioGroup.checkedRadioButtonId
                val selectedRadioButton = findViewById<RadioButton>(selectedRadioButtonId)
                val chip = getAllChipTexts()
                if (etHeight.text.isNullOrEmpty()){
                    etHeight.error = "Insert your height"
                }else if (etWeight.text.isNullOrEmpty()){
                    etWeight.error = "Insert your weight"
                }else if (selectedRadioButtonId==-1){
                    Toast.makeText(applicationContext,"Pilih Jenis Kelamin",Toast.LENGTH_SHORT).show()
                }else if (date.text.isNullOrEmpty()){
                    Toast.makeText(applicationContext,"Isi Tanggal Lahir",Toast.LENGTH_SHORT).show()
                }else if (chip.size<5){
                    Toast.makeText(applicationContext,"Pilih Minimal 5 Makanan Favorite",Toast.LENGTH_SHORT).show()
                }else if (chip.size>10){
                    Toast.makeText(applicationContext,"Pilih Maximal 10 Makanan Favorite",Toast.LENGTH_SHORT).show()
                }else{
                    val height = etHeight.text.toString()
                    val weight = etWeight.text.toString()
                    val genderRadio = selectedRadioButton.text.toString()
                    val birthDateTv = date.text.toString()
                    birthDate = birthDateTv
                    gender = genderRadio


                    val dataDiri = Data(birthDate,email,"",gender,idUser,nama,noHp)
                    viewModel.editDatari(dataDiri)
                    viewModel.postUserPreference(token,goal,height,weight,genderRadio,birthDateTv,level,alergiList,chip)

                }
            }
        }




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

    private fun setupRcListSearch(listSearch:List<com.example.nutripal.network.response.search.Data>){
        val adapter= SearchFoodAdapter(listSearch,object: SearchFoodAdapter.ListenerSearch{
            override fun onKlik(food: com.example.nutripal.network.response.search.Data) {

                val listChip = getAllChipTexts()
                if (listChip.contains(food.foodName)){
                    Toast.makeText(this@UserPreferencesActivity,"Makanan ${food.foodName} sudah ada",Toast.LENGTH_SHORT).show()
                }else{
                    addChip(food.foodName)
                    Toast.makeText(this@UserPreferencesActivity,"add ${food.foodName}",Toast.LENGTH_SHORT).show()
                }


            }
        })
        binding.apply {
            rcListSearch.adapter = adapter
            rcListSearch.layoutManager = LinearLayoutManager(this@UserPreferencesActivity)
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
    private fun getCheckBoxFood(cb:List<CheckBox>){
        for (i in cb.indices){
            cb[i].setOnCheckedChangeListener { _, isChecked ->
                if (isChecked){
                    val text = cb[i].text.toString()
                    favoritFoodList.add(text)
                }else{
                    val text = cb[i].text.toString()
                    favoritFoodList.remove(text)
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
        binding.spinerLevel.onItemSelectedListener = object:AdapterView.OnItemSelectedListener{
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
                Toast.makeText(applicationContext,"Pilih dahulu",Toast.LENGTH_LONG).show()
            }
        }
    }

    private fun setupRadioGender() {
        binding.genderRadioGroup.setOnCheckedChangeListener { group, checkedId ->
            val selectedRadioButton: RadioButton = findViewById(checkedId)
            val selectedGender = selectedRadioButton.text.toString()
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
        binding.spinerWeight.onItemSelectedListener = object:AdapterView.OnItemSelectedListener{
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
                Toast.makeText(applicationContext,"Pilih dahulu",Toast.LENGTH_LONG).show()
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