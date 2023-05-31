package com.example.nutripal.ui.viemodel

import android.util.Log
import android.widget.Toast
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.nutripal.network.api.ApiConfig
import com.example.nutripal.network.dummmy.ResponseDataDiri
import com.example.nutripal.network.dummmy.ResponseUserPreference
import com.example.nutripal.network.response.ApiResult
import com.example.nutripal.network.response.ResponsStatus
import com.google.firebase.auth.FirebaseAuth
import kotlinx.coroutines.launch

class NutripalViewModel: ViewModel() {

    private val _userPrefernce = MutableLiveData<ApiResult<ResponseUserPreference>>()
    val userPreference : LiveData<ApiResult<ResponseUserPreference>> = _userPrefernce
    private val _dataDiri = MutableLiveData<ApiResult<ResponseDataDiri>>()
    val dataDiri : LiveData<ApiResult<ResponseDataDiri>> = _dataDiri
    private val _responRegister = MutableLiveData<ApiResult<ResponsStatus>>()
    val responRegister : LiveData<ApiResult<ResponsStatus>> =_responRegister

    init {
        getUserPreference()
        getDatadiri()
    }

    private fun getDatadiri(){
        viewModelScope.launch {
            _dataDiri.value = ApiResult.Loading
            try {
                val dataDiri = ResponseDataDiri(
                    "12-8-200",
                    "example@gmail.com",
                    "www.example.com/profile.jpg",
                    "Male",
                    "123",
                    "Akbar","12344567"
                )
                _dataDiri.value = ApiResult.Success(dataDiri)
            }catch (e:Exception){
                _dataDiri.value = ApiResult.Error(e.toString())
            }

        }
    }
    fun registerWithFirebase(auth:FirebaseAuth,nama:String,email:String,noHp:String,pwd:String) {
        _responRegister.value = ApiResult.Loading
        auth.createUserWithEmailAndPassword(email, pwd)
            .addOnCompleteListener {
                if (it.isSuccessful) {
                    viewModelScope.launch {
                        try {
                            val response = ApiConfig.getApiService().register(auth.currentUser?.uid.toString(),nama,noHp,email,"","","")
                            if (response.isSuccessful){
                                val result = response.body()
                                _responRegister.value = ApiResult.Success(result!!)
                            }else{
                               Log.e("REGISTER", response.message())
                            }
                        }catch (e:Exception){
                            _responRegister.value = ApiResult.Error(e.toString())
                        }
                    }
                }
            }
            .addOnFailureListener {
                Log.e("REGISTER", it.toString())
                _responRegister.value = ApiResult.Error(it.toString())
            }
    }
     fun register(idUser:String,nama:String,email:String,noHp:String){
        viewModelScope.launch {
            _responRegister.value = ApiResult.Loading
            try {
                val response = ApiConfig.getApiService().register(idUser,nama,noHp,email,"","","")
                if (response.isSuccessful){
                    val result = response.body()
                    _responRegister.value = ApiResult.Success(result!!)
                }else{
                    response.body()?.let { Log.e("REGISTER", it.message) }
                }

            }catch (e:Exception){
                _responRegister.value = ApiResult.Error(e.toString())
            }

        }
    }

    private fun getUserPreference(){
        viewModelScope.launch {
            _userPrefernce.value = ApiResult.Loading
            try {
                val data = ResponseUserPreference(
                    "1.2",
                    "12-8-2000",
                    listOf("Obesitas","Diabetes"),
                    listOf("Obesitas","Diabetes"),
                    "Male",
                    "0",
                    "168",
                    "123",
                    "60"
                )
                _userPrefernce.value = ApiResult.Success(data)
            }catch (e:Exception){
                _userPrefernce.value = ApiResult.Error(e.toString())
            }
        }
    }





}