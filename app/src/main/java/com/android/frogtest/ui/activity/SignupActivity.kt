package com.android.frogtest.ui.activity

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import com.android.frogtest.R
import com.android.frogtest.databinding.ActivitySignupBinding
import com.android.frogtest.ui.fragment.LoginFragment
import com.android.frogtest.ui.fragment.RegisterFragment
import dagger.hilt.android.AndroidEntryPoint

@AndroidEntryPoint
class SignupActivity : AppCompatActivity() {

    private lateinit var binding: ActivitySignupBinding
    override fun onCreate(savedInstanceState: Bundle?) {

        super.onCreate(savedInstanceState)
        binding = ActivitySignupBinding.inflate(layoutInflater)
        setContentView(binding.root)

        if (savedInstanceState == null) {
            supportFragmentManager.beginTransaction().replace(R.id.containers, RegisterFragment()).commitNow()
        }
    }
}