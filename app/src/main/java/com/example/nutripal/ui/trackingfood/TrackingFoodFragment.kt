package com.example.nutripal.ui.trackingfood

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.navigation.fragment.findNavController
import com.example.nutripal.R
import com.example.nutripal.databinding.FragmentTrackingFoodBinding
import com.example.nutripal.utils.Util.setupDatePicker

class TrackingFoodFragment : Fragment() {
    private var _binding: FragmentTrackingFoodBinding? = null
    private val binding get() = _binding!!

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        _binding = FragmentTrackingFoodBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        setupDatePicker(requireContext(),binding.tvDate)

        setupAddBtn()



    }

    private fun setupAddBtn() {
        binding.apply {
            imageView2.setOnClickListener {
                findNavController().navigate(R.id.navigation_search)
            }
            ivAddMakanSiang.setOnClickListener {
                findNavController().navigate(R.id.navigation_search)
            }
            ivAddMakanMalam.setOnClickListener {
                findNavController().navigate(R.id.navigation_search)
            }
            ivAddCemilan.setOnClickListener {
                findNavController().navigate(R.id.navigation_search)
            }
        }
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }
}