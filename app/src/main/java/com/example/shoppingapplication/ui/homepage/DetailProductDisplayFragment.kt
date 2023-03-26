package com.example.shoppingapplication.ui.homepage

/**
 * This is the detail product display fragment.
 * This will trigger once items from the recycler view are clicked.
 * @author Neeraj Mahapatra
 */

import android.annotation.SuppressLint
import android.os.Bundle
import android.view.*
import android.widget.ImageView
import androidx.fragment.app.Fragment
import com.bumptech.glide.Glide
import com.example.shoppingapplication.R
import com.example.shoppingapplication.data.productsItem
import com.example.shoppingapplication.databinding.FragmentDetailProductDisplayBinding

class DetailProductDisplayFragment : Fragment() {

    private var _binding: FragmentDetailProductDisplayBinding? = null
    private val binding get() = _binding!!
    private lateinit var imageView: ImageView

    private val onClickListener = View.OnClickListener { view ->
        when (view) {
            imageView ->{
                // all the functionality that we want to achieve on click of buttonName
                fragmentManager?.beginTransaction()
                    ?.replace(R.id.recyclerViewFrameLayout, ProductDisplayFragment())?.addToBackStack(null)
                    ?.commit()
            }
        }
    }

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View {
        _binding = FragmentDetailProductDisplayBinding.inflate(inflater, container, false)
        return binding.root
    }

    /**
     * Once the view is created assigning the menu resources image.
     * also setting up the product display fragment again on pressing of this back button.
     * @return Menu Image, replace the current fragment to product display fragment
     */
    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        inits()
        displayDataFromProductDisplayFragment()
        setListeners()
    }

    private fun inits(){
        imageView = requireActivity().findViewById(R.id.hamburger_menu)
        imageView.setImageResource(R.drawable.green_go_back)
    }

    private fun setListeners() {
        imageView.setOnClickListener(onClickListener)
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