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
import com.example.nutripal.network.response.ApiResult
import com.example.nutripal.savepreference.PreferenceUser
import com.example.nutripal.ui.userpreference.UserPreferencesActivity
import com.example.nutripal.ui.viemodel.NutripalViewModel
import com.example.nutripal.utils.DialogUtil
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

//        viewModel.responLogin.observe(viewLifecycleOwner){response->
//            when(response){
//                is ApiResult.Loading->{
//                    showDialogLoading(true)
//                }
//                is ApiResult.Error->{
//                    Handler(Looper.getMainLooper()).postDelayed({
//                        showDialogLoading(false)
//                        DialogUtil.showDialogSuccesError(
//                            RegisterFragment.ERROR,
//                            requireContext(),
//                            "Login Failed",
//                            ""
//                        )
//                    },2000)
//                }
//                is ApiResult.Success->{
//                    Log.e("LOGIN",response.data)
//                    Handler(Looper.getMainLooper()).postDelayed({
//                        showDialogLoading(false)
//                        DialogUtil.showDialogSuccesError(
//                            RegisterFragment.SUCCESS,
//                            requireContext(),
//                            "",
//                            "LOGIN"
//                        )
//                        pref.setToken(response.data)
//                        viewModel.getUserPreference(response.data)
//                    },2000)
//
//                }
//            }
//        }

        viewModel.responseLoginAuth.observe(viewLifecycleOwner){login->
            when(login){
                is ApiResult.Loading->{
                    showDialogLoading(true)
                }
                is ApiResult.Error->{
                    Handler(Looper.getMainLooper()).postDelayed({
                        showDialogLoading(false)
                        DialogUtil.showDialogSuccesError(
                            RegisterFragment.ERROR,
                            requireContext(),
                            "Login Failed",
                            ""
                        )
                    },2000)
                }
                is ApiResult.Success->{
                    pref.setToken(login.data.data.uid)
                    pref.setTokenAuth(login.data.data.idToken)
                    val uid = pref.getToken().toString()
                    val auth = pref.getTokenAuth().toString()
                    Log.e("AUTH",login.data.data.uid)
                    Log.e("AUTH",login.data.data.idToken)
                    Log.e("AUTH",auth)
                    showDialogLoading(false)
                    viewModel.getUserPreference("Bearer ${auth}",uid)
                }
            }
        }

        viewModel.userPreference.observe(viewLifecycleOwner){response->
            when(response){
                is ApiResult.Loading->{
                    showDialogLoading(true)
                }
                is ApiResult.Error->{
                    Handler(Looper.getMainLooper()).postDelayed({
                        showDialogLoading(false)
                        startActivity(Intent(requireContext(),UserPreferencesActivity::class.java))
                    },2000)

                }
                is ApiResult.Success->{
                    Handler(Looper.getMainLooper()).postDelayed({
                        showDialogLoading(false)
                        val intent = Intent(requireContext(),MainActivity::class.java)
                        intent.flags = Intent.FLAG_ACTIVITY_NEW_TASK or Intent.FLAG_ACTIVITY_CLEAR_TASK
                        startActivity(intent)
                    },2000)

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
                    viewModel.login(email, pwd)
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



    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }
}