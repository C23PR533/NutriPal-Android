package com.example.nutripal.ui.search

import android.annotation.SuppressLint
import android.content.Intent
import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.MotionEvent
import android.view.View
import android.view.ViewGroup
import android.widget.LinearLayout
import android.widget.SearchView
import android.widget.Toast
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import androidx.recyclerview.widget.LinearLayoutManager
import com.example.nutripal.databinding.FragmentSearchBinding
import com.example.nutripal.network.response.ApiResult
import com.example.nutripal.network.response.search.Data
import com.example.nutripal.ui.detail.DetailActivity
import com.example.nutripal.ui.viemodel.NutripalViewModel

class SearchFragment : Fragment() {
    private val nutripalViewModel:NutripalViewModel by viewModels()
    private var _binding: FragmentSearchBinding? = null
    private val binding get() = _binding!!

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        _binding = FragmentSearchBinding.inflate(inflater, container, false)
        return binding.root
    }

    @SuppressLint("ClickableViewAccessibility")
    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)


            binding.etSearch.setOnQueryTextListener(object: SearchView.OnQueryTextListener{
            override fun onQueryTextSubmit(query: String?): Boolean {

                return false
            }

            override fun onQueryTextChange(newText: String?): Boolean {
                if (newText.isNullOrEmpty()|| newText.isEmpty()){
                    binding.rcListSearch.visibility = View.GONE
                    setupRcListSearch(emptyList())
                }else{
                    nutripalViewModel.getSearchFood(newText)
                    nutripalViewModel.searchFood.observe(viewLifecycleOwner){search->
                        when(search){
                            is ApiResult.Loading->{
                            }
                            is ApiResult.Error->{
                            }
                            is ApiResult.Success->{
                                binding.rcListSearch.visibility = View.VISIBLE
                                setupRcListSearch(search.data.data)
                            }
                        }
                    }
                }
                return true
            }
        })
        binding.etSearch.setOnCloseListener {
            binding.rcListSearch.visibility = View.GONE
            true
        }
    }

    private fun setupRcListSearch(listSearch:List<Data>){
        val adapter=SearchFoodAdapter(listSearch,object:SearchFoodAdapter.ListenerSearch{
            override fun onKlik(food: Data) {
                val intent = Intent(requireContext(),DetailActivity::class.java)
                intent.putExtra("DATA",food.food_id)
                startActivity(intent)
            }
        })
        binding.apply {
            rcListSearch.adapter = adapter
            rcListSearch.layoutManager = LinearLayoutManager(requireContext())
        }
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }
}