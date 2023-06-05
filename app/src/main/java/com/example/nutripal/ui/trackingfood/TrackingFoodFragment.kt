package com.example.nutripal.ui.trackingfood

import android.app.DatePickerDialog
import android.content.Context
import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import androidx.navigation.fragment.findNavController
import androidx.recyclerview.widget.LinearLayoutManager
import com.example.nutripal.MainActivity
import com.example.nutripal.R
import com.example.nutripal.databinding.FragmentTrackingFoodBinding
import com.example.nutripal.network.response.ApiResult
import com.example.nutripal.network.response.historiaktifitas.History
import com.example.nutripal.network.response.historiaktifitas.KaloriMasuk
import com.example.nutripal.network.response.historiaktifitas.ListHistoryActivity
import com.example.nutripal.savepreference.PreferenceUser
import com.example.nutripal.ui.home.HomeFragment
import com.example.nutripal.ui.home.HomeFragment.Companion.calorie
import com.example.nutripal.ui.viemodel.NutripalViewModel
import com.example.nutripal.utils.Util.setupDatePicker
import java.text.SimpleDateFormat
import java.util.Calendar
import java.util.Locale

class TrackingFoodFragment : Fragment() {

    private val nutripalViewModel:NutripalViewModel by viewModels()
    private var _binding: FragmentTrackingFoodBinding? = null
    private val binding get() = _binding!!
    var tvDate = ""
    private val responsHistory = HomeFragment.responsHistory

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
         val pref = PreferenceUser(requireContext())
         val token = pref.getToken().toString()
        tvDate = binding.tvDate.text.toString()

//        setupDatePicker(requireContext(),binding.tvDate)
        setupDatePicker()
        setupAddBtn()

        nutripalViewModel.getHistoryAktifitas(token)
//        nutripalViewModel.history.observe(viewLifecycleOwner){history->
//            when(history){
//                is ApiResult.Loading->{
//
//                }
//                is ApiResult.Error->{
//                    Log.e("TRACK",history.errorMessage)
//                }
//                is ApiResult.Success->{
//
//                    responseHistory = history.data
//                    Log.e("DATE",tvDate)
//                    lateinit var h:History
//                    for (i in history.data.History.indices){
//                        if (history.data.History[i].tanggal==tvDate){
//                            h=history.data.History[i]
//                        }
//                    }
//
//                    setHeader(h.kalori_harian,h.total_kalori.toString(),h.sisa_kalori)
//                    filterData(tvDate,history.data)
//
//                }
//            }
//        }



    }
    private fun filterData(tgl:String){
        val result = responsHistory.History
        lateinit var history :History
        var listKaloriMasuk= emptyList<KaloriMasuk>()
        for (i in result.indices){
            if (result[i].tanggal==tgl){
                 listKaloriMasuk=result[i].aktifitas.kalori_masuk
                history = result[i]
            }
        }
        if (listKaloriMasuk.isEmpty()){
            setHeader(calorie.toInt().toString(),"0","${calorie.toInt()}")
            setupRcListSarapan(emptyList())
            setupRcListMakanSiang(emptyList())
            setupRcListMakanMalam(emptyList())
            setupRcListCemilan(emptyList())
        }else{
            setHeader(history.kalori_harian,history.total_kalori.toString(),history.sisa_kalori)
            setupRcListSarapan(listKaloriMasuk)
            setupRcListMakanSiang(listKaloriMasuk)
            setupRcListMakanMalam(listKaloriMasuk)
            setupRcListCemilan(listKaloriMasuk)
        }


    }
    private fun setupDatePicker() {
        val cal = Calendar.getInstance()
        val dateNow = Calendar.getInstance().time
        val mySdf = SimpleDateFormat("dd-MM-yyyy")
        val now = mySdf.format(dateNow)
        binding.tvDate.text = now
        filterData(now)

        val dateSetListener = DatePickerDialog.OnDateSetListener { _, year, monthOfYear, dayOfMonth ->
            cal.set(Calendar.YEAR, year)
            cal.set(Calendar.MONTH, monthOfYear)
            cal.set(Calendar.DAY_OF_MONTH, dayOfMonth)
            val myFormat = "dd-MM-yyyy" // mention the format you need
            val sdf = SimpleDateFormat(myFormat, Locale.US)
            binding.tvDate.text = sdf.format(cal.time)
            tvDate=sdf.format(cal.time)
            filterData(sdf.format(cal.time))


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
            it.waktu == "Sarapan"
        }
        sumKalorimakan(sarapan,binding.tvTotalSarapan)
        val adapter=TrackingFoodAdapter(sarapan)
        binding.apply {
            rcListSarapan.adapter = adapter
            rcListSarapan.layoutManager = LinearLayoutManager(requireContext())
        }
    }
    private fun setupRcListMakanSiang(listMakanan:List<KaloriMasuk>){
        val makanSiang = listMakanan.filter {
            it.waktu == "Makan Siang"
        }
        sumKalorimakan(makanSiang,binding.tvTotalMakanSiang)
        val adapter=TrackingFoodAdapter(makanSiang)
        binding.apply {
            rcListMakanSiang.adapter = adapter
            rcListMakanSiang.layoutManager = LinearLayoutManager(requireContext())
        }
    }
    private fun setupRcListMakanMalam(listMakanan:List<KaloriMasuk>){
        val malam = listMakanan.filter {
            it.waktu == "Makan Malam"
        }
        sumKalorimakan(malam,binding.tvTotalMakanMalam)
        val adapter=TrackingFoodAdapter(malam)
        binding.apply {
            rcListMakanMalam.adapter = adapter
            rcListMakanMalam.layoutManager = LinearLayoutManager(requireContext())
        }
    }
    private fun setupRcListCemilan(listMakanan:List<KaloriMasuk>){
        val cemilan = listMakanan.filter {
            it.waktu == "Cemilan"
        }
        sumKalorimakan(cemilan,binding.tvTotalCemilan)
        val adapter=TrackingFoodAdapter(cemilan)
        binding.apply {
            rcListCemilan.adapter = adapter
            rcListCemilan.layoutManager = LinearLayoutManager(requireContext())
        }
    }

    private fun sumKalorimakan(listMakanan:List<KaloriMasuk>,textView: TextView){
        var result = 0
        for (i in listMakanan.indices){
            result+=listMakanan[i].kalori.toInt()
        }
        textView.text = "${result}kkal"

    }

    private fun setHeader(target:String,tercapai:String,sisa:String){
        binding.apply {
           tvTarget.text = "${target} kkal"
            tvTercapai.text = "${tercapai} kkal"
            tvSisa.text = "${sisa} kkal"
        }
    }


    private fun setupAddBtn() {
        binding.apply {
            imageView2.setOnClickListener {
                findNavController().navigate(R.id.action_navigation_tracking_food_to_navigation_search)
            }
            ivAddMakanSiang.setOnClickListener {
                findNavController().navigate(R.id.action_navigation_tracking_food_to_navigation_search)
            }
            ivAddMakanMalam.setOnClickListener {
                findNavController().navigate(R.id.action_navigation_tracking_food_to_navigation_search)
            }
            ivAddCemilan.setOnClickListener {
                findNavController().navigate(R.id.action_navigation_tracking_food_to_navigation_search)
            }
        }
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }
}