package com.ssessments.newsapp

import com.ssessments.newsapp.network.NetworkNewsFilterObject
import org.junit.Test

import org.junit.Assert.*

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

        var m:NetworkNewsFilterObject= NetworkNewsFilterObject(dateFrom = "21.21.2019")
        print("${m.toString()}")

    }


}
