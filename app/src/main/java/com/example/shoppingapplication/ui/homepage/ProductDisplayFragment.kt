package com.example.shoppingapplication.ui.homepage

import android.annotation.SuppressLint
import android.content.ContentValues
import android.content.Context
import android.os.Bundle
import android.util.Log
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.view.animation.AnimationUtils
import android.widget.ImageView
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.example.shoppingapplication.R
import com.example.shoppingapplication.data.productsItem
import com.example.shoppingapplication.databinding.FragmentLogInBinding
import com.example.shoppingapplication.databinding.FragmentProductDisplayBinding
import com.example.shoppingapplication.repository.MyAdapter
import com.example.shoppingapplication.ui.auth.LogInFragment
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.database.DataSnapshot
import com.google.firebase.database.DatabaseError
import com.google.firebase.database.FirebaseDatabase
import com.google.firebase.database.ValueEventListener


class ProductDisplayFragment : Fragment() {

    private var _binding: FragmentProductDisplayBinding? = null
    private val binding get() = _binding!!

    // add a variable to hold the loading image view
    private lateinit var loadingImage: ImageView

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        // Inflate the layout for this fragment
        _binding = FragmentProductDisplayBinding.inflate(inflater, container, false)
        getDataFromFirebase()
        return binding.root
    }

    /**
     * This method will get the data from the firebase database and set it to the
     * recycler view.
     */
    private fun getDataFromFirebase() {
        // Get a reference to your Firebase database
        val database = FirebaseDatabase.getInstance()
        val myRef = database.getReference("products")

        // Create a list to hold your model objects
        val data = mutableListOf<productsItem>()

        // Set up the RecyclerView and adapter
        val recyclerView = binding.rvToShowItems
        recyclerView.layoutManager = LinearLayoutManager(context)
        val adapter = MyAdapter(data as ArrayList<productsItem>)
        recyclerView.adapter = adapter

        // Query the Firebase database for your data
        myRef.addValueEventListener(object : ValueEventListener {
            @SuppressLint("NotifyDataSetChanged")
            override fun onDataChange(dataSnapshot: DataSnapshot) {
                data.clear()
                for (snapshot in dataSnapshot.children) {
                    val model = snapshot.getValue(productsItem::class.java)
                    data.add(model!!)
                }
                adapter.notifyDataSetChanged()
            }

            override fun onCancelled(error: DatabaseError) {
                // Failed to read value
                Log.w(ContentValues.TAG, "Failed to read value.", error.toException());
            }
        })
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }
}