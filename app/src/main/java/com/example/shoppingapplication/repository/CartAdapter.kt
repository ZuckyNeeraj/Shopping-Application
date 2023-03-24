package com.example.shoppingapplication.repository

/**
 * This is an Adapter for the list view.
 * It will extract the data from the shared prefs and inflate in the add to cart fragment using list view.
 * @author Neeraj Mahapatra
 */

import android.annotation.SuppressLint
import android.content.Context
import android.content.SharedPreferences
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.BaseAdapter
import android.widget.Button
import android.widget.TextView
import com.example.shoppingapplication.R
import com.google.android.material.bottomnavigation.BottomNavigationView
import com.google.android.material.navigation.NavigationView
import com.google.gson.Gson



class CartAdapter(
    private val context: Context,
    var cartQuantities: MutableMap<Int, Pair<String, Int>>,
    private val sharedPreferences: SharedPreferences
) : BaseAdapter() {

    override fun getCount(): Int {
        return cartQuantities.size
    }

    override fun getItem(position: Int): Any {
        return cartQuantities.values.toList()[position]
    }

    override fun getItemId(position: Int): Long {
        return position.toLong()
    }

    @SuppressLint("SetTextI18n")
    override fun getView(position: Int, convertView: View?, parent: ViewGroup?): View? {
        var view = convertView
        if (view == null) {
            view = LayoutInflater.from(context).inflate(R.layout.cart_item_layout, parent, false)
        }

        val productId = cartQuantities.keys.toList()[position]
        val product = cartQuantities.values.toList()[position]

        view?.findViewById<TextView>(R.id.product_name)?.text = product.first
        view?.findViewById<TextView>(R.id.product_quantity)?.text = " ${product.second}"

        /*This method will be triggered on the click of delete button.
        * It will delete the data of particular item.
        */
        view?.findViewById<Button>(R.id.cart_item_delete_button)?.setOnClickListener{
            cartQuantities.remove(productId)
            sharedPreferences.edit().putString("my_hashmap_key", Gson().toJson(cartQuantities)).apply()
            notifyDataSetChanged()
        }
        return view
    }
}

