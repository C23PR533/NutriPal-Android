package com.example.nutripal.savepreference

import android.content.Context

class PreferenceUser(context: Context) {
    private val preference = context.getSharedPreferences(PREF_NAME,Context.MODE_PRIVATE)
    fun setDataPreference(goal:String,weight:String,height:String,gender:String,birthDate:String,level:String,desease:String,favorite:String){
        val edit = preference.edit()
        edit.putString(GOAL,goal)
        edit.putString(WEIGTH,weight)
        edit.putString(HEIGHT,height)
        edit.putString(GENDER,gender)
        edit.putString(BIRTHDATE,birthDate)
        edit.putString(LEVEL,level)
        edit.putString(FAVORITE,favorite)
        edit.putString(DESEASE,desease)
        edit.apply()
    }
    fun setDataDiri(nama:String,email:String,noHp:String,gender:String,birthDate:String,fotoProfile:String){
        val edit = preference.edit()
        edit.apply {
            putString(NAMA,nama)
            putString(EMAIL,email)
            putString(NOHP,noHp)
            putString(GENDER,gender)
            putString(BIRTHDATE,birthDate)
            putString(FOTO,fotoProfile)
        }
        edit.apply()
    }
    fun setToken(token:String){
        val edit = preference.edit()
        edit.putString(TOKEN,token)
        edit.apply()
    }
    fun getToken():String?{
        return preference.getString(TOKEN, null)
    }

    fun getDatadiri():List<String>{
        val list = arrayListOf<String>()
        list.add(  preference.getString(NAMA, null).toString())
        list.add(  preference.getString(EMAIL, null).toString())
        list.add(  preference.getString(NOHP, null).toString())
        list.add(  preference.getString(GENDER, null).toString())
        list.add(  preference.getString(BIRTHDATE, null).toString())
        list.add(  preference.getString(FOTO, null).toString())
        return list
    }

    fun clearDataPrefrence(){
        val edit = preference.edit().clear()
        edit.apply()
    }
    companion object {
        const val PREF_NAME = "login_pref"
        const val TOKEN = "token"
        const val GOAL = "goal"
        const val WEIGTH = "weight"
        const val HEIGHT = "height"
        const val GENDER = "gender"
        const val BIRTHDATE = "birthdate"
        const val LEVEL = "level"
        const val DESEASE = "desease"
        const val FAVORITE = "favorite"
        const val NAMA = "nama"
        const val NOHP = "nohp"
        const val EMAIL = "email"
        const val FOTO = "foto"
    }
}