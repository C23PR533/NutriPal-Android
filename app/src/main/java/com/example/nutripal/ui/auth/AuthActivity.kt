package com.example.nutripal.ui.auth

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import androidx.navigation.fragment.NavHostFragment
import com.example.nutripal.R

class AuthActivity : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_auth)
        val navHostFragment = supportFragmentManager.findFragmentById(R.id.nav_host_fragment_activity_auth) as NavHostFragment

//        val navController = findNavController(R.id.nav_host_fragment_activity_main)
        val navController = navHostFragment.navController
    }
}