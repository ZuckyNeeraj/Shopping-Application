package com.example.shoppingapplication.ui.homepage

import android.annotation.SuppressLint
import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.widget.Button
import com.example.shoppingapplication.R
import com.example.shoppingapplication.databinding.ActivityHomePageBinding
import com.example.shoppingapplication.ui.auth.AuthActivity
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.database.*

@SuppressLint("StaticFieldLeak")
private lateinit var binding: ActivityHomePageBinding

@SuppressLint("StaticFieldLeak")
private lateinit var logOutButton: Button
private lateinit var auth: FirebaseAuth


class HomePageActivity : AppCompatActivity(){
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityHomePageBinding.inflate(layoutInflater)
        setContentView(binding.root)
        loadTheInitialFragments()
        logOut()
    }


    /**
     * This method will load the very first fragment.
     */
    private fun loadTheInitialFragments() {
        supportFragmentManager.beginTransaction()
            .replace(R.id.recyclerViewFrameLayout, ProductDisplayFragment())
            .commit()
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
            val intent = Intent(this, AuthActivity::class.java)
            startActivity(intent)
        }

        /**
         * This will trigger when log out button will be clicked.
         * As log out button will be clicked, it will redirect to Auth Activity.
         */
        logOutButton.setOnClickListener {
            FirebaseAuth.getInstance().signOut()
            val intent = Intent(this, AuthActivity::class.java)
            startActivity(intent)
        }
    }
}

