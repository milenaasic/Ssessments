package com.ssessments.newsapp.utilities

import android.app.Application
import android.content.Context
import android.net.*
import android.os.Build


fun isOnline(application:Application):Boolean{

    var online=false
    val connMgr= application.getSystemService(Context.CONNECTIVITY_SERVICE) as ConnectivityManager

     if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
         if(connMgr.activeNetwork!=null){
                online=true
         }else{online=false}

    }

    if (Build.VERSION.SDK_INT < Build.VERSION_CODES.M){
        if(connMgr.activeNetworkInfo!=null){
        online=connMgr.activeNetworkInfo.isConnected
        }else online=false
    }

    return online

}



