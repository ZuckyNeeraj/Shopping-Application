package com.example.shoppingapplication.auth.view

/**
 * This is an auth activity.
 * Both login and sign up fragments will be render over this activity.
 * @author Neeraj Mahapatra
 */

import android.annotation.SuppressLint
import android.content.Intent
import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import com.example.shoppingapplication.R
import com.example.shoppingapplication.databinding.ActivityAuthBinding
import com.example.shoppingapplication.login.view.LogInFragment
import com.example.shoppingapplication.signUp.view.SignUpFragment
import com.example.shoppingapplication.homePage.HomePageActivity
import com.google.firebase.auth.FirebaseAuth

/**
 * This is an authentication activity.
 * If user is already logged in user will be redirected to Home page activity.
 * If not than it will render the Home page activity.
 */

@SuppressLint("StaticFieldLeak")
lateinit var binding: ActivityAuthBinding
lateinit var auth: FirebaseAuth

class AuthActivity : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityAuthBinding.inflate(layoutInflater)
        setContentView(binding.root)

        inits()
        auth.currentUser?.let {
            startActivity(Intent(this, HomePageActivity::class.java))
            finish()
        } ?: loadTheFragments()
    }

    private fun inits() {
        auth = FirebaseAuth.getInstance()
    }


    /**
     * This method will load the fragments depending upon the menu items and by default
     * it will render the login fragment.
     * @return null
     */

    private fun loadTheFragments() {
        binding.navView.setOnNavigationItemSelectedListener {
            val fragment = when (it.itemId) {
                R.id.navigation_login -> LogInFragment()
                R.id.navigation_signup -> SignUpFragment()
                else -> null
            }
            fragment?.let { f ->
                supportFragmentManager.beginTransaction()
                    .replace(R.id.authFrameFl, f)
                    .commit()
                return@setOnNavigationItemSelectedListener true
            }
            false
        }
        supportFragmentManager.beginTransaction()
            .replace(R.id.authFrameFl, LogInFragment())
            .commit()
    }
}