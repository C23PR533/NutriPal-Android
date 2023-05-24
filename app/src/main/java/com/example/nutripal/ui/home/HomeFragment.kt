package com.example.nutripal.ui.home

import android.content.Intent
import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.recyclerview.widget.LinearLayoutManager
import com.example.nutripal.databinding.FragmentHomeBinding
import com.example.nutripal.network.dummmy.ResponseFoods
import com.example.nutripal.savepreference.PreferenceUser
import com.example.nutripal.ui.detail.DetailActivity
import com.example.nutripal.utils.Util.getCalories
import com.example.nutripal.utils.Util.getJsonDataFromAsset
import com.example.nutripal.utils.Util.getPercenCalori
import com.google.gson.Gson
import com.google.gson.reflect.TypeToken

class HomeFragment : Fragment() {
    private var _binding: FragmentHomeBinding? = null
    private val binding get() = _binding!!

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
       activity?.actionBar?.hide()
        _binding = FragmentHomeBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        val jsonFileString = getJsonDataFromAsset(requireContext(), "data_makanan.json")
        val gson = Gson()
        val listFoodsType = object : TypeToken<ResponseFoods>() {}.type

        val foods: ResponseFoods = gson.fromJson(jsonFileString, listFoodsType)
        val adapter= FoodRecomendationAdapter(foods.food,object:FoodRecomendationAdapter.Recomendation{
            override fun onKlik() {
                startActivity(Intent(requireContext(),DetailActivity::class.java))
            }
        })
        val layoutManager = LinearLayoutManager(requireContext(),LinearLayoutManager.VERTICAL,false)
        binding.rcListHome.adapter = adapter
        binding.rcListHome.layoutManager = layoutManager

        val pref = PreferenceUser(requireContext())
        val dataPref = pref.getDataPreference()
        val goal = dataPref[0]
        val weight = dataPref[1]
        val height = dataPref[2]
        val gender = dataPref[3]
        val birthDate = dataPref[4]
        val level = dataPref[5]
        val calorie = getCalories(gender,weight,height,birthDate,level,goal)
        val percen = getPercenCalori(calorie,1000)
        binding.tvPercen.text = percen.toString()+"%"
        binding.circularProgressBar.progress = 1000f
        binding.circularProgressBar.progressMax = calorie.toFloat()
        binding.tvKebKalori.text = calorie.toInt().toString()+"kkal"
        dataPref.forEach {
            Log.e("USER",it)
        }

    }


    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }
}