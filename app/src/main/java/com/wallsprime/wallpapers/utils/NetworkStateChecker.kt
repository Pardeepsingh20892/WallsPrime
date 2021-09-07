package com.wallsprime.wallpapers.utils

import android.net.ConnectivityManager
import android.net.Network
import android.util.Log


class NetworkStateChecker : ConnectivityManager.NetworkCallback() {

    /*
    override fun onCapabilitiesChanged(network: Network, capabilities: NetworkCapabilities) {
       // val connected = capabilities.hasCapability(NET_CAPABILITY_INTERNET)
       // Log.i("netx", connected.toString())
    }
*/
    override fun onAvailable(network: Network) {
        super.onAvailable(network)
        Log.i("netx", "network connected ")
    }

    override fun onLost(network: Network) {
        super.onLost(network)
        Log.i("netx", "no network")
    }

    override fun onUnavailable() {
        super.onUnavailable()
        Log.i("netx", "no network start")
    }
}





