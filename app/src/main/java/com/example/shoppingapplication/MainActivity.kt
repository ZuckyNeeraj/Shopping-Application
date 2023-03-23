package com.example.shoppingapplication

import android.animation.ObjectAnimator
import android.annotation.SuppressLint
import android.content.Context
import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.os.Handler
import android.util.Log
import android.view.animation.AlphaAnimation
import android.view.animation.Animation
import android.view.animation.LinearInterpolator
import android.view.animation.ScaleAnimation
import android.widget.ImageView
import com.example.shoppingapplication.databinding.ActivityMainBinding
import com.example.shoppingapplication.ui.auth.AuthActivity

@SuppressLint("StaticFieldLeak")
private lateinit var binding: ActivityMainBinding
// Create a HashMap to store the quantities of each product in the cart
val cartQuantities = HashMap<Int?, Pair<String, Int>>()
class MainActivity : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityMainBinding.inflate(layoutInflater)
        setContentView(binding.root)
        animateSplash()
    }

    /**
     * This function will help to create the splash screen.
     * It will also animate the logo.
     * After the delay of 3 seconds it will open the auth activity.
     * @return null
     */
    private fun animateSplash() {
        val myImageView: ImageView = binding.imageView

        // Create a zoom-in animation for the logo
        val zoomAnimation = ScaleAnimation(
            0.5f,
            1.5f,
            0.5f,
            1.5f,
            Animation.RELATIVE_TO_SELF,
            0.5f,
            Animation.RELATIVE_TO_SELF,
            0.5f
        )
        zoomAnimation.duration = 1000
        zoomAnimation.fillAfter = true
        myImageView.startAnimation(zoomAnimation)

        // Create a fade-in animation for the splash screen
        val fadeInAnimation = AlphaAnimation(0.0f, 1.0f)
        fadeInAnimation.duration = 1000
        binding.root.startAnimation(fadeInAnimation)

        // Create a Handler to delay the redirect to AuthActivity
        Handler().postDelayed({
            // Start AuthActivity
            val intent = Intent(this@MainActivity, AuthActivity::class.java)
            startActivity(intent)

            // Finish MainActivity so that it cannot be returned to by pressing the back button
            finish()

            // Create a fade-out animation for the splash screen
            val fadeOutAnimation = AlphaAnimation(1.0f, 0.0f)
            fadeOutAnimation.duration = 1000
            binding.root.startAnimation(fadeOutAnimation)
        }, 3000) // Delay for 3 seconds (3000 milliseconds)
    }
}
