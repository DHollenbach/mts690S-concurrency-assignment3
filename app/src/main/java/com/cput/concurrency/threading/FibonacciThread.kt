package com.cput.concurrency.threading

import android.util.Log
import com.cput.concurrency.threading.FibonacciFinder.fibonacii
import com.cput.concurrency.threading.FibonacciFinder
import java.text.SimpleDateFormat
import java.util.*
import java.util.concurrent.TimeUnit

class FibonacciThread(var fibNeeded: Long) : Thread() {
    var formatter = SimpleDateFormat("dd/MM/yyyy HH:mm:ss")
    override fun run() {
        Log.d("Generic", "Our thread is running ...")
        val dateStart = Date()
        Log.v("Start *******: ", formatter.format(dateStart))
        val foundFib = fibonacii(fibNeeded)
        Log.v("EditText Fib Input: ", fibNeeded.toString())
        Log.v("EditText Fib Result: ", foundFib.toString())
        val dateEnd = Date()
        val difference_In_Time = dateEnd.time - dateStart.time
        val seconds = TimeUnit.MILLISECONDS.toSeconds(difference_In_Time)
        Log.v("Difference: ", seconds.toString())
        Log.v("End *******: ", formatter.format(dateEnd))
        Log.d("Generic", "Our thread is running End ...")
    }
}