package com.ssessments.newsapp

import android.text.TextUtils
import android.util.Log
import com.ssessments.newsapp.data.getNewsItemArray
import com.ssessments.newsapp.database.NewsDatabase
import com.ssessments.newsapp.database.UserData
import com.ssessments.newsapp.network.NetworkNewsFilterObject
import com.ssessments.newsapp.utilities.EMPTY_TOKEN
import com.ssessments.newsapp.utilities.Markets
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.MainScope
import kotlinx.coroutines.launch
import org.junit.Test

import org.junit.Assert.*
import java.util.*

/**
 * Example local unit test, which will execute on the development machine (host).
 *
 * See [testing documentation](http://d.android.com/tools/testing).
 */
class ExampleUnitTest {
    @Test
    fun addition_isCorrect() {
        assertEquals(4, 2 + 2)
    }

    @Test
    fun checkNetworkNewsObjectClass(){

        //var m:NetworkNewsFilterObject= NetworkNewsFilterObject(dateFrom = "21.21.2019")
        //print("${m.toString()}")

    }

    @Test
    fun checkArrayTostring(){
    val a=Array<String>(3,{it->"mi+$it"})

    print("${Arrays.toString(a)}")

    val b=a.toList()
        print("${b}")

        print(TextUtils.join(",",a))
        print(TextUtils.join(",",b))

    }

    @Test
    fun checknull(){
        val m:UserData=UserData("milena","pass","token1")
        print (" sve ima u userdata ${m?.token?: EMPTY_TOKEN}")

        val m2:UserData?=null
        print (" null userdata ${m2?.token ?: EMPTY_TOKEN}")
    }

    @Test
    fun countofNewsItem() {

        val list= getNewsItemArray()
            print("size je ${list.size}")

    }


}
