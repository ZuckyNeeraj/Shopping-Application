package com.example.shoppingapplication.ui.homepage

import android.annotation.SuppressLint
import android.content.Context
import android.content.SharedPreferences
import android.os.Bundle
import android.provider.Settings.Global.putString
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ListView
import android.widget.TextView
import androidx.fragment.app.Fragment
import com.example.shoppingapplication.R
import com.example.shoppingapplication.databinding.FragmentAddToCartBinding
import com.example.shoppingapplication.repository.CartAdapter
import com.google.android.material.bottomnavigation.BottomNavigationView
import com.google.android.material.navigation.NavigationView
import com.google.common.reflect.TypeToken
import com.google.gson.Gson

/**
 * This fragment will deal with adding the items to the cart.
 * It is a part of Home Page Activity.
 * @author Neeraj Mahapatra
 */

class AddToCartFragment : Fragment() {

    private var _binding: FragmentAddToCartBinding? = null
    private val binding get() = _binding!!
    private lateinit var cartQuantities: MutableMap<Int, Pair<String, Int>>
    private val sharedPreferences: SharedPreferences by lazy {
        requireContext().getSharedPreferences("my_shared_prefs", Context.MODE_PRIVATE)
    }

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View? {
        _binding = FragmentAddToCartBinding.inflate(inflater, container, false)
        cartQuantities = sharedPreferences.getString("my_hashmap_key", null)?.let {
            Gson().fromJson(it, object : TypeToken<MutableMap<Int, Pair<String, Int>>>() {}.type)
        } ?: mutableMapOf()
        val listView = binding.cartListView // get the reference of the ListView from the binding object
        val adapter = CartAdapter(requireContext(), cartQuantities)
        listView.adapter = adapter
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
            setTheCartItemNumbers()
    }

    /**
     * This method will be used to store the data into the map that is in
     * main activity.
     * And also the cart details will be extracted from that global hashmap.
     * @return Total number of items in the cart.
     */
    private fun setTheCartItemNumbers() {
        val count = arguments?.getInt("count", 0)?:0
        val productName = arguments?.getString("productName", "")?:""
        val productId = arguments?.getInt("productId", 0)?:0

        val (name, currentCount) = cartQuantities.getOrDefault(productId, Pair("", 0))
        val newCount = currentCount + count
        cartQuantities[productId] = Pair(productName, newCount)

        val totalItems = cartQuantities.values.sumBy { it.second }

        // Update the cart item title with the total number of items in the cart
        requireActivity().findViewById<NavigationView>(R.id.navigation_view)
            .menu.findItem(R.id.nav_cart).title = "My Cart ($totalItems)"

        // Update the cart item title with the total number of items in the cart
        requireActivity().findViewById<BottomNavigationView>(R.id.bottomNV)
            .menu.findItem(R.id.nav_cart).title = "My Cart ($totalItems)"

        sharedPreferences.edit().apply {
            putString("my_hashmap_key", Gson().toJson(cartQuantities))
            apply()
        }
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }
}
