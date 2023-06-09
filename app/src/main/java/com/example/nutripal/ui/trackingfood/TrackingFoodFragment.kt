package com.example.nutripal.ui.trackingfood

import android.app.DatePickerDialog
import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import androidx.appcompat.widget.Toolbar
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import androidx.navigation.fragment.findNavController
import androidx.recyclerview.widget.LinearLayoutManager
import com.example.nutripal.MainActivity
import com.example.nutripal.R
import com.example.nutripal.databinding.FragmentTrackingFoodBinding
import com.example.nutripal.network.response.ApiResult
import com.example.nutripal.network.response.historiaktifitas.KaloriMasuk
import com.example.nutripal.network.response.historiaktifitas.ListHistoryActivity
import com.example.nutripal.savepreference.PreferenceUser
import com.example.nutripal.ui.home.HomeFragment.Companion.calorie
import com.example.nutripal.ui.viemodel.NutripalViewModel
import java.text.SimpleDateFormat
import java.util.Calendar
import java.util.Date
import java.util.Locale

class TrackingFoodFragment : Fragment() {

    private val nutripalViewModel:NutripalViewModel by viewModels()
    private var _binding: FragmentTrackingFoodBinding? = null
    private val binding get() = _binding!!
    var token = ""

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
        (activity as MainActivity).toolbarTracking()
         val pref = PreferenceUser(requireContext())
         token = pref.getToken().toString()
        setupDatePicker()
        setupAddBtn()


