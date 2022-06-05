package com.cput.concurrency.threading

import com.cput.concurrency.threading.FileIOFunctions

class FileIOFunctionsThread(filesToProcess: Int) : Thread() {
    var filesToProcess = 0
    override fun run() {
        val fileIO = FileIOFunctions(filesToProcess)
        fileIO.performIOProcessing()
    }

    init {
        this.filesToProcess = filesToProcess
    }
}