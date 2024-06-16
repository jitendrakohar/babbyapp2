package com.example.babbyapp.ui.home

import android.content.Context
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import androidx.fragment.app.Fragment
import androidx.lifecycle.ViewModelProvider
import androidx.lifecycle.lifecycleScope
import androidx.recyclerview.widget.LinearLayoutManager
import com.example.babbyapp.databinding.FragmentHomeBinding
import com.example.babyapp.recyclerview.ItemAdapter
import com.example.babyapp.recyclerview.ItemList
import com.google.gson.Gson
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext

class HomeFragment : Fragment() {

    private var _binding: FragmentHomeBinding? = null

    // This property is only valid between onCreateView and
    // onDestroyView.
    private val binding get() = _binding!!

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        val homeViewModel =
            ViewModelProvider(this).get(HomeViewModel::class.java)

        _binding = FragmentHomeBinding.inflate(inflater, container, false)
        val root: View = binding.root

//        val textView: TextView = binding.textHome
        homeViewModel.text.observe(viewLifecycleOwner) {
//            textView.text = it
        }
        val context: Context = requireContext()


        binding.recyclerView.layoutManager = LinearLayoutManager(this@HomeFragment.context)
        lifecycleScope.launch {
            val itemList = withContext(Dispatchers.IO) {
                createItemList(requireContext()) // Populate your list of items here
            }
            val adapter = ItemAdapter(itemList)
            binding.recyclerView.adapter = adapter
        }


        return root
    }
    private fun createItemList(context: Context): MutableList<ItemList> {
        var itemList = mutableListOf<ItemList>()
//        itemList.add(
//            ItemList(
//                "Diper Pants",
//                "This pants are useful for the 5 years old girl, it absorbs the water with ease. This is very soft and comfortable.",
//                "Cost: Rs. 200",
//                "file_1.png"
//            )
//        )
//
//        itemList.add(
//            ItemList(
//                "tshirt",
//                "This tshirt are useful for the 5 years old girl, it absorbs the water with ease. This is very soft and comfortable.",
//                "Cost: Rs. 199",
//                "file_2.png"
//            )
//        )
        itemList=getItemListFromSharedPreferences(context)
//        saveItemListToSharedPreferences(context, itemList)
        return itemList
    }


    fun convertItemListToJson(itemList: List<ItemList>): String {
        return Gson().toJson(itemList)
    }

    fun getItemListFromSharedPreferences(context: Context): MutableList<ItemList> {
        val sharedPreferences = context.getSharedPreferences("MyPrefs", Context.MODE_PRIVATE)
        val json = sharedPreferences.getString("itemList", null)
        return if (json != null) {
            Gson().fromJson(json, Array<ItemList>::class.java).toMutableList()
        } else {
            mutableListOf()
        }
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }
}