package com.example.shoppingapplication.repository

import android.annotation.SuppressLint
import android.content.Context
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.BaseAdapter
import android.widget.TextView
import com.example.shoppingapplication.R

class CartAdapter(
    private val context: Context,
    private val cartQuantities: MutableMap<Int, Pair<String, Int>>
) : BaseAdapter() {

    override fun getCount(): Int {
        return cartQuantities.size
    }

    override fun getItem(position: Int): Any {
        return cartQuantities.toList()[position]
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
        val product = cartQuantities.toList()[position].second
        view?.findViewById<TextView>(R.id.product_name)?.text = product.first
        view?.findViewById<TextView>(R.id.product_quantity)?.text = " ${product.second}"
        return view
    }
}