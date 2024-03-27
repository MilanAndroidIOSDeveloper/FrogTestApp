package com.android.frogtest

import android.content.Context
import android.net.ConnectivityManager
import android.net.Network
import android.net.NetworkCapabilities
import android.net.NetworkRequest

object Utils {
    fun isInternetAvailable(context: Context, callback: (Boolean) -> Unit) {
        val connectivityManager =
            context.getSystemService(Context.CONNECTIVITY_SERVICE) as ConnectivityManager

        val networkCallback = object : ConnectivityManager.NetworkCallback() {
            override fun onAvailable(network: Network) {
                super.onAvailable(network)
                callback(true)
            }

            override fun onLost(network: Network) {
                super.onLost(network)
                callback(false)
            }
        }

        val networkRequest = NetworkRequest.Builder()
            .addTransportType(NetworkCapabilities.TRANSPORT_CELLULAR)
            .addTransportType(NetworkCapabilities.TRANSPORT_WIFI)
            .build()

        connectivityManager.registerNetworkCallback(networkRequest, networkCallback)

        val networkCapabilities = connectivityManager.getNetworkCapabilities(connectivityManager.activeNetwork)
        val isConnected = networkCapabilities != null &&
                (networkCapabilities.hasTransport(NetworkCapabilities.TRANSPORT_CELLULAR) ||
                        networkCapabilities.hasTransport(NetworkCapabilities.TRANSPORT_WIFI))

        callback(isConnected)
    }

}