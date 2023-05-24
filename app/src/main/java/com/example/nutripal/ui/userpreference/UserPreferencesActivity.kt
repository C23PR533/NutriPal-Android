package com.example.nutripal.ui.userpreference

import android.R
import android.content.Intent
import android.os.Bundle
import android.view.View
import android.widget.AdapterView
import android.widget.ArrayAdapter
import android.widget.CheckBox
import android.widget.RadioButton
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import com.example.nutripal.MainActivity
import com.example.nutripal.databinding.ActivityUserPreferencesBinding
import com.example.nutripal.savepreference.PreferenceUser
import com.example.nutripal.utils.Util.setupDatePicker

class UserPreferencesActivity : AppCompatActivity() {

    private lateinit var binding: ActivityUserPreferencesBinding
    val alergiList = ArrayList<String>()
    val favoritFoodList = ArrayList<String>()
    var goal = ""
    var level = ""

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityUserPreferencesBinding.inflate(layoutInflater)
        setContentView(binding.root)

        val pref = PreferenceUser(this)
        val dataPref = pref.getDataPreference()
        val goall = dataPref[0]
        if (!goall.equals("null",true)){
            startActivity(Intent(this,MainActivity::class.java))
        }

        setupSpinerWeightGoal()
        setupRadioGender()
        setupDatePicker(this,binding.date)
        setupSpinerLevelActivity()

        binding.apply {
            val listCbAlergi = listOf(
                cbDiabetes,cbAsamurat,cbObesitas
            )
            val listCbFoods = listOf(
                cbApple,cbBakso,cbMie,cbNasigoreng,cbRendang,cbSate
            )
            getCheckBoxAlergi(listCbAlergi)
            getCheckBoxFood(listCbFoods)

        }

        binding.btnSaveUserPreference.setOnClickListener {
            val radioGroup = binding.genderRadioGroup
            val selectedRadioButtonId = radioGroup.checkedRadioButtonId
            val selectedRadioButton = findViewById<RadioButton>(selectedRadioButtonId)
            val gender = selectedRadioButton.text.toString()
            if (binding.etHeight.text.isNullOrEmpty()){
                binding.etHeight.error = "Insert your height"
            }else if (binding.etWeight.text.isNullOrEmpty()){
                binding.etWeight.error = "Insert your weight"
            }else{
                pref.setDataPreference(
                    "123",
                    goal,
                    binding.etWeight.text.toString(),
                    binding.etHeight.text.toString(),
                    gender,
                    binding.date.text.toString(),
                    level
                )
                startActivity(Intent(this,MainActivity::class.java))
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
            Toast.makeText(applicationContext,selectedGender,Toast.LENGTH_LONG).show()
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




}