package com.example.shoppingapplication.ui.homepage

import android.annotation.SuppressLint
import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import androidx.fragment.app.Fragment
import com.example.shoppingapplication.R
import com.example.shoppingapplication.cartQuantities
import com.example.shoppingapplication.databinding.FragmentAddToCartBinding
import com.google.android.material.bottomnavigation.BottomNavigationView
import com.google.android.material.navigation.NavigationView

/**
 * This fragment will deal with adding the items to the cart.
 * It is a part of Home Page Activity.
 * @author Neeraj Mahapatra
 */
class AddToCartFragment : Fragment() {
    private var _binding: FragmentAddToCartBinding? = null
    private val binding get() = _binding!!


    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View? {
        _binding = FragmentAddToCartBinding.inflate(inflater, container, false)
        return binding.root
    }

    @SuppressLint("SetTextI18n")
    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        val count = arguments?.getInt("count", 0) ?: 0
        val productName = arguments?.getString("productName", "") ?: ""
        val productId = arguments?.getInt("productId", 0) ?: 0

        if (cartQuantities.containsKey(productId)) {
            val (name, currentCount) = cartQuantities[productId]!!
            val newCount = currentCount + count
            cartQuantities[productId] = Pair(name, newCount)
        } else {
            cartQuantities[productId] = Pair(productName, count)
        }
        val totalItems = cartQuantities.values.sumBy { it.second }

        // Update the cart item title with the total number of items in the cart
        val navigationView = requireActivity().findViewById<NavigationView>(R.id.navigation_view)
        val cartItem = navigationView.menu.findItem(R.id.nav_cart)
        cartItem.setTitle("My Cart ($totalItems)")

        // Update the cart item title with the total number of items in the cart
        val bottomNavigationView = requireActivity().findViewById<BottomNavigationView>(R.id.bottomNV)
        val cartItemB = bottomNavigationView.menu.findItem(R.id.nav_cart)
        cartItemB.setTitle("My Cart ($totalItems)")
    }


    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }
}