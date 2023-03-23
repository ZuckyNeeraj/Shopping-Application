package com.example.shoppingapplication.repository

import android.annotation.SuppressLint
import android.content.Intent
import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.view.animation.AnimationUtils
import android.widget.Button
import android.widget.EditText
import android.widget.ImageView
import android.widget.TextView
import androidx.appcompat.app.AppCompatActivity
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import com.bumptech.glide.load.resource.bitmap.CircleCrop
import com.example.shoppingapplication.R
import com.example.shoppingapplication.data.productsItem
import androidx.navigation.findNavController
import com.example.shoppingapplication.ui.homepage.AddToCartFragment


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
        // set OnClickListener on plus and minus buttons
        holder.plusButton.setOnClickListener {
            var count = holder.countEditText.text.toString().toInt()
            count++
            holder.countEditText.setText(count.toString())
        }
        holder.minusButton.setOnClickListener {
            var count = holder.countEditText.text.toString().toInt()
            if (count > 0) {
                count--
                holder.countEditText.setText(count.toString())
            }
        }

        holder.addToCartButton.setOnClickListener {
            try {
                val countText = holder.countEditText.text.toString()
                val count = countText.toInt()
                val productName = currentProduct.title

                // Pass the product name and quantity to the AddToCartFragment
                val bundle = Bundle()
                bundle.putInt("count", count)
                bundle.putString("productName", productName)
                currentProduct.id.let { it1 ->
                    if (it1 != null) {
                        bundle.putInt("productId", it1)
                    }
                }

                val addToCartFragment = AddToCartFragment()
                addToCartFragment.arguments = bundle


                val fragmentManager = (holder.itemView.context as AppCompatActivity).supportFragmentManager
                fragmentManager.beginTransaction()
                    .replace(R.id.recyclerViewFrameLayout, addToCartFragment)
                    .addToBackStack(null)
                    .commit()
            } catch (e: Exception) {
                Log.d("Fata", e.toString())
            }
        }




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
        val plusButton: Button = itemView.findViewById(R.id.plus_button)
        val minusButton: Button = itemView.findViewById(R.id.minus_button)
        val countEditText: EditText = itemView.findViewById(R.id.count_edit_text)
        val addToCartButton: Button = itemView.findViewById(R.id.button_add_to_cart)
    }
}