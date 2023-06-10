package com.example.nutripal.ui.profile

import android.content.Intent
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.LinearLayout
import android.widget.TextView
import android.widget.Toast
import androidx.appcompat.widget.Toolbar
import androidx.fragment.app.Fragment
import androidx.recyclerview.widget.LinearLayoutManager
import com.example.nutripal.MainActivity
import com.example.nutripal.R
import com.example.nutripal.databinding.FragmentProfileBinding
import com.example.nutripal.savepreference.PreferenceUser
import com.example.nutripal.ui.auth.AuthActivity
import com.google.firebase.auth.FirebaseAuth

class ProfileFragment : Fragment() {
    private val auth = FirebaseAuth.getInstance()
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
        val pref = PreferenceUser(requireContext())
        val resultPref = pref.getDatadiri()
        val nama = resultPref[0]
        binding.tvNamaProfile.text = nama
        setupItemProfile()

        binding.btnLogout.setOnClickListener {
            auth.signOut()
            pref.clearDataPrefrence()
            val intent = Intent(requireContext(),AuthActivity::class.java)
            intent.flags = Intent.FLAG_ACTIVITY_CLEAR_TASK or Intent.FLAG_ACTIVITY_NEW_TASK
            startActivity(intent)
        }


    }

    private fun toolbarProfile() {
        val toolbar = activity?.findViewById<Toolbar>(R.id.toolbar)
        toolbar?.visibility = View.GONE
    }

    private fun setupItemProfile() {
        val listTitle = listOf(
            DataIntentProfile("Data diri",Intent(requireContext(),EditDataDiriActivity::class.java)),
            DataIntentProfile("Data Preference",Intent(requireContext(),EditPreferenceUserActivity::class.java)),
            DataIntentProfile("Bantuan",Intent(requireContext(),EditDataDiriActivity::class.java)),
            DataIntentProfile("Privasi",Intent(requireContext(),EditDataDiriActivity::class.java)),

        )
        val adapter = ProfileAdapter(listTitle,object:ProfileAdapter.ProfileListener{
            override fun onKlik(intent: Intent) {
                startActivity(intent)
            }

        })
        val lm = LinearLayoutManager(requireContext(),LinearLayoutManager.VERTICAL,false)
       binding.rcListProfile.adapter = adapter
        binding.rcListProfile.layoutManager = lm
    }

    override fun onResume() {
        super.onResume()
        toolbarProfile()
    }

    override fun onStart() {
        super.onStart()
        toolbarProfile()
    }

    override fun onDestroyView() {
        super.onDestroyView()
        val toolbar = activity?.findViewById<Toolbar>(R.id.toolbar)
        toolbar?.visibility = View.VISIBLE
    }
}