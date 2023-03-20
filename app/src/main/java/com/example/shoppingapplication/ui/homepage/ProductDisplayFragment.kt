package com.example.shoppingapplication.ui.homepage

/**
 * This fragment will used to display the product inside the fragment.
 * This will be also render inside Home Page Activity.
 * @author Neeraj Mahapatra
 */

import android.annotation.SuppressLint
import android.content.ContentValues
import android.os.Bundle
import android.util.Log
import android.util.TypedValue
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.appcompat.widget.SearchView
import androidx.core.content.ContextCompat
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.example.shoppingapplication.R
import com.example.shoppingapplication.data.productsItem
import com.example.shoppingapplication.databinding.FragmentProductDisplayBinding
import com.example.shoppingapplication.repository.MyAdapter
import com.google.firebase.database.DataSnapshot
import com.google.firebase.database.DatabaseError
import com.google.firebase.database.FirebaseDatabase
import com.google.firebase.database.ValueEventListener
import com.pranavpandey.android.dynamic.toasts.DynamicToast
import java.util.*
import kotlin.collections.ArrayList


class ProductDisplayFragment : Fragment() {

    private var _binding: FragmentProductDisplayBinding? = null
    private val binding get() = _binding!!
    private lateinit var data: ArrayList<productsItem>
    private lateinit var recyclerView: RecyclerView
    private lateinit var adapter: MyAdapter
    private lateinit var searchView: SearchView
    private lateinit var filteredList: ArrayList<productsItem>

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        _binding = FragmentProductDisplayBinding.inflate(inflater, container, false)
        inits()
        getDataFromFirebase()
        searchFuntionality()
        return binding.root
    }

    /**
     * This method will initialize all the required resources.
     * @return Initialize all the resources.
     */
    private fun inits(){
        data = mutableListOf<productsItem>() as ArrayList<productsItem>
        recyclerView = binding.rvToShowItems
        adapter = MyAdapter(data)
        searchView = binding.searchView
    }

    /**
     * Below two methods are for searching purpose.
     * Based on the input in the search view it will search and display the product list.
     * @return Required product lists based on query.
     */
    private fun searchFuntionality() {
        searchView.clearFocus()
        searchView.setOnQueryTextListener(object: SearchView.OnQueryTextListener{
            override fun onQueryTextSubmit(query: String?): Boolean {
                return false
            }

            override fun onQueryTextChange(newText: String?): Boolean {
                filterlist(newText)
                return true
            }

        })
    }

    private fun filterlist(query: String?){
        if(query!=null){
            val filteredList = ArrayList<productsItem>()
            for(i in data){
                if(i.title?.lowercase(Locale.ROOT)?.contains(query) == true){
                    filteredList.add(i)
                }
            }

            if(filteredList.isEmpty()){
                context?.let { DynamicToast.makeWarning(it, "No Data Found", Toast.LENGTH_SHORT).show() }
            }else{
                adapter.setFilteredList(filteredList)
            }
        }
    }

    /**
     * This method will get the data from the firebase database and set it to the
     * recycler view.
     * @return Set the data that is received from the firebase database.
     */
    private fun getDataFromFirebase() {
        // Get a reference to your Firebase database
        val database = FirebaseDatabase.getInstance()
        val myRef = database.getReference("products")


        recyclerView.layoutManager = LinearLayoutManager(context)
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

        adapter.onItemClick = { product ->
            val bundle = Bundle()
            bundle.putSerializable("product", product)
            val fragment = DetailProductDisplayFragment()
            fragment.arguments = bundle
            val transaction = requireActivity().supportFragmentManager.beginTransaction()
            transaction.replace(R.id.recyclerViewFrameLayout, fragment)
            transaction.addToBackStack(null)
            transaction.commit()
        }
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }
}