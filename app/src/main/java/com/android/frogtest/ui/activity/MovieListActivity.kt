package com.android.frogtest.ui.activity

import android.content.Context
import android.content.Intent
import android.content.Intent.FLAG_ACTIVITY_CLEAR_TOP
import android.net.ConnectivityManager
import android.net.NetworkCapabilities
import android.os.Build
import android.os.Bundle
import android.view.Menu
import android.view.MenuItem
import android.view.View
import android.widget.Toast
import androidx.activity.OnBackPressedCallback
import androidx.activity.viewModels
import androidx.appcompat.app.AlertDialog
import androidx.appcompat.app.AppCompatActivity
import androidx.lifecycle.Observer
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.android.frogtest.R
import com.android.frogtest.Utils
import com.android.frogtest.adapters.MovieAdapter
import com.android.frogtest.databinding.ActivityMovielistBinding
import com.android.frogtest.viewmodel.MovieViewModel
import com.google.firebase.auth.FirebaseAuth
import dagger.hilt.android.AndroidEntryPoint

@AndroidEntryPoint
class MovieListActivity : AppCompatActivity() {

    private lateinit var adapter: MovieAdapter
    private lateinit var binding: ActivityMovielistBinding


    private val viewModel by viewModels<MovieViewModel>()


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        binding = ActivityMovielistBinding.inflate(layoutInflater)
        setContentView(binding.root)

        setSupportActionBar(binding.toolbar);

        adapter = MovieAdapter()
        binding.recyclerView.layoutManager = LinearLayoutManager(this)
        binding.recyclerView.adapter = adapter

        viewModel.movies.observe(this, Observer { movies ->
            adapter.addMovies(movies)
        })

        viewModel.isLoading.observe(this, Observer { isLoading ->
            binding.progressBar.visibility = if (isLoading) View.VISIBLE else View.GONE
        })


        binding.recyclerView.addOnScrollListener(object : RecyclerView.OnScrollListener() {
            override fun onScrolled(recyclerView: RecyclerView, dx: Int, dy: Int) {
                super.onScrolled(recyclerView, dx, dy)
                val layoutManager = recyclerView.layoutManager as LinearLayoutManager
                val visibleItemCount = layoutManager.childCount
                val totalItemCount = layoutManager.itemCount
                val firstVisibleItemPosition = layoutManager.findFirstVisibleItemPosition()

                if (!viewModel.isLoading.value!! && (visibleItemCount + firstVisibleItemPosition) >= totalItemCount
                    && firstVisibleItemPosition >= 0) {
                    if (Utils.isInternetAvailable(this@MovieListActivity)) {
                        viewModel.getMoreMovies()
                    } else {
                        Toast.makeText(this@MovieListActivity, getString(R.string.no_internet_available), Toast.LENGTH_SHORT).show()
                    }
                }
            }
        })
        if (Utils.isInternetAvailable(this@MovieListActivity)) {
            viewModel.getMovies()
        } else {
            Toast.makeText(this@MovieListActivity, getString(R.string.no_internet_available), Toast.LENGTH_SHORT).show()
        }

        backPressCallback()
    }

    fun backPressCallback()
    {
        val callback = object : OnBackPressedCallback(true) {
            override fun handleOnBackPressed() {
                exitConfirmationDialog()
            }
        }

        this.onBackPressedDispatcher.addCallback(this, callback)
    }


    override fun onCreateOptionsMenu(menu: Menu?): Boolean {
        menuInflater.inflate(R.menu.movie_list_menu, menu)
        return true
    }

    override fun onOptionsItemSelected(item: MenuItem): Boolean {
        return when (item.itemId) {
            R.id.action_logout -> {
                logoutConfirmationDialog()
                true
            }

            else -> super.onOptionsItemSelected(item)
        }
    }

    private fun logoutUser() {

        FirebaseAuth.getInstance().signOut()
        startActivity(
            Intent(this@MovieListActivity, SignInActivity::class.java).addFlags(
                FLAG_ACTIVITY_CLEAR_TOP
            )
        )
    }

    private fun exitConfirmationDialog() {
        AlertDialog.Builder(this)
            .setTitle(getString(R.string.exit))
            .setMessage(getString(R.string.are_you_sure_you_want_to_exit))
            .setPositiveButton(getString(R.string.yes)) { dialog, which ->
                finish()
            }
            .setNegativeButton(getString(R.string.cancel)) { dialog, which ->
            }
            .show()
    }

    private fun logoutConfirmationDialog() {
        AlertDialog.Builder(this@MovieListActivity)
            .setTitle(getString(R.string.logout))
            .setMessage(getString(R.string.are_you_sure_you_want_to_logout))
            .setPositiveButton(getString(R.string.yes)) { dialog, which ->
                logoutUser()
                finish()
            }
            .setNegativeButton(getString(R.string.cancel)) { dialog, which ->
                dialog.dismiss()
            }
            .show()
    }


}