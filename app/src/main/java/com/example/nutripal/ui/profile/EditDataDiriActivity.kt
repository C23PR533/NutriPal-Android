package com.example.nutripal.ui.profile


import android.annotation.SuppressLint
import android.content.Intent
import android.os.Bundle
import android.os.Handler
import android.os.Looper
import android.widget.RadioButton
import android.widget.Toast
import androidx.activity.viewModels
import androidx.appcompat.app.AppCompatActivity
import com.bumptech.glide.Glide
import com.example.nutripal.MainActivity
import com.example.nutripal.databinding.ActivityEditDataDiriBinding
import com.example.nutripal.network.response.ApiResult
import com.example.nutripal.network.response.datadiri.Data
import com.example.nutripal.savepreference.PreferenceUser
import com.example.nutripal.ui.viemodel.NutripalViewModel
import com.example.nutripal.utils.DialogUtil.showDialogSuccesError

class EditDataDiriActivity : AppCompatActivity() {

    private lateinit var binding: ActivityEditDataDiriBinding
    private val nutripalViewModel:NutripalViewModel by viewModels()

    @SuppressLint("CheckResult")
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityEditDataDiriBinding.inflate(layoutInflater)
        setContentView(binding.root)
        val pref = PreferenceUser(this)
        val token = pref.getToken().toString()
        val datadiri = pref.getDatadiri()
        var email = datadiri[1]
        var url = datadiri[5]

        nutripalViewModel.getDatadiri(token)
        nutripalViewModel.dataDiri.observe(this){datadiri->
            when(datadiri){
                is ApiResult.Loading->{

                }
                is ApiResult.Error->{

                }
                is ApiResult.Success->{
                    val result = datadiri.data.data
                    binding.apply {
                        etNama.setText(result.nama)
                        etNohp.setText(result.nomor_hp)
                        date.text = result.birthdate
                        email = result.email
                        url = result.foto_profile
                        if(result.gender=="Male"){
                            maleRadioButton.isChecked = true
                        }else {
                            maleRadioButton.isChecked = true
                        }
                        if (result.foto_profile.isNotEmpty()){
                            Glide.with(this@EditDataDiriActivity).load(result.foto_profile)
                        }

                    }
                }
            }
        }
        nutripalViewModel.responRegister.observe(this) { response ->
            when (response) {
                is ApiResult.Loading -> {

                }

                is ApiResult.Error -> {
                    Toast.makeText(this,"ERROR Edit",Toast.LENGTH_SHORT).show()
                    showDialogSuccesError("ERROR",this,"Gagal Edit Data","")
                }

                is ApiResult.Success -> {
                    Toast.makeText(this,"SUKSES Edit",Toast.LENGTH_SHORT).show()
                    showDialogSuccesError("SUKSES",this,"","Berhasil Edit Data")
                }
            }
        }

        binding.apply {
            ivBack.setOnClickListener {
                finish()
            }
            btnSave.setOnClickListener {

                val nama = etNama.text.toString()
                val noHp = etNohp.text.toString().trim()
                val radioGroup = genderRadioGroup
                val selectedRadioButtonId = radioGroup.checkedRadioButtonId
                val selectedRadioButton = findViewById<RadioButton>(selectedRadioButtonId)

                if (etNama.text.isNullOrEmpty()) {
                    etNama.error = "Wajib diisi"
                } else if (etNohp.text.isNullOrEmpty()) {
                    etNohp.error = "Wajib diisi"
                } else if (date.text.isNullOrEmpty()){
                    date.error = "Wajib diisi"
                }else if (selectedRadioButtonId==-1){
                    Toast.makeText(applicationContext,"Pilih Jenis Kelamin", Toast.LENGTH_SHORT).show()
                }else{
                    val date = date.text.toString()
                    val genderRadio = selectedRadioButton.text.toString()
                    val data= Data(date,email,url,genderRadio,token,nama,noHp)

                    nutripalViewModel.editDatari(data)

                }
            }
        }




    }
}