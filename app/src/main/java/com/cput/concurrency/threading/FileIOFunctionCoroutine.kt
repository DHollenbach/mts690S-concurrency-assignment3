package com.cput.concurrency.threading
import android.util.Log

class FileIOFunctionCoroutine {
    private var filesToProcess: Int = 0;
    constructor(filesToProcess: Int) {
        this.filesToProcess = filesToProcess;
    }

    private suspend fun performFileIO(filesToProcess: Int) {
        val fileIO = FileIOFunctions(filesToProcess)
        fileIO.performIOProcessing()
    }

    suspend fun processFiles() {
        performFileIO(filesToProcess)
    }
}