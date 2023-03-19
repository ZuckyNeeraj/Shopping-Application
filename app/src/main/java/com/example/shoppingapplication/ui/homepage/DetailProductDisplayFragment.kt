package com.example.shoppingapplication.ui.homepage

/**
 * This is the detail product display fragment.
 * This will trigger once items from the recycler view are clicked.
 * @author Neeraj Mahapatra
 */

import android.annotation.SuppressLint
import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import com.bumptech.glide.Glide
import com.example.shoppingapplication.R
import com.example.shoppingapplication.data.productsItem
import com.example.shoppingapplication.databinding.FragmentDetailProductDisplayBinding

class DetailProductDisplayFragment : Fragment() {

    private var _binding: FragmentDetailProductDisplayBinding? = null
    private val binding get() = _binding!!
    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        // Inflate the layout for this fragment
        _binding = FragmentDetailProductDisplayBinding.inflate(inflater, container, false)
        displayDataFromProductDisplayFragment()
        return binding.root
    }

    /**
     * It will receive the data from the Product Display fragment.
     * And populate it to the detail product display fragment.
     * @return Store all the product details into the root fragment
     */
    @SuppressLint("SetTextI18n")
    private fun displayDataFromProductDisplayFragment() {
        val product = arguments?.getSerializable("product") as productsItem

        binding.detailedProductName.text = product.title
        binding.detailedProductDescription.text = product.description
        Glide.with(this)
            .load(product.image)
            .override(400, 400)
            .placeholder(R.drawable.loading) //placeholder image
            .into(binding.detailedProductImage)
        val priceValue: Double? = product.price
        binding.detailedProductPrice.text = "$priceValue ₹"
    }

}