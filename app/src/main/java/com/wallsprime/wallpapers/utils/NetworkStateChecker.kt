package com.wallsprime.wallpapers.utils


import android.content.Context
import android.net.*
import android.os.Build
import androidx.lifecycle.LiveData


class NetworkStateChecker(private val context: Context) : LiveData<Boolean>() {



  private var connectivityManager: ConnectivityManager = context.getSystemService(Context.CONNECTIVITY_SERVICE)  as ConnectivityManager
  private lateinit var networkCallback: ConnectivityManager.NetworkCallback




    override fun onActive() {
        super.onActive()

        when {

            Build.VERSION.SDK_INT >= Build.VERSION_CODES.N ->{
               connectivityManager.registerDefaultNetworkCallback(connectivityManagerCallback())

            }

           else -> {
                networkRequest()
            }



        }

    }

    override fun onInactive() {
        super.onInactive()
        connectivityManager.unregisterNetworkCallback(connectivityManagerCallback())
    }



  private fun networkRequest(){

     val requestBuilder =  NetworkRequest.Builder()
         .addTransportType(NetworkCapabilities.TRANSPORT_CELLULAR)
         .addTransportType(NetworkCapabilities.TRANSPORT_WIFI)
         .addTransportType(NetworkCapabilities.TRANSPORT_ETHERNET)

      connectivityManager.registerNetworkCallback(requestBuilder.build(),connectivityManagerCallback())

  }



   private fun connectivityManagerCallback(): ConnectivityManager.NetworkCallback{

       networkCallback = object :  ConnectivityManager.NetworkCallback() {

           override fun onUnavailable() {
               super.onUnavailable()
             //  postValue(false)
           }

           override fun onAvailable(network: Network) {
               super.onAvailable(network)
               postValue(true)
           }

           override fun onLost(network: Network) {
               super.onLost(network)
               postValue(false)
           }
       }

       return networkCallback


   }



}





