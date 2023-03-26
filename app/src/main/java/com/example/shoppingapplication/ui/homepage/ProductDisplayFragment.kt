package com.example.shoppingapplication.ui.homepage

/**
 * This fragment will used to display the product inside the fragment.
 * This will be also render inside Home Page Activity.
 * @author Neeraj Mahapatra
 */

import android.annotation.SuppressLint
import android.app.Dialog
import android.content.ContentValues
import android.content.Intent
import android.net.Uri
import android.os.Bundle
import android.text.Editable
import android.text.TextWatcher
import android.util.Log
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Button
import android.widget.EditText
import android.widget.ImageView
import android.widget.TextView
import android.widget.Toast
import androidx.appcompat.widget.SearchView
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
import android.content.Context


private var reportName: String = "Neeraj"
private var reportDescription: String = "Desc"

class ProductDisplayFragment : Fragment() {

    private var _binding: FragmentProductDisplayBinding? = null
    private val binding get() = _binding!!
    private lateinit var data: ArrayList<productsItem>
    private lateinit var recyclerView: RecyclerView
    private lateinit var adapter: MyAdapter
    private lateinit var searchView: SearchView
    private lateinit var filteredList: ArrayList<productsItem>
    private lateinit var reportImageViewButton: ImageView

    private val onClickListener = View.OnClickListener { view ->
        when (view) {
            binding.reportButton -> {
                // Create a new dialog
                val reportDialog = context?.let { it1 -> Dialog(it1) }
                reportDialog?.setContentView(R.layout.report_form)
                val width = ViewGroup.LayoutParams.MATCH_PARENT
                val height = ViewGroup.LayoutParams.WRAP_CONTENT

                if (reportDialog != null) {
                    reportDialog.window?.setLayout(width, height)
                    // Show the dialog
                    reportDialog.show()
                }

                val reportOkButton = reportDialog?.findViewById<ImageView>(R.id.report_ok_button)
                val reportUserNameEditText = reportDialog?.findViewById<EditText>(R.id.report_user_name)
                val reportUserDescEditText = reportDialog?.findViewById<EditText>(R.id.report_user_desc)

                // Disable the Next button initially
                reportOkButton?.isEnabled = false

                // Add a text change listener to both EditTexts
                reportUserNameEditText?.addTextChangedListener(object : TextWatcher {
                    override fun afterTextChanged(s: Editable?) {
                        // Check if both EditTexts have some text entered
                        reportOkButton?.isEnabled = !s.isNullOrBlank() && !reportUserDescEditText?.text.isNullOrBlank()
                    }

                    override fun beforeTextChanged(s: CharSequence?, start: Int, count: Int, after: Int) {}

                    override fun onTextChanged(s: CharSequence?, start: Int, before: Int, count: Int) {}
                })

                reportUserDescEditText?.addTextChangedListener(object : TextWatcher {
                    override fun afterTextChanged(s: Editable?) {
                        // Check if both EditTexts have some text entered
                        reportOkButton?.isEnabled = !s.isNullOrBlank() && !reportUserNameEditText?.text.isNullOrBlank()
                    }

                    override fun beforeTextChanged(s: CharSequence?, start: Int, count: Int, after: Int) {}

                    override fun onTextChanged(s: CharSequence?, start: Int, before: Int, count: Int) {}
                })

                reportOkButton?.setOnClickListener {
                    reportName = reportUserNameEditText?.text.toString()
                    reportDescription = reportUserDescEditText?.text.toString()
                    reportDialog.dismiss()
                    show_report_details()
                }

                reportDialog?.findViewById<ImageView>(R.id.report_close_image)?.setOnClickListener {
                    reportDialog.dismiss()
                }
            }
        }
    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        _binding = FragmentProductDisplayBinding.inflate(inflater, container, false)
        return binding.root
    }

    /**
     * Once the view is created assigning the menu resources image.
     * also activating the drawer func() that is defined in the Home Page Activity.
     * @return Menu Image, triggers drawer function
     */
    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        val imageView = requireActivity().findViewById<ImageView>(R.id.hamburger_menu)
        imageView.setImageResource(R.drawable.green_menu)
        val homePageActivity= activity as HomePageActivity
        homePageActivity.drawer_func()

        inits()
        getDataFromFirebase()
        searchFuntionality()
        setListeners()
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
        reportImageViewButton = binding.reportButton
    }


    /**
     * This method will be triggered on clicking report Image.
     * A form will be opened that can take the issue of the user.
     * On clicking the next button it will pass all the data to the next dialog.
     * User can verify it and take some actions
     * @return dialog box with form.
     */
    private fun setListeners() {
        reportImageViewButton.setOnClickListener(onClickListener)
    }


    /**
     * This method will be open and show the data that have been shared by the user
     * earlier.
     * @return new dialog box, also if send is pressed it should be send to the manufacturer.
     */
    private fun show_report_details() {
        val nextDialog = context?.let { Dialog(it) }
        nextDialog?.setContentView(R.layout.report_response)
        val width = ViewGroup.LayoutParams.MATCH_PARENT
        val height = ViewGroup.LayoutParams.WRAP_CONTENT

        if (nextDialog != null) {
            nextDialog.window?.setLayout(width, height)
            nextDialog.findViewById<TextView>(R.id.report_response_name).text = reportName
            nextDialog.findViewById<TextView>(R.id.report_response_desc).text = reportDescription
            // Show the dialog
            nextDialog.show()
        }

        /**
         * This will be triggered on the snd button and it will send mail to the owner about
         * the problems that is faced by the user.
         */
        nextDialog?.findViewById<ImageView>(R.id.report_send_button)?.setOnClickListener{
            val recipient = "neeraj.mahapatra@mindstix.com"
            val subject = "Report"
            val body1 = reportName.toString()
            val body2 = reportDescription.toString()

            val intent = Intent(Intent.ACTION_SENDTO).apply {
                data = Uri.parse("mailto:")
                putExtra(Intent.EXTRA_EMAIL, arrayOf(recipient))
                putExtra(Intent.EXTRA_SUBJECT, subject)
                putExtra(Intent.EXTRA_TEXT, "Name: $body1\n\n Issue: $body2")
            }

            if (intent.resolveActivity(requireActivity().packageManager) != null) {
                startActivity(intent)
            }
        }

        nextDialog?.findViewById<ImageView>(R.id.response_close_image)?.setOnClickListener {
            nextDialog.dismiss()
        }

    }




    /**
     * Below two methods are for searching purpose.
     * Based on the input in the search view it will search and display the product list.
     * @return Required product lists based on query.
     */
    private fun searchFuntionality() {
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
            filteredList = ArrayList<productsItem>()
            for(i in data){
                if(i.title?.lowercase(Locale.ROOT)?.contains(query) == true){
                    filteredList.add(i)
                }
            }


            if(filteredList.isEmpty()){
                Toast.makeText(context, "No Data Found", Toast.LENGTH_SHORT).show()
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


        /**
         * this will activate when we click on the single product in the
         * product list page.
         * @return New fragment containing detail of the product
         */
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