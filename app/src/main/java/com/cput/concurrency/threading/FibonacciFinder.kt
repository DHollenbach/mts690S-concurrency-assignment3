package com.cput.concurrency.threading

import com.cput.concurrency.threading.FibonacciFinder

object FibonacciFinder {
    @JvmStatic
    fun fibonacii(n: Long): Long {
        return if (n == 0L) 0 else if (n == 1L) 1 else fibonacii(n - 1) + fibonacii(
            n - 2
        )
    }
}