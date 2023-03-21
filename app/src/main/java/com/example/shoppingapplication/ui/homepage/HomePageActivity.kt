package com.example.shoppingapplication.ui.homepage

/**
 * This is the home page activity that will be providing navigation feature.
 * As well as logout functionality is also added in this section.
 * @author Neeraj Mahapatra
 */

import android.annotation.SuppressLint
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.view.MenuItem
import androidx.appcompat.app.ActionBarDrawerToggle
import androidx.core.view.GravityCompat
import androidx.drawerlayout.widget.DrawerLayout
import androidx.fragment.app.Fragment
import androidx.recyclerview.widget.LinearLayoutManager
import com.example.shoppingapplication.R
import com.example.shoppingapplication.databinding.ActivityHomePageBinding
import com.example.shoppingapplication.repository.NavigationImageAdapter
import com.google.android.material.bottomnavigation.BottomNavigationView
import com.google.android.material.navigation.NavigationView
import com.google.firebase.database.*

@SuppressLint("StaticFieldLeak")
private lateinit var binding: ActivityHomePageBinding

@SuppressLint("StaticFieldLeak")
private lateinit var drawerlayout: DrawerLayout
private lateinit var actionBarDrawerToggle: ActionBarDrawerToggle
private lateinit var navigationView: NavigationView
private lateinit var bottomNavView: BottomNavigationView
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
        set_bottom_nav()
        setRecyclerViewJumbotronImages()
    }

    private fun set_bottom_nav() {
        bottomNavView = binding.bottomNV

        bottomNavView.setOnItemSelectedListener { item ->
            when(item.itemId){
                R.id.nav_user_detail -> {
                    setCurrentFragmentOnCLick(userDetailFrag)
                    true
                }
                R.id.nav_prod_display -> {
                    setCurrentFragmentOnCLick(productDisplayFrag)
                    true
                }
                R.id.nav_cart -> {
                    setCurrentFragmentOnCLick(cartFrag)
                    true
                }
                else -> {
                    false
                }
            }
        }

    }


    /**
     * Method to add the jumbotron images inside the recycler view in the navigation view.
     * Using a Navigation Image Adapter to show the images.
     * (This is for offers.)
     * This is clickable and once user will click on it it will take to the offer pages.
     * @return Recycler view with offer images that is horizontally scrollable.
     */
    private fun setRecyclerViewJumbotronImages() {
        val recyclerView = binding.navigationRv
        val layoutManager = LinearLayoutManager(this, LinearLayoutManager.HORIZONTAL, true)
        recyclerView.layoutManager = layoutManager

        // get images from the home page activity
        val images = listOf(R.drawable.offer3, R.drawable.offer3, R.drawable.offer3)
        recyclerView.adapter = NavigationImageAdapter(images)
    }

    /**
     * While we have opened the navigation view, if we press back button it should not close the app.
     * To control it, this method is helpful.
     * @return null
     */
    @Deprecated("Deprecated in Java")
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
     * @param Fragment
     * @return Load the fragment on the Home page activity
     */
    private fun setCurrentFragment(fragment: Fragment) =
        supportFragmentManager.beginTransaction().apply {
            replace(R.id.recyclerViewFrameLayout, fragment)
            commit()
        }

    /**
     * This method will load fragment except the first fragment.(Because we have to put the later fragment to backstack.)
     * @param Fragment
     * @return Load the fragment on the Home page activity
     */
    private fun setCurrentFragmentOnCLick(fragment: Fragment) =
        supportFragmentManager.beginTransaction().apply {
            replace(R.id.recyclerViewFrameLayout, fragment)
            addToBackStack(null)
            commit()
        }
}

