package com.example.nutripal


import android.content.Intent
import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import androidx.navigation.findNavController
import androidx.navigation.ui.setupWithNavController
import com.example.nutripal.databinding.ActivityMainBinding
import com.example.nutripal.savepreference.PreferenceUser
import com.example.nutripal.ui.auth.AuthActivity
import com.google.android.material.bottomnavigation.BottomNavigationView

class MainActivity : AppCompatActivity() {

    private lateinit var binding: ActivityMainBinding


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityMainBinding.inflate(layoutInflater)
        setContentView(binding.root)

        val pref = PreferenceUser(this)
        val token = pref.getToken()

        if (token.isNullOrEmpty()){
            startActivity(Intent(this,AuthActivity::class.java))
        }

        val navView: BottomNavigationView = binding.navView

        val navController = findNavController(R.id.nav_host_fragment_activity_main)

        navView.setupWithNavController(navController)
    }
}