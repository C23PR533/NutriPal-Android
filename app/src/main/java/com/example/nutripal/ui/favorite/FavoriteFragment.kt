package com.example.nutripal.ui.favorite

import android.app.AlertDialog
import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import androidx.appcompat.widget.Toolbar
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import androidx.recyclerview.widget.LinearLayoutManager
import com.example.nutripal.R
import com.example.nutripal.databinding.FragmentFavoriteBinding
import com.example.nutripal.network.response.ApiResult
import com.example.nutripal.network.response.favorites.Data
import com.example.nutripal.savepreference.PreferenceUser
import com.example.nutripal.ui.viemodel.NutripalViewModel

class FavoriteFragment : Fragment() {

    companion object{
         var calorie:Double = 0.0
    }
    private lateinit var builder: AlertDialog.Builder
    private lateinit var dialog: AlertDialog
    private var _binding: FragmentFavoriteBinding? = null
    private val  nutripalViewModel: NutripalViewModel by viewModels()
    private val binding get() = _binding!!

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        _binding = FragmentFavoriteBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        setupDialogLoading()

        val pref = PreferenceUser(requireContext())
        val token = pref.getToken().toString()

        nutripalViewModel.getlistFoodFavorite(token)
        nutripalViewModel.favoriteFoods.observe(viewLifecycleOwner){fav->
            when(fav){
                is ApiResult.Loading->{
                    showDialogLoading(true)
                }
                is ApiResult.Error->{
                    showDialogLoading(false)
                }
                is ApiResult.Success->{
                    setupRecylcerFoods(fav.data.data)
                    showDialogLoading(false)
                }
            }
        }



    }
    private fun setupRecylcerFoods(foods: Data) {
        val adapter =
            FoodFavoriteAdapter(foods.favoriteFoods, object : FoodFavoriteAdapter.FavoriteFoodsListener {
                override fun onKlik(id: String) {
                    Log.e("KLIK",id)
                }
            })
        val layoutManager =
            LinearLayoutManager(requireContext(), LinearLayoutManager.VERTICAL, false)
        binding.rcListFav.adapter = adapter
        binding.rcListFav.layoutManager = layoutManager
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
    private fun toolbarFav(){
        val toolbar = activity?.findViewById<Toolbar>(R.id.toolbar)
        val title = toolbar?.findViewById<TextView>(R.id.titleBar)
        title?.text = "Favorite Food"
        toolbar?.title=""
    }

    override fun onStart() {
        super.onStart()
        toolbarFav()
    }

    override fun onResume() {
        super.onResume()
        toolbarFav()
    }


    override fun onDestroyView() {
        super.onDestroyView()
        val toolbar = activity?.findViewById<Toolbar>(R.id.toolbar)
        toolbar?.visibility = View.VISIBLE
    }
}