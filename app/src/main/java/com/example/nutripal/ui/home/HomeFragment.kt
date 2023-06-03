package com.example.nutripal.ui.home

import android.app.AlertDialog
import android.content.Intent
import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import androidx.recyclerview.widget.LinearLayoutManager
import com.bumptech.glide.Glide
import com.example.nutripal.R
import com.example.nutripal.databinding.FragmentHomeBinding
import com.example.nutripal.network.response.ApiResult
import com.example.nutripal.network.response.food.ResponseFoodsItem
import com.example.nutripal.network.response.userpreference.ListUserPreferences
import com.example.nutripal.savepreference.PreferenceUser
import com.example.nutripal.ui.auth.AuthActivity
import com.example.nutripal.ui.detail.DetailActivity
import com.example.nutripal.ui.viemodel.NutripalViewModel
import com.example.nutripal.utils.Util.getCalories
import com.example.nutripal.utils.Util.getPercenCalori

class HomeFragment : Fragment() {
    private lateinit var builder: AlertDialog.Builder
    private lateinit var dialog: AlertDialog
    private var _binding: FragmentHomeBinding? = null
    private val nutripalViewModel: NutripalViewModel by viewModels()
    private val binding get() = _binding!!

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        _binding = FragmentHomeBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        setupDialogLoading()

        val pref = PreferenceUser(requireContext())
        val token = pref.getToken()

        if (token.isNullOrEmpty()) {
            val intent = Intent(requireContext(), AuthActivity::class.java)
            intent.flags = Intent.FLAG_ACTIVITY_CLEAR_TASK or Intent.FLAG_ACTIVITY_NEW_TASK
            startActivity(intent)
        }

        nutripalViewModel.getUserPreference(token.toString())
        nutripalViewModel.getDatadiri(token.toString())
        nutripalViewModel.userPreference.observe(viewLifecycleOwner) { preference ->
            when (preference) {
                is ApiResult.Success -> {
                    showDialogLoading(false)
                    getDashboard(preference.data.listUserPreferences)
                }

                is ApiResult.Loading -> {
                    showDialogLoading(true)
                }

                is ApiResult.Error -> {
                    showDialogLoading(false)
                }
            }
        }

            nutripalViewModel.dataDiri.observe(viewLifecycleOwner){dataDiri->
                when(dataDiri){
                    is ApiResult.Success->{
                        showDialogLoading(false)
                        val nama = if (dataDiri.data.data.nama.isNullOrEmpty()){
                            ""
                        }else{
                            dataDiri.data.data.nama
                        }
                        binding.tvNameHome.text = "Hi, ${nama}"
                        if (!dataDiri.data.data.foto_profile.isEmpty()){
                            Glide.with(requireContext())
                                .load(dataDiri.data.data.foto_profile)
                                .into(binding.circleImageView)
                        }
                    }
                    is ApiResult.Loading->{
                        showDialogLoading(true)
                    }
                    is ApiResult.Error->{
                        showDialogLoading(false)
                    }
                }
            }


        nutripalViewModel.listFood.observe(viewLifecycleOwner){foods->
            when (foods) {
                is ApiResult.Success -> {
                    showDialogLoading(false)
                    setupRecylcerFoods(foods.data)

                }

                is ApiResult.Loading -> {
                    showDialogLoading(true)
                }

                is ApiResult.Error -> {
                    Log.e("FOODS",foods.errorMessage)
                    showDialogLoading(false)
                }
            }
        }


    }

    private fun setupRecylcerFoods(foods: List<ResponseFoodsItem>) {
        val adapter =
            FoodRecomendationAdapter(foods, object : FoodRecomendationAdapter.Recomendation {
                override fun onKlik(food: ResponseFoodsItem) {

                    val intent = Intent(requireContext(), DetailActivity::class.java)
                    intent.putExtra("DATA",food.foodId)
                    startActivity(intent)
                }

            })
        val layoutManager =
            LinearLayoutManager(requireContext(), LinearLayoutManager.VERTICAL, false)
        binding.rcListHome.adapter = adapter
        binding.rcListHome.layoutManager = layoutManager
    }

    private fun getDashboard(preference: ListUserPreferences){
        val calorie:Double = getCalories(preference)
        val percen = getPercenCalori(calorie,1000)
        binding.tvPercen.text = percen.toString()+"%"
        binding.circularProgressBar.progress = 1000f
        binding.circularProgressBar.progressMax = calorie.toFloat()
        binding.tvKebKalori.text = calorie.toInt().toString()+" kkal"
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