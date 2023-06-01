package com.example.nutripal.ui.auth

import android.app.AlertDialog
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
import androidx.navigation.fragment.findNavController
import com.example.nutripal.R
import com.example.nutripal.databinding.FragmentRegisterBinding
import com.example.nutripal.network.response.ApiResult
import com.example.nutripal.ui.viemodel.NutripalViewModel
import com.google.firebase.auth.FirebaseAuth
import kotlinx.coroutines.delay
import java.util.Timer
import java.util.TimerTask

class RegisterFragment : Fragment() {
    companion object{
        const val SUCCESS = "success"
        const val ERROR = "error"
    }
    private lateinit var auth: FirebaseAuth
    private var _binding: FragmentRegisterBinding? = null
    private val viewModel:NutripalViewModel by viewModels()
    private lateinit var builder:AlertDialog.Builder
    private lateinit var dialog:AlertDialog
    private val binding get() = _binding!!

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        _binding = FragmentRegisterBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        setupDialogLoading()

        auth = FirebaseAuth.getInstance()

        viewModel.responRegister.observe(viewLifecycleOwner){ response->
            when(response){
                is ApiResult.Loading->{
                    showDialogLoading(true)
                }
                is ApiResult.Error->{
                    showDialogLoading(false)
                    showDialogSuccesError(ERROR)
                }
                is ApiResult.Success->{
                    showDialogLoading(false)
                    showDialogSuccesError(SUCCESS)
                }
            }
        }

        binding.apply {
            btnRegister.setOnClickListener {
                val nama = etNama.text.toString()
                val noHp = etNohp.text.toString().trim()
                val email = etEmail.text.toString().trim()
                val pwd = etPwd.text.toString().trim()
                val cPwd = etCpwd.text.toString().trim()
                if (etNama.text.isNullOrEmpty()) {
                    etNama.error = "Wajib diisi"
                } else if (etNohp.text.isNullOrEmpty()) {
                    etNohp.error = "Wajib diisi"
                } else if (etEmail.text.isNullOrEmpty()) {
                    etEmail.error = "Wajib diisi"
                } else if (etPwd.text.isNullOrEmpty()) {
                    etPwd.error = "Wajib diisi"
                } else if (etCpwd.text.isNullOrEmpty()) {
                    etCpwd.error = "Wajib diisi"
                } else if (pwd != cPwd) {
                    etPwd.error = "Harus sama"
                    etCpwd.error = "Harus sama"
                } else if (etPwd.text.length < 8) {
                    etPwd.error = "Password minimal 8 karakter"
                } else {
                    viewModel.registerWithFirebase(auth, nama, email, noHp, pwd)
                }
            }
        }

        binding.buttonToLogin.setOnClickListener {
            findNavController().navigate(R.id.action_navigation_register_to_navigation_login)
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
        val view = layoutInflater.inflate(R.layout.custom_dialog_success,null)
        val tvPertama = view.findViewById<TextView>(R.id.tvPertama)
        val tvKedua = view.findViewById<TextView>(R.id.tvKedua)
        val ivDialog = view.findViewById<ImageView>(R.id.ivDialog)
        val close = view.findViewById<Button>(R.id.btn_close)

        if (mode== SUCCESS){
            val title = SUCCESS
            val message = "Register berhasil"
            ivDialog.setImageDrawable(ContextCompat.getDrawable(requireContext(),R.drawable.undraw_completed_03xt))
            tvPertama.text = title
            tvKedua.text = message
            builder.setView(view)
            val dia = builder.create()
            dia.show()
            close.setOnClickListener {
                dia.dismiss()
                findNavController().navigate(R.id.action_navigation_register_to_navigation_login)
            }
        }else if (mode== ERROR){
            val title = ERROR
            val message = "Register Gagal, coba lagi"
            ivDialog.setImageDrawable(ContextCompat.getDrawable(requireContext(),R.drawable.undraw_page_not_found_re_e9o6))
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