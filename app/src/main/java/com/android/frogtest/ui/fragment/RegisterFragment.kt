package com.android.frogtest.ui.fragment

import android.content.Intent
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import com.android.frogtest.FrogTestApplication
import com.android.frogtest.R
import com.android.frogtest.Utils
import com.android.frogtest.databinding.FragmentRegisterBinding
import com.android.frogtest.ui.activity.MovieListActivity
import com.android.frogtest.viewmodel.AuthViewModel
import com.google.firebase.auth.FirebaseAuth
import dagger.hilt.android.AndroidEntryPoint

@AndroidEntryPoint
class RegisterFragment : Fragment() {

    private lateinit var binding: FragmentRegisterBinding
    private val viewModel by viewModels<AuthViewModel>()

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View {
        binding = FragmentRegisterBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        if (Utils.isInternetAvailable(FrogTestApplication.appContext)) {
            binding.signupButton.setOnClickListener {
                val email = binding.emailEditText.text.toString()
                val password = binding.passwordEditText.text.toString()
                viewModel.signupWithEmailAndPassword(email, password)
                viewModel.authSuccess.observe(viewLifecycleOwner) { success ->
                    if (success) {
                        viewModel.authenticateWithBiometrics(this@RegisterFragment, {
                            Toast.makeText(context, getString(R.string.biometric_authentication_succeeded), Toast.LENGTH_SHORT).show()
                            startActivity(Intent(context, MovieListActivity::class.java))
                            activity?.finish()
                        }, { error ->
                            if (!error.contentEquals(getString(R.string.biometric_authentication_error_too_many_attempts_try_again_later)) && !error.contentEquals(getString(R.string.biometric_authentication_error_fingerprint_operation_cancelled_by_user))) {
                                Toast.makeText(context, error, Toast.LENGTH_SHORT).show()
                            } else {
                                FirebaseAuth.getInstance().currentUser?.delete()
                            }
                        })
                    }
                }
                viewModel.authError.observe(viewLifecycleOwner) { error ->
                    Toast.makeText(context, error, Toast.LENGTH_SHORT).show()
                }
            }
        } else {
            Toast.makeText(
                FrogTestApplication.appContext, getString(R.string.no_internet_available), Toast.LENGTH_SHORT).show()
        }
    }


}