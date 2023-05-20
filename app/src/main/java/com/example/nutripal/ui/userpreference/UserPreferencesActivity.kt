package com.example.nutripal.ui.userpreference

import android.content.Intent
import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import com.example.nutripal.MainActivity
import com.example.nutripal.databinding.ActivityUserPreferencesBinding

class UserPreferencesActivity : AppCompatActivity() {

    private lateinit var binding: ActivityUserPreferencesBinding

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityUserPreferencesBinding.inflate(layoutInflater)
        setContentView(binding.root)

        binding.btnSaveUserPreference.setOnClickListener {
            startActivity(Intent(this,MainActivity::class.java))
        }


    }
}