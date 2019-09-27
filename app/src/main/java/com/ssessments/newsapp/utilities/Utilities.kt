package com.ssessments.newsapp.utilities

import android.app.Application
import android.content.Context
import android.net.*
import android.os.Build



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



