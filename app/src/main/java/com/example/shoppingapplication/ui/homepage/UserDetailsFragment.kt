package com.example.shoppingapplication.ui.homepage

/**
 * This is the user details fragment.
 * This fragment is also deals with the log out feature.
 * @author Neeraj Mahapatra
 */


import android.annotation.SuppressLint
import android.content.Intent
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Button
import androidx.fragment.app.Fragment
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.example.shoppingapplication.R
import com.example.shoppingapplication.databinding.FragmentProductDisplayBinding
import com.example.shoppingapplication.databinding.FragmentUserDetailsBinding
import com.example.shoppingapplication.repository.NavigationImageAdapter
import com.example.shoppingapplication.ui.auth.AuthActivity
import com.google.firebase.auth.FirebaseAuth


@SuppressLint("StaticFieldLeak")
private lateinit var logOutButton: Button
private lateinit var auth: FirebaseAuth

class UserDetailsFragment : Fragment() {

    private var _binding: FragmentUserDetailsBinding? = null
    private val binding get() = _binding!!
    private val viewOnClickListener = View.OnClickListener { view ->
        when (view) {
            binding.logOutButton ->{
                FirebaseAuth.getInstance().signOut()
                val intent = Intent(context, AuthActivity::class.java)
                startActivity(intent)
            }
        }
    }
    @SuppressLint("MissingInflatedId")
    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        // Inflate the layout for this fragment
        _binding = FragmentUserDetailsBinding.inflate(inflater, container, false)
        logOut()
        return binding.root
    }

    /**
     * Log out functionality.
     * @return Auth Activity.
     */
    private fun logOut() {
        logOutButton = binding.logOutButton
        auth = FirebaseAuth.getInstance()

        val currentUser = auth.currentUser
        if (currentUser == null) {
            val intent = Intent(context, AuthActivity::class.java)
            startActivity(intent)
        }

        /**
         * This will trigger when log out button will be clicked.
         * As log out button will be clicked, it will redirect to Auth Activity.
         */
        logOutButton.setOnClickListener(viewOnClickListener)
    }
}