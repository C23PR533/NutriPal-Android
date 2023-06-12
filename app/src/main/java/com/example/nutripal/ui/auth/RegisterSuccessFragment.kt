package com.example.nutripal.ui.auth

import android.app.AlertDialog
import android.content.Intent
import android.os.Bundle
import android.os.Handler
import android.os.Looper
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Button
import android.widget.ImageView
import android.widget.TextView
import androidx.core.content.ContextCompat
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import androidx.lifecycle.viewModelScope
import androidx.navigation.fragment.findNavController
import com.example.nutripal.MainActivity
import com.example.nutripal.R
import com.example.nutripal.databinding.FragmentLoginBinding
import com.example.nutripal.databinding.FragmentRegisterSuccessBinding
import com.example.nutripal.network.response.ApiResult
import com.example.nutripal.savepreference.PreferenceUser
import com.example.nutripal.ui.userpreference.UserPreferencesActivity
import com.example.nutripal.ui.viemodel.NutripalViewModel
import com.example.nutripal.utils.DialogUtil
import com.google.firebase.auth.FirebaseAuth

class RegisterSuccessFragment : Fragment() {

    private var _binding: FragmentRegisterSuccessBinding? = null
    private val binding get() = _binding!!

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        _binding = FragmentRegisterSuccessBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        binding.button.setOnClickListener {
            findNavController().navigate(R.id.action_registerSuccessFragment_to_navigation_login)
        }

    }


}