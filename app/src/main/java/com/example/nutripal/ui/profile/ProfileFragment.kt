package com.example.nutripal.ui.profile

import android.content.Intent
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.LinearLayout
import android.widget.Toast
import androidx.fragment.app.Fragment
import androidx.recyclerview.widget.LinearLayoutManager
import com.example.nutripal.databinding.FragmentProfileBinding
import com.example.nutripal.ui.auth.AuthActivity

class ProfileFragment : Fragment() {
    private var _binding: FragmentProfileBinding? = null
    private val binding get() = _binding!!

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        _binding = FragmentProfileBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        setupItemProfile()
        binding.btnLogout.setOnClickListener {
            startActivity(Intent(requireContext(),AuthActivity::class.java))
        }
    }

    private fun setupItemProfile() {
        val listTitle = listOf(
            "Data diri",
            "Data Preference",
            "Bantuan",
            "Privacy"
        )
        val adapter = ProfileAdapter(listTitle,object:ProfileAdapter.ProfileListener{
            override fun onKlik(title: String) {
                Toast.makeText(requireContext(),title,Toast.LENGTH_SHORT).show()
            }
        })
        val lm = LinearLayoutManager(requireContext(),LinearLayoutManager.VERTICAL,false)
       binding.rcListProfile.adapter = adapter
        binding.rcListProfile.layoutManager = lm
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }
}