        nutripalViewModel.history.observe(viewLifecycleOwner){history->
            when(history){
                is ApiResult.Loading->{
                    showProgressBar(true)
                }
                is ApiResult.Error->{
                    Log.e("ERROR IN FR","HISTORI")
                    showProgressBar(false)
                    setHeader(calorie.toInt(),0,calorie.toInt())
                    setupRcListSarapan(emptyList())
                    setupRcListMakanSiang(emptyList())
                    setupRcListMakanMalam(emptyList())
                    setupRcListCemilan(emptyList())
                }
                is ApiResult.Success->{
                    showProgressBar(false)
                    filterData(history.data)
                }
            }
        }



    }

    private fun showProgressBar(show:Boolean) {
        if (show){
            binding.pbBar.visibility = View.VISIBLE
        }else{
            binding.pbBar.visibility = View.GONE
        }

    }

    private fun filterData(listHistory:ListHistoryActivity){
        val result =listHistory.History
        val listKaloriMasuk = result[0].aktifitas.kalori_masuk
        val history = result[0]
        setHeader(history.kalori_harian,history.total_kalori,history.sisa_kalori)
        setupRcListSarapan(listKaloriMasuk)
        setupRcListMakanSiang(listKaloriMasuk)
        setupRcListMakanMalam(listKaloriMasuk)
        setupRcListCemilan(listKaloriMasuk)

    }
    private fun setupDatePicker() {
        val currentDate = Date()
        val dateFormat = SimpleDateFormat("dd-MM-yyyy", Locale.getDefault())
        val now=  dateFormat.format(currentDate)
        val cal = Calendar.getInstance()
        binding.tvDate.text = now
        nutripalViewModel.getHistoryAktifitas(token,now)
        val dateSetListener = DatePickerDialog.OnDateSetListener { _, year, monthOfYear, dayOfMonth ->
            cal.set(Calendar.YEAR, year)
            cal.set(Calendar.MONTH, monthOfYear)
            cal.set(Calendar.DAY_OF_MONTH, dayOfMonth)
            val myFormat = "dd-MM-yyyy" // mention the format you need
            val sdf = SimpleDateFormat(myFormat, Locale.US)
            binding.tvDate.text = sdf.format(cal.time)
            nutripalViewModel.getHistoryAktifitas(token,sdf.format(cal.time))
        }
        binding.tvDate.setOnClickListener {
            DatePickerDialog(requireContext(), dateSetListener,
                cal.get(Calendar.YEAR),
                cal.get(Calendar.MONTH),
                cal.get(Calendar.DAY_OF_MONTH)).show()
        }
    }


    private fun setupRcListSarapan(listMakanan:List<KaloriMasuk>){
        val sarapan = listMakanan.filter {
            it.waktu=="Sarapan"
        }
        sumKalorimakan(sarapan,binding.tvTotalSarapan)
        val adapterSarapan=TrackingFoodAdapter(sarapan)
        binding.apply {
            rcListSarapan.adapter = adapterSarapan
            rcListSarapan.layoutManager = LinearLayoutManager(requireContext())
        }
    }
    private fun setupRcListMakanSiang(listMakanan:List<KaloriMasuk>){
        val makanSiang = listMakanan.filter {
            it.waktu == "Makan Siang"
        }
        sumKalorimakan(makanSiang,binding.tvTotalMakanSiang)
        val adapterSiang=TrackingFoodAdapter(makanSiang)
        binding.apply {
            rcListMakanSiang.adapter = adapterSiang
            rcListMakanSiang.layoutManager = LinearLayoutManager(requireContext())
        }
    }
    private fun setupRcListMakanMalam(listMakanan:List<KaloriMasuk>){
        val malam = listMakanan.filter {
            it.waktu == "Makan Malam"
        }
        sumKalorimakan(malam,binding.tvTotalMakanMalam)
        val adapterMalam=TrackingFoodAdapter(malam)
        binding.apply {
            rcListMakanMalam.adapter = adapterMalam
            rcListMakanMalam.layoutManager = LinearLayoutManager(requireContext())
        }
    }
    private fun setupRcListCemilan(listMakanan:List<KaloriMasuk>){
        val cemilan = listMakanan.filter {
            it.waktu == "Cemilan"
        }
        sumKalorimakan(cemilan,binding.tvTotalCemilan)
        val adapterCemilan=TrackingFoodAdapter(cemilan)
        binding.apply {
            rcListCemilan.adapter = adapterCemilan
            rcListCemilan.layoutManager = LinearLayoutManager(requireContext())
        }
    }

    private fun sumKalorimakan(listMakanan:List<KaloriMasuk>,textView: TextView){
        var result = 0
        for (i in listMakanan.indices){
            result+=listMakanan[i].kalori.toInt()
        }
        textView.text = "${result} kkal"
    }

    private fun setHeader(target:Int,tercapai:Int,sisa:Int){
        binding.apply {
           tvTarget.text = "${target} kkal"
            tvTercapai.text = "${tercapai} kkal"
            tvSisa.text = "${sisa} kkal"
        }
    }


    private fun setupAddBtn() {
        binding.apply {
            imageView2.setOnClickListener {
                val data = "Sarapan"
                val action = TrackingFoodFragmentDirections.actionNavigationTrackingFoodToNavigationSearch(data)
                findNavController().navigate(action)
            }
            ivAddMakanSiang.setOnClickListener {
                val data = "Makan Siang"
                val action = TrackingFoodFragmentDirections.actionNavigationTrackingFoodToNavigationSearch(data)
                findNavController().navigate(action)
            }
            ivAddMakanMalam.setOnClickListener {
                val data = "Makan Malam"
                val action = TrackingFoodFragmentDirections.actionNavigationTrackingFoodToNavigationSearch(data)
                findNavController().navigate(action)
            }
            ivAddCemilan.setOnClickListener {
                val data = "Cemilan"
                val action = TrackingFoodFragmentDirections.actionNavigationTrackingFoodToNavigationSearch(data)
                findNavController().navigate(action)
            }
        }
    }

    override fun onStart() {
        super.onStart()
        toolbarTracking()
    }

    override fun onResume() {
        super.onResume()
        toolbarTracking()
    }
    private fun toolbarTracking(){
        val toolbar = activity?.findViewById<Toolbar>(R.id.toolbar)
        val title = toolbar?.findViewById<TextView>(R.id.titleBar)
        title?.text = "Tracking Food"
        toolbar?.title=""
    }

    override fun onDestroyView() {
        super.onDestroyView()
        val toolbar = activity?.findViewById<Toolbar>(R.id.toolbar)
        toolbar?.visibility = View.VISIBLE
    }
}