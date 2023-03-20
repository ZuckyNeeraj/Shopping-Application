package com.example.shoppingapplication.repository

import android.annotation.SuppressLint
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.view.animation.AnimationUtils
import android.widget.ImageView
import android.widget.RatingBar
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import com.bumptech.glide.load.resource.bitmap.CircleCrop
import com.example.shoppingapplication.R
import com.example.shoppingapplication.data.productsItem


class MyAdapter(private var productList: ArrayList<productsItem>):
    RecyclerView.Adapter<MyAdapter.MyViewHolder>() {

    var onItemClick: ((productsItem) -> Unit)? = null
    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): MyViewHolder {
        val itemView = LayoutInflater.from(parent.context).inflate(R.layout.item_display, parent, false)
        return MyViewHolder(itemView)
    }

    /**
     * This will set the filtered list. Filtered list is the list of the data
     * that is coming based on the search query the user provides.
     * @param ArrayList<productsItem>
     * @return New updated filtered List
     */
    fun setFilteredList(productList: ArrayList<productsItem>){
        this.productList = productList
        notifyDataSetChanged()
    }

    override fun getItemCount(): Int {
        return productList.size
    }

    @SuppressLint("SetTextI18n")
    override fun onBindViewHolder(holder: MyViewHolder, position: Int) {
        val currentProduct = productList[position]

        holder.itemName.text = currentProduct.title
        //we are storing the price first and then appending via Rs. symbol
        val priceValue: Double? = currentProduct.price
        holder.itemPrice.text = "$priceValue ₹"
        // Set the product image using Glide
        Glide.with(holder.itemView.context)
            .load(currentProduct.image)
            .placeholder(R.drawable.loading)
            .transform(CircleCrop())
            .into(holder.itemImage)

        holder.itemView.startAnimation(
            AnimationUtils.loadAnimation(holder.itemView.context, R.anim.anim_1))

        holder.itemView.setOnClickListener{
            currentProduct.let { it1 -> onItemClick?.invoke(it1) }
        }
    }


    class MyViewHolder(itemView: View): RecyclerView.ViewHolder(itemView){
        val itemImage: ImageView = itemView.findViewById(R.id.item_display_product_image)
        val itemName: TextView = itemView.findViewById(R.id.item_display_product_name)
        val itemPrice: TextView = itemView.findViewById(R.id.item_display_product_price)
    }
}