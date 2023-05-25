package com.example.nutripal.ui.viemodel

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.nutripal.network.dummmy.ResponseDataDiri
import com.example.nutripal.network.dummmy.ResponseUserPreference
import com.example.nutripal.network.response.ApiResult
import kotlinx.coroutines.launch

class NutripalViewModel: ViewModel() {

    private val _userPrefernce = MutableLiveData<ApiResult<ResponseUserPreference>>()
    val userPreference : LiveData<ApiResult<ResponseUserPreference>> = _userPrefernce
    private val _dataDiri = MutableLiveData<ApiResult<ResponseDataDiri>>()
    val dataDiri : LiveData<ApiResult<ResponseDataDiri>> = _dataDiri

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