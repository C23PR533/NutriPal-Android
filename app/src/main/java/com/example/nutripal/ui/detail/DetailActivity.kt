package com.example.nutripal.ui.detail

import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import com.example.nutripal.databinding.ActivityDetailBinding

class DetailActivity : AppCompatActivity() {

    private lateinit var binding: ActivityDetailBinding

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityDetailBinding.inflate(layoutInflater)
        setContentView(binding.root)

        supportActionBar?.show()

    }
}