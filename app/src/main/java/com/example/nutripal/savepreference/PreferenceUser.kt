package com.example.nutripal.savepreference

import android.content.Context

class PreferenceUser(context: Context) {
    private val preference = context.getSharedPreferences(PREF_NAME,Context.MODE_PRIVATE)
//    fun setDataPreference(token:String,goal:String,weight:String,height:String,gender:String,birthDate:String,level:String){
//        val edit = preference.edit()
//        edit.putString(TOKEN,token)
//        edit.putString(GOAL,goal)
//        edit.putString(WEIGTH,weight)
//        edit.putString(HEIGHT,height)
//        edit.putString(GENDER,gender)
//        edit.putString(BIRTHDATE,birthDate)
//        edit.putString(LEVEL,level)
//        edit.apply()
//    }
    fun setToken(token:String){
        val edit = preference.edit()
        edit.putString(TOKEN,token)
        edit.apply()
    }
    fun getToken():String?{
        return preference.getString(TOKEN, null)
    }


    fun clearDataPrefrence(){
        val edit = preference.edit().clear()
        edit.apply()
    }
    companion object {
        const val PREF_NAME = "login_pref"
        const val TOKEN = "token"
    }
}