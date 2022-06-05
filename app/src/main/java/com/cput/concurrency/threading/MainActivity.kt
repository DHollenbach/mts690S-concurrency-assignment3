package com.cput.concurrency.threading

import com.cput.concurrency.threading.FibonacciFinder.fibonacii
import androidx.appcompat.app.AppCompatActivity
import android.widget.EditText
import android.os.Bundle
import android.os.Build
import android.os.Environment
import android.content.Intent
import android.provider.Contacts
import android.provider.Settings
import android.util.Log
import android.view.View
import android.widget.Button
import java.text.SimpleDateFormat
import java.util.*
import java.util.concurrent.TimeUnit
import kotlinx.coroutines.*
import kotlinx.coroutines.channels.SendChannel
import kotlinx.coroutines.channels.actor

class MainActivity : AppCompatActivity() {
    var buttonSimpleCPU: Button? = null
    var buttonThreadsCPU: Button? = null
    var buttonActorsCPU: Button? = null
    var buttonCoroutinesCPU: Button? = null
    var buttonSimpleIO: Button? = null
    var buttonThreadsIO: Button? = null
    var buttonActorsIO: Button? = null
    var buttonCoroutinesIO: Button? = null
    var runCPUInputEdit: EditText? = null
    var runIOInputEdit: EditText? = null
    var formatter = SimpleDateFormat("dd/MM/yyyy HH:mm:ss")
    var actorDateStart = Date()

    val channel: SendChannel<String> = GlobalScope.actor(Dispatchers.Default) {
        for (number in channel) {
            val foundFib = fibonacii(number.toLong())
            Log.v("Call Kotlin Coroutine Code End: ", foundFib.toString())
            val dateEnd = Date()
            val difference_In_Time = dateEnd.time - actorDateStart.time
            val seconds = TimeUnit.MILLISECONDS.toSeconds(difference_In_Time)
            Log.v("Difference: ", seconds.toString())
            Log.v("End *******: ", formatter.format(dateEnd))
        }
    }

    val IoChannel: SendChannel<Int> = GlobalScope.actor(Dispatchers.IO) {
        for (number in channel) {
            val fileIO = FileIOFunctions(number)
            fileIO.performIOProcessing()
        }
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)
        runCPUInputEdit = findViewById<View>(R.id.runCPUInputEdit) as EditText
        runIOInputEdit = findViewById<View>(R.id.runIOInputEdit) as EditText

        // CPU Buttons
        buttonSimpleCPU = findViewById<View>(R.id.buttonSimpleCPU) as Button
        buttonThreadsCPU = findViewById<View>(R.id.buttonThreadsCPU) as Button
        buttonActorsCPU = findViewById<View>(R.id.buttonActorsCPU) as Button
        buttonCoroutinesCPU = findViewById<View>(R.id.buttonCoroutinesCPU) as Button

        // IO Buttons
        buttonSimpleIO = findViewById<View>(R.id.buttonSimpleIO) as Button
        buttonThreadsIO = findViewById<View>(R.id.buttonThreadsIO) as Button
        buttonActorsIO = findViewById<View>(R.id.buttonActorsIO) as Button
        buttonCoroutinesIO = findViewById<View>(R.id.buttonCoroutinesIO) as Button
        if (Build.VERSION.SDK_INT >= 30) {
            if (!Environment.isExternalStorageManager()) {
                val getpermission = Intent()
                getpermission.action = Settings.ACTION_MANAGE_ALL_FILES_ACCESS_PERMISSION
                startActivity(getpermission)
            }
        }

        // CPU intensive baseline
        buttonSimpleCPU!!.setOnClickListener {
            val nthFibToFind = runCPUInputEdit!!.text.toString().toLong()
            val dateStart = Date()
            Log.v("Start *******: ", formatter.format(dateStart))
            val foundFib = fibonacii(nthFibToFind)
            Log.v("EditText Fib Input: ", nthFibToFind.toString())
            Log.v("EditText Fib Result: ", foundFib.toString())
            val dateEnd = Date()
            val difference_In_Time = dateEnd.time - dateStart.time
            val seconds = TimeUnit.MILLISECONDS.toSeconds(difference_In_Time)
            Log.v("Difference: ", seconds.toString())
            Log.v("End *******: ", formatter.format(dateEnd))
        }

        // CPU intensive Threads
        buttonThreadsCPU!!.setOnClickListener {
            val nthFibToFind = runCPUInputEdit!!.text.toString().toLong()
            val myFibThread = FibonacciThread(nthFibToFind)
            myFibThread.start()
        }

        // CPU intensive Coroutines
        buttonCoroutinesCPU!!.setOnClickListener {
            val nthFibToFind = runCPUInputEdit!!.text.toString().toLong()
            val myFibCoRoutine = FibonacciCoRoutine(nthFibToFind)
            Log.v("Call Kotlin Code: ", nthFibToFind.toString())
            CoroutineScope(Dispatchers.Default).launch {
                Log.v("Call Kotlin Coroutine Code Start: ", nthFibToFind.toString())
                val dateStart = Date()
                Log.v("Start *******: ", formatter.format(dateStart))
                val fibResult = myFibCoRoutine.getFib()
                Log.v("Call Kotlin Coroutine Code End: ", fibResult.toString())
                val dateEnd = Date()
                val difference_In_Time = dateEnd.time - dateStart.time
                val seconds = TimeUnit.MILLISECONDS.toSeconds(difference_In_Time)
                Log.v("Difference: ", seconds.toString())
                Log.v("End *******: ", formatter.format(dateEnd))
            }
        }

        // CPU intensive actor
        buttonActorsCPU!!.setOnClickListener {
            val nthFibToFind = runCPUInputEdit!!.text.toString().toLong()
            Log.v("Call Kotlin Code buttonActorsCPU: ", nthFibToFind.toString())
            CoroutineScope(Dispatchers.Default).launch {
                Log.v("Call Kotlin Coroutine Code Start: ", nthFibToFind.toString())
                actorDateStart = Date()
                Log.v("Start *******: ", formatter.format(actorDateStart))
                channel.send(nthFibToFind.toString())
            }
        }

        // IO intensive baseline
        buttonSimpleIO!!.setOnClickListener {
            val filesToProcess = runIOInputEdit!!.text.toString().toInt()
            val fileIO = FileIOFunctions(filesToProcess)
            fileIO.performIOProcessing()
        }

        // IO intensive threads
        buttonThreadsIO!!.setOnClickListener {
            val filesToProcess = runIOInputEdit!!.text.toString().toInt()
            val myIOThread = FileIOFunctionsThread(filesToProcess)
            myIOThread.start()
        }

        // IO intensive Coroutines
        buttonCoroutinesIO!!.setOnClickListener {
            val filesToProcess = runIOInputEdit!!.text.toString().toInt()
            CoroutineScope(Dispatchers.IO).launch {
                val fileIOCoRoutine = FileIOFunctionCoroutine(filesToProcess)
                fileIOCoRoutine.processFiles()
            }
        }

        // IO intensive Actors
        buttonActorsIO!!.setOnClickListener {
            val filesToProcess = runIOInputEdit!!.text.toString().toInt()
            Log.v("Call Kotlin Code buttonActorsIO: ", filesToProcess.toString())
            CoroutineScope(Dispatchers.Default).launch {
                Log.v("Call Kotlin Coroutine Code Start: ", filesToProcess.toString())
                actorDateStart = Date()
                IoChannel.send(filesToProcess)
            }
        }
    }
}