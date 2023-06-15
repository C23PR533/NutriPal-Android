package com.example.nutripal.ui.home

import android.app.AlertDialog
import android.content.Context
import android.content.Intent
import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.appcompat.widget.Toolbar
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import androidx.navigation.fragment.findNavController
import androidx.recyclerview.widget.LinearLayoutManager
import com.bumptech.glide.Glide
import com.example.nutripal.MainActivity
import com.example.nutripal.R
import com.example.nutripal.databinding.FragmentHomeBinding
import com.example.nutripal.network.response.ApiResult
import com.example.nutripal.network.response.food.Data
import com.example.nutripal.network.response.historiaktifitas.ListHistoryActivity
import com.example.nutripal.network.response.userpreference.ListUserPreferences
import com.example.nutripal.savepreference.PreferenceUser
import com.example.nutripal.ui.auth.AuthActivity
import com.example.nutripal.ui.detail.DetailActivity
import com.example.nutripal.ui.userpreference.UserPreferencesActivity
import com.example.nutripal.ui.viemodel.NutripalViewModel
import com.example.nutripal.utils.Util.getCalories
import com.example.nutripal.utils.Util.getDateNow
import com.example.nutripal.utils.Util.getPercenCalori


class HomeFragment : Fragment() {


    companion object{
         var calorie:Double = 0.0
    }
    private lateinit var builder: AlertDialog.Builder
    private lateinit var dialog: AlertDialog
    private var _binding: FragmentHomeBinding? = null
    private val  nutripalViewModel: NutripalViewModel by viewModels()
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
        hideToolbar()
        setupDialogLoading()

        val pref = PreferenceUser(requireContext())
        val token = pref.getToken()

        if (token.isNullOrEmpty()) {
            val intent = Intent(requireContext(), AuthActivity::class.java)
            intent.flags = Intent.FLAG_ACTIVITY_CLEAR_TASK or Intent.FLAG_ACTIVITY_NEW_TASK
            startActivity(intent)
        }


        nutripalViewModel.getUserPreference(token.toString())

        nutripalViewModel.userPreference.observe(viewLifecycleOwner) { preference ->
            when (preference) {
                is ApiResult.Success -> {
                    val result =  preference.data.listUserPreferences
                    val desease = convertListToString(result.disease)
                    val fav = convertListToString(result.favoriteFood)
                    pref.setDataPreference(
                        result.goals,result.weight,result.height,result.gender,result.birthdate,result.activityLevel,desease,fav
                    )
                    pref.setCalorie( getHitungKalori(preference.data.listUserPreferences))
                    showDialogLoading(false)
                    nutripalViewModel.getHistoryAktifitas(token.toString(), getDateNow())
                }

                is ApiResult.Loading -> {
                    showDialogLoading(true)
                }

                is ApiResult.Error -> {
                    startActivity(Intent(requireContext(),UserPreferencesActivity::class.java))
                    showDialogLoading(false)
                }
            }
        }
        nutripalViewModel.history.observe(viewLifecycleOwner){history->
            when (history) {
                is ApiResult.Success -> {
                    setupDashboard(history.data)
                    showDialogLoading(false)
                }
                is ApiResult.Loading -> {
                    showDialogLoading(true)
                }
                is ApiResult.Error -> {
                    binding.apply {
                        tvPercen.text = "0%"
                        circularProgressBar.progress = 0f
                        circularProgressBar.progressMax = calorie.toFloat()
                        tvKebKalori.text = calorie.toInt().toString()+"kkal"
                        tvKaloriTerkonsumsi.text = "0 kkal"
                        tvKaloriBelumTercapai.text = calorie.toInt().toString()+"kkal"
                    }
                    showDialogLoading(false)
                }
            }
        }
        nutripalViewModel.dataDiri.observe(viewLifecycleOwner){dataDiri->
                when(dataDiri){
                    is ApiResult.Success->{
                        val result = dataDiri.data.data
                        pref.setDataDiri(
                            result.nama.ifEmpty { "" },
                            result.email.ifEmpty { "" },
                            result.nomor_hp.ifEmpty { "" },
                            result.gender.ifEmpty { "" },
                            result.birthdate.ifEmpty { "" },
                            result.foto_profile.ifEmpty { "" }
                            )
                        val nama = result.nama.ifEmpty {
                            ""
                        }
                        binding.tvNameHome.text = "Hi,\n${nama}"
                        if(result.foto_profile.isNotEmpty()){
                            Glide.with(requireContext())
                                .load(result.foto_profile)
                                .into(binding.circleImageView)
                        }
                        showDialogLoading(false)

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
                    setupRecylcerFoods(foods.data.data)
                    showDialogLoading(false)
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
        nutripalViewModel.getDatadiri(token.toString())
        nutripalViewModel.getListFoods()

        binding.circleImageView.setOnClickListener {
            findNavController().navigate(R.id.action_navigation_home_to_navigation_profile)
        }


    }

    private fun hideToolbar() {
        val toolbar = activity?.findViewById<Toolbar>(R.id.toolbar)
        toolbar?.visibility = View.GONE
    }

    fun convertListToString(list: List<String>): String {
        return list.joinToString(", ")
    }

    private fun setupRecylcerFoods(foods: List<Data>) {
        val adapter =
            FoodRecomendationAdapter(requireContext(),foods, object : FoodRecomendationAdapter.Recomendation {
                override fun onKlik(food: Data) {
                    val intent = Intent(requireContext(),DetailActivity::class.java)
                    intent.putExtra("DATA",food.foodId)
                    startActivity(intent)

                }

            })
        val layoutManager =
            LinearLayoutManager(requireContext(), LinearLayoutManager.VERTICAL, false)
        binding.rcListHome.adapter = adapter
        binding.rcListHome.layoutManager = layoutManager
    }

    private fun setupDashboard(history:ListHistoryActivity){
        val percen = getPercenCalori(history.History[0].kalori_harian.toDouble(),history.History[0].total_kalori)
        binding.apply {
            circularProgressBar.progressMax = history.History[0].kalori_harian.toFloat()
            circularProgressBar.progress = history.History[0].total_kalori.toFloat()
            tvPercen.text = "$percen%"
            tvKebKalori.text = "${calorie.toInt()} Kkal"
            tvKaloriTerkonsumsi.text = "${history.History[0].total_kalori} Kkal"
            tvKaloriBelumTercapai.text = "${history.History[0].sisa_kalori} Kkal"
        }

    }

    private fun getHitungKalori(preference: ListUserPreferences):Double{
         calorie = getCalories(preference)
        return calorie
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

    override fun onResume() {
        super.onResume()
        hideToolbar()
    }

    override fun onStart() {
        super.onStart()
        hideToolbar()
    }


    override fun onDestroyView() {
        super.onDestroyView()
        val toolbar = activity?.findViewById<Toolbar>(R.id.toolbar)
        toolbar?.visibility = View.VISIBLE
    }
}
