package com.example.shoppingapplication.login.view

/**
 * This fragment will deal with the login of user.
 * It will be loaded over auth activity.
 * Both sign up and login fragment will render over the auth activity.
 * @author Neeraj Mahapatra
 */


import android.annotation.SuppressLint
import android.app.Activity
import android.content.ContentValues.TAG
import android.content.Intent
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
import android.widget.Toast
import androidx.activity.result.contract.ActivityResultContracts
import com.example.shoppingapplication.R
import com.example.shoppingapplication.databinding.FragmentLogInBinding
import com.example.shoppingapplication.homePage.HomePageActivity
import com.google.android.gms.auth.api.signin.GoogleSignIn
import com.google.android.gms.auth.api.signin.GoogleSignInAccount
import com.google.android.gms.auth.api.signin.GoogleSignInClient
import com.google.android.gms.auth.api.signin.GoogleSignInOptions
import com.google.android.gms.tasks.Task
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.auth.GoogleAuthProvider
import com.pranavpandey.android.dynamic.toasts.DynamicToast

class LogInFragment : Fragment() {

    private var _binding: FragmentLogInBinding? = null
    private val binding get() = _binding!!

    private val onClickListener = View.OnClickListener setOnClickListener@{ view ->
        when (view) {
            binding.logInButton ->{
                val email: String = logInEmail.text.toString()
                val password: String = logInPassword.text.toString()

                if (email.isEmpty() || password.isEmpty()) {
                    activity?.let { it1 ->
                        DynamicToast.makeError(
                            it1,
                            "Please fill in both email and password fields to continue.",
                            Toast.LENGTH_SHORT,
                        ).show()
                    }
                    return@setOnClickListener
                }

                auth.signInWithEmailAndPassword(email, password)
                    .addOnCompleteListener() { task ->
                        if (task.isSuccessful) {
                            // Sign in success, update UI with the signed-in user's information
                            Log.d(TAG, "signInWithEmail:success")
                            val user = auth.currentUser
                            val intent = Intent(activity, HomePageActivity::class.java)
                            startActivity(intent)
                        } else {
                            // If sign in fails, display a message to the user.
                            Log.w(TAG, "signInWithEmail:failure", task.exception)
                            val message = task.exception?.message ?: "An unknown error occurred."
                            when {
                                message.contains("no user record") -> {
                                    activity?.let { it1 ->
                                        DynamicToast.makeError(
                                            it1,
                                            "The email entered is not registered. Please sign up or try again with a registered email.",
                                            Toast.LENGTH_SHORT
                                        ).show()
                                    }
                                }
                                message.contains("password is invalid") -> {
                                    activity?.let { it1 ->
                                        DynamicToast.makeError(
                                            it1,
                                            "The password entered is incorrect. Please try again with the correct password.",
                                            Toast.LENGTH_SHORT
                                        ).show()
                                    }
                                }
                                else -> {
                                    Toast.makeText(activity, message, Toast.LENGTH_SHORT).show()
                                }
                            }
                        }
                    }
            }
        }
    }


    private lateinit var logInButton: Button
    private lateinit var logInEmail: EditText
    private lateinit var logInPassword: EditText
    private lateinit var auth: FirebaseAuth

    @SuppressLint("StaticFieldLeak")
    private lateinit var googleSignInClient: GoogleSignInClient
    private lateinit var googleSignInImageView: ImageView

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        // Inflate the layout for this fragment
        _binding = FragmentLogInBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        inits()
        gsoAndGoogleSignIn()
        disableLogInButtonAtFirst()
        setListeners()
    }

    private fun inits() {
        logInEmail = binding.logInEmail
        logInPassword = binding.logInPassword
        logInButton = binding.logInButton
        auth = FirebaseAuth.getInstance()
        googleSignInImageView = binding.googleLogIn
    }

    /**
     * This will check email and password if both are not null,
     * it will authenticate using firebase, if everything is correct,
     * it will render the Home Activity.
     * @return Home Page Activity
     */
    private fun setListeners() {
        binding.logInButton.setOnClickListener(onClickListener)

    }


    /**
     * This method will hide the login button until email and password are not filled
     * @return null
     */
    private fun disableLogInButtonAtFirst() {

        // Disable the SignUpButton initially
        logInButton.isEnabled = false

        // Add text change listeners to each EditText field
        val textWatcher = object : TextWatcher {
            override fun beforeTextChanged(s: CharSequence?, start: Int, count: Int, after: Int) {}

            override fun onTextChanged(s: CharSequence?, start: Int, before: Int, count: Int) {
                // Check if all three EditText fields are not empty
                logInButton.isEnabled = (!logInEmail.text.isNullOrEmpty()
                        && !logInPassword.text.isNullOrEmpty())
            }

            override fun afterTextChanged(s: Editable?) {}
        }

        logInEmail.addTextChangedListener(textWatcher)
        logInPassword.addTextChangedListener(textWatcher)
    }

    /**
     * Below methods are to deal with the google single sign on feature.
     * @return Allow user to sign via google SSO.
     */

    private fun gsoAndGoogleSignIn() {
        val gso = GoogleSignInOptions.Builder(GoogleSignInOptions.DEFAULT_SIGN_IN)
            .requestIdToken(getString(R.string.default_web_client_id))
            .requestEmail()
            .build()

        googleSignInClient = context?.let { GoogleSignIn.getClient(it, gso) }!!

        googleSignInImageView.setOnClickListener {
            signInGoogle()
        }
    }

    private fun signInGoogle() {
        val signInIntent = googleSignInClient.signInIntent
        launcher.launch(signInIntent)
    }

    private val launcher = registerForActivityResult(
        ActivityResultContracts.StartActivityForResult()
    ) { result ->
        if (result.resultCode == Activity.RESULT_OK) {
            val task = GoogleSignIn.getSignedInAccountFromIntent(result.data)
            handleResults(task)
        }
    }

    private fun handleResults(task: Task<GoogleSignInAccount>) {
        if (task.isSuccessful) {
            val account: GoogleSignInAccount? = task.result
            if (account != null) {
                updateUI(account)
            }
        } else {
            Toast.makeText(context, task.exception.toString(), Toast.LENGTH_SHORT).show()
        }
    }

    private fun updateUI(account: GoogleSignInAccount) {
        val credential = GoogleAuthProvider.getCredential(account.idToken, null)
        auth.signInWithCredential(credential).addOnCompleteListener {
            if (it.isSuccessful) {
                val intent = Intent(activity, HomePageActivity::class.java)
                startActivity(intent)
            } else {
                Toast.makeText(context, it.exception.toString(), Toast.LENGTH_SHORT).show()
            }
        }
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }
}