package com.example.shoppingapplication.ui.homepage

import android.annotation.SuppressLint
import android.content.Context
import android.content.Intent
import android.content.SharedPreferences
import android.net.Uri
import android.os.Bundle
import android.provider.Settings.Global.putString
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Button
import android.widget.ListView
import android.widget.TextView
import android.widget.Toast
import androidx.appcompat.app.AlertDialog
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

//        val cartQuantitiesSize = cartQuantities.size.toString()
//        Log.d("cartSize", "$cartQuantitiesSize....$cartQuantities")
        val listView = binding.cartListView // get the reference of the ListView from the binding object
        val adapter = CartAdapter(requireContext(), cartQuantities, sharedPreferences)
        listView.adapter = adapter
        adapter.cartQuantities = cartQuantities // Add this line to update the cartQuantities map in the adapter
        return binding.root
    }


    @SuppressLint("SetTextI18n")
    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        setTheCartItemNumbers()
        checkOutButtonFunctionality()

    }

    /**
     * This method will add checkout button facility.
     * @return If quantity != 0 it will display the products and take user to its payment app.
     */
    private fun checkOutButtonFunctionality() {
        binding.checkoutButton.setOnClickListener {
            /*If the cart quantities are 0 than it will simply redirect
            * user to the product display fragment so that they can add products.
            * If not that it will display the products and take user to its payment app. */

            if(cartQuantities.isEmpty()){
                // show alert dialog if cart is empty
                AlertDialog.Builder(requireContext())
                    .setTitle("Empty Cart")
                    .setMessage("Please add items to cart before checking out.")
                    .setPositiveButton("OK") { _, _ ->
                        // redirect to ProductsDisplayFragment
                        parentFragmentManager
                            .beginTransaction()
                            .replace(R.id.recyclerViewFrameLayout, ProductDisplayFragment())
                            .commit()
                    }
                    .show()
            }else{
                val inflater = LayoutInflater.from(requireContext())
                val view = inflater.inflate(R.layout.dialog_checkout, null)

                val titleTextView = view.findViewById<TextView>(R.id.dialog_title)
                val messageTextView = view.findViewById<TextView>(R.id.dialog_message)
                val okButton = view.findViewById<Button>(R.id.dialog_button_ok)

                titleTextView.text = "Checkout"
                messageTextView.text = cartQuantities.entries.joinToString("\n") {
                    "${it.value.first}: ${it.value.second}"
                }
                okButton.setOnClickListener{
                    val intent = Intent(Intent.ACTION_VIEW).apply {
                        data = Uri.parse("upi://pay")
                    }
                    startActivity(intent)
                }


                val dialog = AlertDialog.Builder(requireContext())
                    .setView(view)
                    .create()

                dialog.show()
            }
        }
    }

    /**
     * This method will be used to store the data into the map that is in
     * main activity.
     * And also the cart details will be extracted from that global hashmap.
     * @return Total number of items in the cart.
     */
    private fun setTheCartItemNumbers() {
        val count = arguments?.getInt("count", 0) ?: 0
        val productName = arguments?.getString("productName", "") ?: ""
        val productId = arguments?.getInt("productId", 0) ?: 0

        val (name, currentCount) = cartQuantities.getOrDefault(productId, Pair("", 0))
        val newCount = currentCount + count

        /*This will check if the count of items becomes zero just clear the cartQuantities */

        if (newCount <= 0) {
            cartQuantities.remove(productId)
        } else {
            cartQuantities[productId] = Pair(productName, newCount)
        }

        val totalItems = cartQuantities.values.sumBy { it.second }

        sharedPreferences.edit().apply {
            if (cartQuantities.isEmpty()) {
                remove("my_hashmap_key")
            } else {
                putString("my_hashmap_key", Gson().toJson(cartQuantities))
            }
            apply()
        }

        val adapter = binding.cartListView.adapter as? CartAdapter
        adapter?.let {
            it.cartQuantities = cartQuantities
            it.notifyDataSetChanged()
        }

        // Update the cart item title with the total number of items in the cart
        requireActivity().findViewById<NavigationView>(R.id.navigation_view)
            .menu.findItem(R.id.nav_cart).title = "My Cart ($totalItems)"

        // Update the cart item title with the total number of items in the cart
        requireActivity().findViewById<BottomNavigationView>(R.id.bottomNV)
            .menu.findItem(R.id.nav_cart).title = "My Cart ($totalItems)"

//        Log.d("cartquantites", "$cartQuantities")
    }


    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }
}
