package com.android.frogtest.ui.activity

import android.content.Intent
import android.os.Bundle
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import com.android.frogtest.R
import com.android.frogtest.Utils
import com.android.frogtest.databinding.ActivitySigninBinding
import com.android.frogtest.ui.fragment.LoginFragment
import com.google.firebase.auth.FirebaseAuth
import dagger.hilt.android.AndroidEntryPoint

@AndroidEntryPoint
class SignInActivity : AppCompatActivity() {

    private lateinit var binding: ActivitySigninBinding
    private lateinit var auth: FirebaseAuth

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivitySigninBinding.inflate(layoutInflater)
        setContentView(binding.root)

        if (savedInstanceState == null) {
            initializeLoginFragment()
        }
        Utils.isInternetAvailable(this) { isConnected ->
            if (isConnected) {
                initializeFirebaseAuth()
                if (auth.currentUser != null) {
                    redirectToMovieListActivity()
                }
            } else {
                showNoInternetToast()
            }
        }
    }

    private fun initializeLoginFragment() {
        supportFragmentManager.beginTransaction()
            .replace(R.id.containers, LoginFragment())
            .commitNow()
    }

    private fun initializeFirebaseAuth() {
        auth = FirebaseAuth.getInstance()
    }

    private fun redirectToMovieListActivity() {
        startActivity(Intent(this, MovieListActivity::class.java))
        finish()
    }

    private fun showNoInternetToast() {
        Toast.makeText(this@SignInActivity, getString(R.string.no_internet_available), Toast.LENGTH_SHORT).show()
    }
}