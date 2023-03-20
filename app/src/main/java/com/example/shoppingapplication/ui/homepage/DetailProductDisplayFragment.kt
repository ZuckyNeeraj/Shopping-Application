package com.example.shoppingapplication.ui.homepage

/**
 * This is the detail product display fragment.
 * This will trigger once items from the recycler view are clicked.
 * @author Neeraj Mahapatra
 */

import android.annotation.SuppressLint
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import com.bumptech.glide.Glide
import com.example.shoppingapplication.R
import com.example.shoppingapplication.data.productsItem
import com.example.shoppingapplication.databinding.FragmentDetailProductDisplayBinding

class DetailProductDisplayFragment : Fragment() {

    private var _binding: FragmentDetailProductDisplayBinding? = null
    private val binding get() = _binding!!

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View? {
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

        with(binding) {
            detailedProductName.text = product.title
            detailedProductDescription.text = product.description
            Glide.with(this@DetailProductDisplayFragment)
                .load(product.image)
                .override(400, 400)
                .placeholder(R.drawable.loading) //placeholder image
                .into(detailedProductImage)
            val priceValue: Double? = product.price
            detailedProductPrice.text = "$priceValue ₹"
            detailedProductCategory.text = product.category
            val rating: Double? = product.rating?.rate
            detailedProductRating.text = "Rating: $rating ★ / 5★"
        }
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }

}