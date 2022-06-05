@file:JvmName("FibonacciCoRoutine")

package com.cput.concurrency.threading

class FibonacciCoRoutine {
    var fibNeeded: Long = 0;
    constructor(fibNeeded: Long) {
        this.fibNeeded = fibNeeded;
    }

    private suspend fun fibResult(fibWanted: Long): Long = FibonacciFinder.fibonacii(fibWanted)

    suspend fun getFib(): Long {

        return fibResult(fibNeeded)
    }
}