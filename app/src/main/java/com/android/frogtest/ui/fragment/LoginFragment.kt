package com.android.frogtest.ui.fragment

import android.content.Intent
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import com.android.frogtest.BioMetricManager
import com.android.frogtest.BioMetricPrompt
import com.android.frogtest.R
import com.android.frogtest.databinding.FragmentLoginBinding
import com.android.frogtest.models.Resource
import com.android.frogtest.ui.activity.MovieListActivity
import com.android.frogtest.ui.activity.SignupActivity
import com.android.frogtest.viewmodel.AuthViewModel
import dagger.hilt.android.AndroidEntryPoint

@AndroidEntryPoint
class LoginFragment : Fragment() {

    private lateinit var binding: FragmentLoginBinding
    private val viewModel: AuthViewModel by viewModels()

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View {
        binding = FragmentLoginBinding.inflate(inflater, container, false)
        setupListeners()
        return binding.root
    }

    private fun setupListeners() {
        binding.loginButton.setOnClickListener {
            val email = binding.emailEditText.text.toString()
            val password = binding.passwordEditText.text.toString()
            viewModel.login(email, password)
        }

        viewModel.authState.observe(viewLifecycleOwner) {
            when (it) {
                is Resource.Loading -> {
                    binding.progressBar.visibility = View.VISIBLE
                }

                is Resource.Success -> {
                    binding.progressBar.visibility = View.GONE
                    authenticateWithBiometrics()
                }

                is Resource.Error -> {
                    binding.progressBar.visibility = View.GONE
                    Toast.makeText(context, it.message, Toast.LENGTH_SHORT).show()
                }
            }
        }

        binding.signupTextView.setOnClickListener {
            startActivity(Intent(context, SignupActivity::class.java))

        }
    }

    private fun authenticateWithBiometrics() {
        val biometricManager = BioMetricManager(requireContext())
        if (biometricManager.canAuthenticate()) {
            BioMetricPrompt(this).authenticate(
                onSuccess = {
                    Toast.makeText(context, getString(R.string.biometric_authentication_succeeded), Toast.LENGTH_SHORT).show()
                    startActivity(Intent(context, MovieListActivity::class.java))
                    requireActivity().finish()
                },
                onError = { error ->
                    Toast.makeText(context, error, Toast.LENGTH_SHORT).show()
                }
            )
        } else {
            Toast.makeText(context, getString(R.string.fingerprint_hardware_not_available), Toast.LENGTH_SHORT).show()
        }
    }

}