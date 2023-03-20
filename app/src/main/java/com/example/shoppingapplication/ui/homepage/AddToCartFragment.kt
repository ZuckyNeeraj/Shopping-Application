package com.example.shoppingapplication.ui.homepage

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import com.example.shoppingapplication.databinding.FragmentAddToCartBinding

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

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }
}