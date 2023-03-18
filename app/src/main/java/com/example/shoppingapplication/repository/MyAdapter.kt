package com.example.shoppingapplication.repository

import android.annotation.SuppressLint
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.view.animation.AnimationUtils
import android.widget.ImageView
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import com.bumptech.glide.load.resource.bitmap.CircleCrop
import com.example.shoppingapplication.R
import com.example.shoppingapplication.data.productsItem


class MyAdapter(private val productList: ArrayList<productsItem>):
    RecyclerView.Adapter<MyAdapter.MyViewHolder>() {



    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): MyViewHolder {
        val itemView = LayoutInflater.from(parent.context).inflate(R.layout.item_display, parent, false)
        return MyViewHolder(itemView)
    }

    override fun getItemCount(): Int {
        return productList.size
    }

    @SuppressLint("SetTextI18n")
    override fun onBindViewHolder(holder: MyViewHolder, position: Int) {
        val currentProduct = productList[position]

        holder.itemName.text = currentProduct.title
        val priceValue: Double? = currentProduct.price
        holder.itemPrice.text = "$priceValue ₹"
        holder.itemRating.text = currentProduct.rating?.rate.toString()
        // Set the product image using Glide
        Glide.with(holder.itemView.context)
            .load(currentProduct.image)
            .placeholder(R.drawable.loading)
            .transform(CircleCrop())
            .into(holder.itemImage)

        holder.itemView.startAnimation(
            AnimationUtils.loadAnimation(holder.itemView.context, R.anim.anim_1))
    }


    class MyViewHolder(itemView: View): RecyclerView.ViewHolder(itemView){
        val itemImage: ImageView = itemView.findViewById(R.id.item_display_product_image)
        val itemName: TextView = itemView.findViewById(R.id.item_display_product_name)
        val itemPrice: TextView = itemView.findViewById(R.id.item_display_product_price)
        val itemRating: TextView = itemView.findViewById(R.id.item_display_product_rating)
    }
}