package com.example.nutripal.ui.viemodel

import android.util.Log
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.nutripal.network.api.ApiConfig
import com.example.nutripal.network.response.ApiResult
import com.example.nutripal.network.response.ResponsStatus
import com.example.nutripal.network.response.datadiri.Data
import com.example.nutripal.network.response.datadiri.ResponseDataDiri
import com.example.nutripal.network.response.favorites.ResponseFoodsFavorite
import com.example.nutripal.network.response.food.ResponseFoods
import com.example.nutripal.network.response.foodid.ResponseFoodId
import com.example.nutripal.network.response.historiaktifitas.ListHistoryActivity
import com.example.nutripal.network.response.search.ResponseSearch
import com.example.nutripal.network.response.userpreference.ResponseUserPreferences
import com.google.firebase.auth.FirebaseAuth
import kotlinx.coroutines.launch
import java.util.Timer
import java.util.TimerTask

class NutripalViewModel: ViewModel() {

    private val _userPrefernce = MutableLiveData<ApiResult<ResponseUserPreferences>>()
    val userPreference : LiveData<ApiResult<ResponseUserPreferences>> = _userPrefernce
    private val _dataDiri = MutableLiveData<ApiResult<ResponseDataDiri>>()
    val dataDiri : LiveData<ApiResult<ResponseDataDiri>> = _dataDiri
    private val _responRegister = MutableLiveData<ApiResult<ResponsStatus>>()
    val responRegister : LiveData<ApiResult<ResponsStatus>> =_responRegister
    private val _responseFoodFav = MutableLiveData<ApiResult<ResponsStatus>>()
    val responseFoodFav : LiveData<ApiResult<ResponsStatus>> =_responseFoodFav
    private val _responLogin = MutableLiveData<ApiResult<String>>()
    val responLogin : LiveData<ApiResult<String>> =_responLogin
    private val _listFood = MutableLiveData<ApiResult<ResponseFoods>>()
    val listFood : LiveData<ApiResult<ResponseFoods>> = _listFood
    private val _food = MutableLiveData<ApiResult<ResponseFoodId>>()
    val food : LiveData<ApiResult<ResponseFoodId>> = _food
    private val _searchFood = MutableLiveData<ApiResult<ResponseSearch>>()
    val searchFood : LiveData<ApiResult<ResponseSearch>> = _searchFood
    private val _history = MutableLiveData<ApiResult<ListHistoryActivity>>()
    val history : LiveData<ApiResult<ListHistoryActivity>> = _history
    private val _favoriteFoods = MutableLiveData<ApiResult<ResponseFoodsFavorite>>()
    val favoriteFoods : LiveData<ApiResult<ResponseFoodsFavorite>> = _favoriteFoods


    fun deleteFoodFavorite(id_user: String,idFood: String){
        viewModelScope.launch {
            _responseFoodFav.value = ApiResult.Loading
            try {
                val response = ApiConfig.getApiService().deleteFoodFav(id_user,idFood)
                if (response.isSuccessful){
                    _responseFoodFav.value = ApiResult.Success(response.body()!!)
                }else{
                    _responseFoodFav.value = ApiResult.Error(response.body()!!.message)
                }
            }catch (e:Exception){
                _responseFoodFav.value = ApiResult.Error(e.toString())
            }
        }
    }
    fun getlistFoodFavorite(id_user: String){
        viewModelScope.launch {
            _favoriteFoods.value = ApiResult.Loading
            try {
                val response = ApiConfig.getApiService().getListFoodFavorite(id_user)
                if (response.isSuccessful){
                    _favoriteFoods.value = ApiResult.Success(response.body()!!)
                }else{
                    _favoriteFoods.value = ApiResult.Error(response.body()!!.message)
                }
            }catch (e:Exception){
                _favoriteFoods.value = ApiResult.Error(e.toString())
            }
        }
    }
    fun getFoodFavorite(id_user: String,idFood: String){
        viewModelScope.launch {
            _favoriteFoods.value = ApiResult.Loading
            try {
                val response = ApiConfig.getApiService().getFoodFavorite(id_user,idFood)
                if (response.isSuccessful){
                    _favoriteFoods.value = ApiResult.Success(response.body()!!)
                }else{
                    _favoriteFoods.value = ApiResult.Error(response.body()!!.message)
                }
            }catch (e:Exception){
                _favoriteFoods.value = ApiResult.Error(e.toString())
            }
        }
    }
    fun postFoodFavorite(id_user: String,idFood:String,foodName:String,calorie:String){
        viewModelScope.launch {
            _responseFoodFav.value = ApiResult.Loading
            try {
                val response = ApiConfig.getApiService().postFavoriteFoods(id_user,idFood,foodName,calorie)
                if (response.isSuccessful){
                    _responseFoodFav.value = ApiResult.Success(response.body()!!)
                }else{
                    _responseFoodFav.value = ApiResult.Error(response.body()!!.message)
                }
            }catch (e:Exception){
                _responseFoodFav.value = ApiResult.Error(e.toString())
            }
        }
    }
    fun getHistoryAktifitas(id_user: String,tgl:String){
        viewModelScope.launch {
            _history.value = ApiResult.Loading
            try {
                val response = ApiConfig.getApiService().getHistoryAktifitas(id_user,tgl)
                if (response.isSuccessful){
                    if (!response.body()!!.error){
                        val result = response.body()!!.listHistoryActivity
                        _history.value = ApiResult.Success(result)
                    }else{
                        _history.value = ApiResult.Error(response.body()!!.message)
                    }

                }else{
                    _history.value = ApiResult.Error(response.body()!!.message)
                }

            }catch (e:Exception){
                _history.value = ApiResult.Error(e.toString())
            }


        }
    }
    fun postHistoryAktifitas(id_user:String, tanggal:String, kalori_harian:Int, id_makanan:String, nama_makanan:String, kalori:String,waktu:String
    ){
        viewModelScope.launch {
            _responRegister.value = ApiResult.Loading
            try {
                val response = ApiConfig.getApiService().postHistoryAktifitas(id_user,tanggal,kalori_harian,id_makanan,nama_makanan,kalori,waktu)
                if (response.isSuccessful){
                    val result = response.body()!!
                    _responRegister.value = ApiResult.Success(result)
                }else{
                    _responRegister.value = response.body()?.let { ApiResult.Error(it.message) }
                }
            }catch (e:Exception){
                _responRegister.value = ApiResult.Error(e.toString())
            }
        }
    }

