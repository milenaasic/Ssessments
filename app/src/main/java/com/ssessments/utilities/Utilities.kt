package com.ssessments.utilities

import android.app.Application
import android.content.Context
import android.net.*
import android.os.Build

const val EMPTY_TOKEN="empty_token"

fun isOnline(application:Application):Boolean{

    var online=false
    val connMgr= application.getSystemService(Context.CONNECTIVITY_SERVICE) as ConnectivityManager

        //iznad API>= 23
     if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
         //val et=connMgr.activeNetwork
         if(connMgr.activeNetwork!=null){
                online=true
               // Log.i("MY_ISONLINE FUNCTION","network objekat"+et.toString())
         }else{online=false}

    }

    if (Build.VERSION.SDK_INT < Build.VERSION_CODES.M){
        if(connMgr.activeNetworkInfo!=null){
        online=connMgr.activeNetworkInfo.isConnected
        }else online=false
    }

    return online

}

        /* connMgr.getNetworkInfo(network).apply {
             if (type == ConnectivityManager.TYPE_WIFI) {
                 isWifiConn = isWifiConn or isConnected
             }
             if (type == ConnectivityManager.TYPE_MOBILE) {
                 isMobileConn = isMobileConn or isConnected
             }*/



    /*val networkRequest:NetworkRequest=NetworkRequest.Builder().addTransportType(NetworkCapabilities.TRANSPORT_WIFI).build()

   connMgr.registerNetworkCallback(networkRequest,object:
       ConnectivityManager.NetworkCallback() {
       override fun onUnavailable() {
           super.onUnavailable()
           online=false
       }

       override fun onAvailable(network: Network?) {
           super.onAvailable(network)
           online=true
       }
   })*/


