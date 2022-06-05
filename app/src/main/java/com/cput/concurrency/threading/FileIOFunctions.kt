package com.cput.concurrency.threading

import android.os.Environment
import android.util.Log
import com.cput.concurrency.threading.FileIOFunctions
import java.io.*
import java.lang.StringBuilder
import java.text.SimpleDateFormat
import java.util.*
import java.util.concurrent.TimeUnit

class FileIOFunctions {
    private var filesToProcess: Int = 0
    var formatter = SimpleDateFormat("dd/MM/yyyy HH:mm:ss")
    var textToWrite =
        "ssfgsdfgsdf gsdf g sdf gsd fgsdf gsdfg sd fg sdf gsd fg sdfg sdf gsd fg sdfg sdf g sdfg sdf gsd fg sdf gs dfg sdf gsdf gsdgsdfgsdfgsdfgsfdg sdf gs dfg s dfg sdf gsd g sd fgs\n"
    var filePath =
        File(Environment.getExternalStorageDirectory().toString() + "/concurrencyThreads.txt")

    constructor(filesToProcess: Int) {
        this.filesToProcess = filesToProcess;
    }

    private fun createFile() {
        try {
            filePath.createNewFile()
        } catch (e: IOException) {
            Log.v("File Create: ", "Fail - " + e.message)
        }
    }

    private fun deleteFile() {
        if (filePath.exists()) {
            filePath.delete()
        }
    }

    private fun writeStringAsFile(fileContents: String) {
        try {
            val out = FileWriter(filePath, true)
            out.write(fileContents)
            out.close()
        } catch (e: IOException) {
            Log.v("File Write: ", "Fail")
        }
    }

    private fun readFileAsString(): String {
        val stringBuilder = StringBuilder()
        var line: String?
        var `in`: BufferedReader? = null
        try {
            `in` = BufferedReader(FileReader(filePath))
            while (`in`.readLine().also { line = it } != null) stringBuilder.append(line)
        } catch (e: FileNotFoundException) {
            Log.v("File Read: ", "Fail - Not Found")
        } catch (e: IOException) {
            Log.v("File Read: ", "Fail - General IO")
        }
        `in`?.close()
        return stringBuilder.toString()
    }

    fun performIOProcessing() {
        val dateStart = Date()
        Log.v(
            "File Processing: ",
            "Start - " + filesToProcess + " - " + formatter.format(dateStart)
        )
        for (i in 0 until filesToProcess) {
            createFile()
            for (j in 0..99) {
                writeStringAsFile(textToWrite)
            }
            readFileAsString()
            deleteFile()
        }
        val dateEnd = Date()
        val difference_In_Time = dateEnd.time - dateStart.time
        val seconds = TimeUnit.MILLISECONDS.toSeconds(difference_In_Time)
        Log.v("Difference: ", seconds.toString())
        Log.v("File Processing: ", "End - " + formatter.format(dateStart))
    }
}