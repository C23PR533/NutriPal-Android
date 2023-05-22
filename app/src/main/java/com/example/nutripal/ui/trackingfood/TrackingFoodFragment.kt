package com.example.nutripal.ui.trackingfood

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
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
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }
}