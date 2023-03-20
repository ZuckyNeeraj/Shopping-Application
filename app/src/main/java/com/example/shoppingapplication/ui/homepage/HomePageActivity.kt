package com.example.shoppingapplication.ui.homepage

/**
 * This is the home page activity that will be providing navigation feature.
 * As well as logout functionality is also added in this section.
 * @author Neeraj Mahapatra
 */

import android.annotation.SuppressLint
import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.view.LayoutInflater
import android.view.Menu
import android.view.MenuInflater
import android.view.MenuItem
import android.widget.ArrayAdapter
import android.widget.Button
import android.widget.LinearLayout
import android.widget.ListView
import androidx.appcompat.app.ActionBarDrawerToggle
import androidx.core.view.GravityCompat
import androidx.drawerlayout.widget.DrawerLayout
import androidx.fragment.app.Fragment
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.example.shoppingapplication.R
import com.example.shoppingapplication.databinding.ActivityHomePageBinding
import com.example.shoppingapplication.repository.NavigationImageAdapter
import com.example.shoppingapplication.ui.auth.AuthActivity
import com.google.android.material.navigation.NavigationView
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.database.*

@SuppressLint("StaticFieldLeak")
private lateinit var binding: ActivityHomePageBinding

@SuppressLint("StaticFieldLeak")
private lateinit var logOutButton: Button
private lateinit var auth: FirebaseAuth
private lateinit var drawerlayout: DrawerLayout
private lateinit var actionBarDrawerToggle: ActionBarDrawerToggle
private lateinit var navigationView: NavigationView
private val productDisplayFrag = ProductDisplayFragment()
private val cartFrag = AddToCartFragment()
private val userDetailFrag = UserDetailsFragment()
private val trackOrderFrag = TrackOrderFragment()

class HomePageActivity : AppCompatActivity() {
    @SuppressLint("InflateParams")
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityHomePageBinding.inflate(layoutInflater)
        setContentView(binding.root)


        setCurrentFragment(productDisplayFrag)
        drawer_func()
        setRecyclerViewJumbotronImages()
        logOut()
    }

    private fun setRecyclerViewJumbotronImages() {
        val recyclerView = binding.navigationRv
        val layoutManager = LinearLayoutManager(this, LinearLayoutManager.HORIZONTAL, true)
        recyclerView.layoutManager = layoutManager

        // get images from the home page activity
        val images = listOf(R.drawable.offer3, R.drawable.offer3, R.drawable.offer3)
        recyclerView.adapter = NavigationImageAdapter(images)
    }

    override fun onBackPressed() {
        drawerlayout = binding.drawerLayout
        if (drawerlayout.isDrawerOpen(GravityCompat.START)) {
            drawerlayout.closeDrawer(GravityCompat.START)
        } else {
            super.onBackPressed()
        }
    }

    /**
     * Navigation drawer function that will be triggered on clicking the
     * hamburger image.
     * @return Navigation Drawer
     */
    private fun drawer_func() {
        // Set the toolbar as the action bar
        val toolbar = binding.hamburgerMenu

        // Set a click listener on the navigation icon to open the drawer
        toolbar.setOnClickListener {
            drawerlayout.openDrawer(GravityCompat.START)

        }
        drawerlayout = binding.drawerLayout
        navigationView = binding.navigationView

        actionBarDrawerToggle =
            ActionBarDrawerToggle(this, drawerlayout, R.string.nav_open, R.string.nav_close)

        drawerlayout.addDrawerListener(actionBarDrawerToggle)
        actionBarDrawerToggle.syncState()

        supportActionBar?.setDisplayHomeAsUpEnabled(true)

        navigationView.setNavigationItemSelectedListener { menuItem ->
            when (menuItem.itemId) {
                R.id.nav_prod_display -> {
                    drawerlayout.closeDrawer(GravityCompat.START)
                    setCurrentFragmentOnCLick(productDisplayFrag)
                    true
                }
                R.id.nav_cart -> {
                    drawerlayout.closeDrawer(GravityCompat.START)
                    setCurrentFragmentOnCLick(cartFrag)
                    true
                }
                R.id.nav_user_detail -> {
                    drawerlayout.closeDrawer(GravityCompat.START)
                    setCurrentFragmentOnCLick(userDetailFrag)
                    true
                }
                R.id.nav_track_order -> {
                    drawerlayout.closeDrawer(GravityCompat.START)
                    setCurrentFragmentOnCLick(trackOrderFrag)
                    true
                }
                else -> false
            }
        }
    }

    override fun onOptionsItemSelected(item: MenuItem): Boolean {
        return if (actionBarDrawerToggle.onOptionsItemSelected(item)) {
            true
        } else super.onOptionsItemSelected(item)
    }


    /**
     * This method will load the very first fragment.
     */
    private fun setCurrentFragment(fragment: Fragment) =
        supportFragmentManager.beginTransaction().apply {
            replace(R.id.recyclerViewFrameLayout, fragment)
            commit()
        }

    private fun setCurrentFragmentOnCLick(fragment: Fragment) =
        supportFragmentManager.beginTransaction().apply {
            replace(R.id.recyclerViewFrameLayout, fragment)
            addToBackStack(null)
            commit()
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

