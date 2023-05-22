package com.example.nutripal.ui.userpreference

import android.R
import android.app.DatePickerDialog
import android.content.Intent
import android.os.Bundle
import android.util.Log
import android.view.View
import android.widget.AdapterView
import android.widget.ArrayAdapter
import android.widget.CheckBox
import android.widget.RadioButton
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import com.example.nutripal.MainActivity
import com.example.nutripal.databinding.ActivityUserPreferencesBinding
import com.example.nutripal.utils.Util.setupDatePicker
import java.text.SimpleDateFormat
import java.util.Calendar
import java.util.Locale

class UserPreferencesActivity : AppCompatActivity() {

    private lateinit var binding: ActivityUserPreferencesBinding
    val alergiList = ArrayList<String>()
    val favoritFoodList = ArrayList<String>()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityUserPreferencesBinding.inflate(layoutInflater)
        setContentView(binding.root)

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
            startActivity(Intent(this,MainActivity::class.java))

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
                val selectedValue = selectedItem.value
                val selectedText = selectedItem.text
                Toast.makeText(applicationContext,selectedValue.toString(),Toast.LENGTH_SHORT).show()
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
                val selectedValue = selectedItem.value
                val selectedText = selectedItem.text
                Toast.makeText(applicationContext,selectedText+selectedValue,Toast.LENGTH_LONG).show()
            }

            override fun onNothingSelected(parent: AdapterView<*>?) {
                Toast.makeText(applicationContext,"Pilih dahulu",Toast.LENGTH_LONG).show()
            }
        }
    }




}