    fun getSearchFood(food_name:String?){
        viewModelScope.launch {
            _searchFood.value = ApiResult.Loading
            try {
                val response = food_name?.let { ApiConfig.getApiService().getSearchFood(it) }
                if (response != null) {
                    if (response.isSuccessful){
                        val result = response.body()!!
                        _searchFood.value = ApiResult.Success(result)
                    }else{
                        _searchFood.value = ApiResult.Error(response.message())

                    }
                }
            }catch (e:Exception){
                _searchFood.value = ApiResult.Error(e.toString())
            }

        }
    }
    fun getFoodId(id:String){
        viewModelScope.launch {
            _food.value = ApiResult.Loading
            try {
                val response = ApiConfig.getApiService().getFoodbyId(id)
                if (response.isSuccessful){
                    val result = response.body()!!
                    _food.value = ApiResult.Success(result)
                }else{
                    _food.value = ApiResult.Error(response.message())
                }
            }catch (e:Exception){
                _food.value = ApiResult.Error(e.toString())
            }

        }
    }

      fun getListFoods(){
        viewModelScope.launch {
            _listFood.value = ApiResult.Loading
            try {
                val response = ApiConfig.getApiService().getListFood()
                if (response.isSuccessful){
                    val result = response.body()!!
                    _listFood.value = ApiResult.Success(result)
                }else{
                    _listFood.value = ApiResult.Error(response.message())
                }

            }catch (e:Exception){
                _listFood.value = ApiResult.Error(e.toString())
            }

        }
    }
     fun getDatadiri(idUser:String){
        viewModelScope.launch {
            _dataDiri.value = ApiResult.Loading
            try {
                val response = ApiConfig.getApiService().getDatadiri(idUser)
                if (response.isSuccessful){
                    val result = response.body()
                    _dataDiri.value = ApiResult.Success(result!!)
                }

            }catch (e:Exception){
                _dataDiri.value = ApiResult.Error(e.toString())
            }

        }
    }
    fun editDatari(data:Data){
        viewModelScope.launch {
            _responRegister.value = ApiResult.Loading
            try {
                val response = ApiConfig.getApiService().editDataDiri(
                    data.id_user,data.id_user,data.nama,data.nomor_hp,data.email,data.birthdate,data.gender,data.foto_profile
                )
                if (response.isSuccessful){
                    val result = response.body()
                    _responRegister.value = ApiResult.Success(result!!)
                }else{
                    _responRegister.value = ApiResult.Error(response.message())
                }

            }catch (e:Exception){
                _responRegister.value = ApiResult.Error(e.toString())
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
     fun loginWithFirebase(auth:FirebaseAuth, email:String, pwd:String) {
         _responLogin.value = ApiResult.Loading
        Timer().schedule(object : TimerTask(){
            override fun run() {
                auth.signInWithEmailAndPassword(email, pwd)
                    .addOnCompleteListener {
                        if (it.isSuccessful) {
                            viewModelScope.launch {
                                val id = auth.currentUser?.uid.toString()
                                _responLogin.value = ApiResult.Success(id)
                            }
                        }else{
                            Log.e("LOGIN", it.exception?.message.toString())
                            _responLogin.value = ApiResult.Error(it.toString())
                        }
                    }
                    .addOnFailureListener {
                        Log.e("LOGIN", it.toString())
                        _responLogin.value = ApiResult.Error(it.toString())
                    }
            }
        },2000)
    }

      fun getUserPreference(id_user:String){
        _userPrefernce.value = ApiResult.Loading
        viewModelScope.launch {
            try {
                val response = ApiConfig.getApiService().getUserPreferences(id_user)
                if (response.isSuccessful){
                    if (!response.body()!!.error){
                        val result = response.body()!!
                        _userPrefernce.value = ApiResult.Success(result)
                    }else{
                        _userPrefernce.value = ApiResult.Error(response.body()!!.message)
                    }

                }else{
                    _userPrefernce.value = ApiResult.Error(response.body()!!.message)
                }
            }catch (e:Exception){
                _userPrefernce.value = ApiResult.Error(e.toString())
            }
        }

    }

    fun postUserPreference(userId:String,goal:String,height:String,weight:String,gender:String,birthDate:String,activityLevel:String,disease:List<String>,favFood:List<String>){
        _responRegister.value = ApiResult.Loading
        viewModelScope.launch {
            val response = ApiConfig.getApiService().postUserPreferences(
                userId,goal,height,weight,birthDate,gender,activityLevel,disease,favFood
            )
            try {
                if (response.isSuccessful){
                    val result = response.body()!!
                    _responRegister.value = ApiResult.Success(result)
                }else{
                    _responRegister.value = response.body()?.let { ApiResult.Error(it.message) }
                }
            }catch (e:Exception){
                _responRegister.value = ApiResult.Error(e.toString())
            }

        }

    }





}