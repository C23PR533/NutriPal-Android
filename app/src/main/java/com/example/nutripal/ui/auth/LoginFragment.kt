package com.example.nutripal.ui.auth

import android.app.AlertDialog
import android.content.Intent
import android.os.Bundle
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
import com.example.nutripal.network.response.ApiResult
import com.example.nutripal.savepreference.PreferenceUser
import com.example.nutripal.ui.userpreference.UserPreferencesActivity
import com.example.nutripal.ui.viemodel.NutripalViewModel
import com.google.firebase.auth.FirebaseAuth

class LoginFragment : Fragment() {
    companion object{
        const val SUCCESS = "SUCCESS"
        const val ERROR = "ERROR"
    }
    private val viewModel: NutripalViewModel by viewModels()
    private lateinit var auth: FirebaseAuth
    private lateinit var builder:AlertDialog.Builder
    private lateinit var dialog:AlertDialog

    private var _binding: FragmentLoginBinding? = null
    private val binding get() = _binding!!

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        _binding = FragmentLoginBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        auth = FirebaseAuth.getInstance()
        setupDialogLoading()
        val pref = PreferenceUser(requireContext())

        viewModel.responLogin.observe(viewLifecycleOwner){response->
            when(response){
                is ApiResult.Loading->{
                    showDialogLoading(true)
                }
                is ApiResult.Error->{
                    showDialogLoading(false)
                    showDialogSuccesError(ERROR)
                }
                is ApiResult.Success->{

                    showDialogSuccesError(SUCCESS)
                    pref.setToken(response.data)
                }
            }
        }
        viewModel.userPreference.observe(viewLifecycleOwner){response->
            when(response){
                is ApiResult.Loading->{
                }
                is ApiResult.Error->{
                    showDialogLoading(false)
                    startActivity(Intent(requireContext(),UserPreferencesActivity::class.java))
                }
                is ApiResult.Success->{
                    showDialogLoading(false)
                    startActivity(Intent(requireContext(),MainActivity::class.java))
                }
            }
        }


        binding.apply {
            btnLogin.setOnClickListener {
                val email = etEmail.text.toString().trim()
                val pwd = etPwd.text.toString().trim()
                if (etEmail.text.isNullOrEmpty()) {
                    etEmail.error = "Wajib diisi"
                } else if (etPwd.text.isNullOrEmpty()) {
                    etPwd.error = "Wajib diisi"
                }  else if (etPwd.text.length < 8) {
                    etPwd.error = "Password minimal 8 karakter"
                } else {
                    viewModel.loginWithFirebase(auth,email, pwd)
                }
            }
        }


        binding.buttonToRegister.setOnClickListener {
            findNavController().navigate(R.id.action_navigation_login_to_navigation_register)
        }

    }

    private fun setupDialogLoading(){
        builder = AlertDialog.Builder(requireContext())
        val view = layoutInflater.inflate(R.layout.custom_dialog_loading,null)
        builder.setView(view)
        dialog = builder.create()
    }

    private fun showDialogLoading(isLoading:Boolean) {
        if (isLoading){
            dialog.show()
        }else{
            dialog.dismiss()
        }
    }

    private fun showDialogSuccesError(mode:String) {
        val builder = AlertDialog.Builder(activity)
        val view = layoutInflater.inflate(R.layout.custom_dialog_success, null)
        val tvPertama = view.findViewById<TextView>(R.id.tvPertama)
        val tvKedua = view.findViewById<TextView>(R.id.tvKedua)
        val ivDialog = view.findViewById<ImageView>(R.id.ivDialog)
        val close = view.findViewById<Button>(R.id.btn_close)

        if (mode == SUCCESS) {
            val title = RegisterFragment.SUCCESS
            val message = "Login berhasil"
            ivDialog.setImageDrawable(
                ContextCompat.getDrawable(
                    requireContext(),
                    R.drawable.undraw_completed_03xt
                )
            )
            tvPertama.text = title
            tvKedua.text = message
            builder.setView(view)
            val dia = builder.create()
            dia.show()
            close.setOnClickListener {
                dia.dismiss()
            }
        } else if (mode == ERROR) {
            val title = RegisterFragment.ERROR
            val message = "Login Gagal, coba lagi"
            ivDialog.setImageDrawable(
                ContextCompat.getDrawable(
                    requireContext(),
                    R.drawable.undraw_page_not_found_re_e9o6
                )
            )
            tvPertama.text = title
            tvKedua.text = message
            builder.setView(view)
            val dia = builder.create()
            dia.show()
            close.setOnClickListener {
                dia.dismiss()
            }
        }
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }
}