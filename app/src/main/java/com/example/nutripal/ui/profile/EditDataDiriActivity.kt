package com.example.nutripal.ui.profile


import android.annotation.SuppressLint
import android.content.Intent
import android.content.pm.PackageManager
import android.net.Uri
import android.os.Bundle
import android.os.Handler
import android.os.Looper
import android.util.Log
import android.widget.RadioButton
import android.widget.Toast
import androidx.activity.result.contract.ActivityResultContracts
import androidx.activity.viewModels
import androidx.appcompat.app.AppCompatActivity
import androidx.core.app.ActivityCompat
import androidx.core.content.ContextCompat
import com.bumptech.glide.Glide
import com.example.nutripal.MainActivity

import com.example.nutripal.databinding.ActivityEditDataDiriBinding
import com.example.nutripal.network.response.ApiResult
import com.example.nutripal.network.response.datadiri.Data
import com.example.nutripal.savepreference.PreferenceUser
import com.example.nutripal.ui.viemodel.NutripalViewModel
import com.example.nutripal.utils.DialogUtil.showDialogSuccesError
import com.example.nutripal.utils.Util.uriToFile
import com.github.dhaval2404.imagepicker.ImagePicker
import okhttp3.MediaType
import okhttp3.MediaType.Companion.toMediaTypeOrNull
import okhttp3.MultipartBody
import okhttp3.RequestBody
import okhttp3.RequestBody.Companion.asRequestBody
import java.io.File

class EditDataDiriActivity : AppCompatActivity() {

    private lateinit var binding: ActivityEditDataDiriBinding
    private val nutripalViewModel:NutripalViewModel by viewModels()
    private var token = ""


    @SuppressLint("CheckResult")
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityEditDataDiriBinding.inflate(layoutInflater)
        setContentView(binding.root)
        val pref = PreferenceUser(this)
         token = pref.getToken().toString()
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

        nutripalViewModel.responseUpload.observe(this){upload->
            when(upload){
                is ApiResult.Loading->{

                }
                is ApiResult.Error->{
                    Log.e("FOTO",upload.errorMessage)
                }
                is ApiResult.Success->{
                    url = upload.data.url
                    Log.e("FOTO",url)
                }
            }
        }



        binding.apply {
            circleImageView2.setOnClickListener {
                getFoto()
            }
            toolbar.setNavigationOnClickListener {
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
    private fun getFoto(){
        ImagePicker.with(this)
            .createIntent { intent ->
                startForProfileImageResult.launch(intent)
            }
    }

    private val startForProfileImageResult = registerForActivityResult(ActivityResultContracts.StartActivityForResult()
    ) { result ->
            if (result.resultCode == RESULT_OK) {
                val selectedImg: Uri = result.data?.data as Uri
                binding.circleImageView2.setImageURI(selectedImg)
                val myFile = uriToFile(selectedImg, this)
                val requestFile = myFile.asRequestBody("image/*".toMediaTypeOrNull())
                val multipart = MultipartBody.Part.createFormData("photo",myFile.name,requestFile)
                nutripalViewModel.uploadFoto(token,multipart)
            }
        }


}