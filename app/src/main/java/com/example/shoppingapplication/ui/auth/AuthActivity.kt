package com.example.shoppingapplication.ui.auth

import android.annotation.SuppressLint
import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.widget.Button
import com.example.shoppingapplication.R
import com.example.shoppingapplication.databinding.ActivityAuthBinding
import com.example.shoppingapplication.ui.homepage.HomePageActivity
import com.google.firebase.auth.FirebaseAuth

@SuppressLint("StaticFieldLeak")
private lateinit var binding: ActivityAuthBinding
private lateinit var auth: FirebaseAuth

/**
 * This is an authentication activity.
 * If user is already logged in user will be redirected to Home page activity.
 * If not than it will render the Home page activity.
 */

class AuthActivity : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityAuthBinding.inflate(layoutInflater)
        setContentView(binding.root)

        auth = FirebaseAuth.getInstance()
        val currentUser = auth.currentUser
        if (currentUser != null) {
            val intent = Intent(this, HomePageActivity::class.java)
            startActivity(intent)
            finish()
        }else{
            loadTheFragments()
        }
    }


    /**
     * This method will load the fragments depending upon the menu items and by default
     * it will render the login fragment.
     * @return null
     */

    private fun loadTheFragments() {
        binding.navView.setOnNavigationItemSelectedListener { menuItem ->
            when (menuItem.itemId) {
                R.id.navigation_login -> {
                    supportFragmentManager.beginTransaction()
                        .replace(R.id.authFrameFl, LogInFragment())
                        .commit()
                    return@setOnNavigationItemSelectedListener true
                }
                R.id.navigation_signup -> {
                    supportFragmentManager.beginTransaction()
                        .replace(R.id.authFrameFl, SignUpFragment())
                        .commit()
                    return@setOnNavigationItemSelectedListener true
                }
            }
            false
        }

        // Show the login fragment by default
        supportFragmentManager.beginTransaction()
            .replace(R.id.authFrameFl, LogInFragment())
            .commit()
    }
}