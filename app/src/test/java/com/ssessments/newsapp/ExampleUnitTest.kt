package com.ssessments.newsapp

import android.text.TextUtils
import android.util.Log
import com.ssessments.newsapp.data.getNewsItemArray
import com.ssessments.newsapp.database.NewsDatabase
import com.ssessments.newsapp.database.UserData
import com.ssessments.newsapp.network.NetworkNewsFilterObject
import com.ssessments.newsapp.network.NetworkSinglePreference
import com.ssessments.newsapp.utilities.EMPTY_TOKEN
import com.ssessments.newsapp.utilities.Markets
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.MainScope
import kotlinx.coroutines.launch
import com.ssessments.newsapp.MainActivityViewModel
import com.ssessments.newsapp.utilities.Products
import com.ssessments.newsapp.utilities.Ssessments
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
    fun countofNewsItem() {

        val list= getNewsItemArray()
            print("size je ${list.size}")

    }

    @Test
    fun checkContinueLoop() {

        val list= listOf(NetworkSinglePreference("sea",preferenceValue = false),NetworkSinglePreference("pp",false),
            NetworkSinglePreference("daily",false))

        loop@
        for(value in list) {

            for (index in 0..Markets.values().size - 2) {
                val s: String = Markets.values()[index + 1].toString().toLowerCase()
                println(" value pre je $value, index pre je $index, a s je $s")
                if (value.preferenceKey.equals(s)) {
                println(" value je $value, index je $index")
                   // editor.putBoolean(s, value.preferenceValue)
                    continue@loop
                }
            }

            for (index in 0..Products.values().size - 2) {
                val s: String = Products.values()[index + 1].toString().toLowerCase()
                println(" value pre je $value, index pre je $index, a s je $s")
                if (value.preferenceKey.equals(s)) {
                    //editor.putBoolean(s, value.preferenceValue)
                    println(" value je $value, index je $index")
                    continue@loop
                }
            }

            for (index in 0..Ssessments.values().size - 2) {

                val s: String = Ssessments.values()[index + 1].toString().toLowerCase()
                println(" value pre je $value, index pre je $index, a s je $s")
                if (value.preferenceKey.equals(s)) {
                    //editor.putBoolean(s, value.preferenceValue)
                    println(" value je $value, index je $index")
                    continue@loop
                }
            }
        }


    }


}
