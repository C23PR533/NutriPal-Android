package com.example.nutripal.ui.profile


import android.annotation.SuppressLint
import android.app.AlertDialog
import android.net.Uri
import android.os.Bundle
import android.util.Log
import android.widget.RadioButton
import android.widget.Toast
import androidx.activity.result.contract.ActivityResultContracts
import androidx.activity.viewModels
import androidx.appcompat.app.AppCompatActivity
import com.bumptech.glide.Glide
import com.example.nutripal.R
import com.example.nutripal.databinding.ActivityEditDataDiriBinding
import com.example.nutripal.network.response.ApiResult
import com.example.nutripal.network.response.datadiri.Data
import com.example.nutripal.savepreference.PreferenceUser
import com.example.nutripal.ui.viemodel.NutripalViewModel
import com.example.nutripal.utils.DialogUtil.showDialogSuccesError
import com.example.nutripal.utils.Util.setupDatePicker
import com.example.nutripal.utils.Util.uriToFile
import com.github.dhaval2404.imagepicker.ImagePicker
import okhttp3.MediaType.Companion.toMediaTypeOrNull
import okhttp3.MultipartBody
import okhttp3.RequestBody.Companion.asRequestBody

class EditDataDiriActivity : AppCompatActivity() {

    private lateinit var binding: ActivityEditDataDiriBinding
    private val nutripalViewModel:NutripalViewModel by viewModels()
    private var token = ""
    private var url = ""
    private var urlBaru = ""
    private var email = ""
    private var isChange = false
    private lateinit var builder: AlertDialog.Builder
    private lateinit var dialog: AlertDialog


    @SuppressLint("CheckResult")
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityEditDataDiriBinding.inflate(layoutInflater)
        setContentView(binding.root)
        val pref = PreferenceUser(this)
         token = pref.getToken().toString()
        setupDatePicker(this,binding.date)
        setupDialogLoading()

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
                            Glide.with(this@EditDataDiriActivity)
                                .load(result.foto_profile)
                                .into(circleImageView2)
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
                showDialogLoading(true)
                }
                is ApiResult.Error->{
                    showDialogLoading(false)
                    Log.e("FOTO",upload.errorMessage)
                }
                is ApiResult.Success->{
                    urlBaru = upload.data.url
                    Log.e("FOTO",url)
                    isChange = true
                    showDialogLoading(false)

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
                    val image = if(isChange) urlBaru else url
                    val genderRadio = selectedRadioButton.text.toString()
                    val data= Data(date,email,image,genderRadio,token,nama,noHp)
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
    private fun setupDialogLoading(){
        builder = AlertDialog.Builder(this)
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

}