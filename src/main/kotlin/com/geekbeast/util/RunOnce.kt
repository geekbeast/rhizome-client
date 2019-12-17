package com.geekbeast.util

import com.google.common.base.Suppliers
import java.util.function.Supplier
import kotlin.RuntimeException

/**
 * A class for safely, lazily, initializing shared data.
 *
 * If you want to initialize some shared state, you can create an instance of this at the same scope as the data,
 * passing in a function that will be called no more than once per scope. Call .get() before using the shared state.
 *
 * When is it useful? When you need to more or less globally initialize some values that can't be easily encapsulated.
 *
 * Why is it preferable to a static {} block? In short, because when static initializers fail, the class is marked as
 * forever unavailable in the underlying classloader, and subsequent attempts to load any class that refers to the now
 * broken class will instead throw NoClassDefFoundError, with no indication of the underlying cause. This can greatly
 * hamper debugging efforts when those initializers depend on any kind of external services or complex code.
 */
class RunOnce private constructor(private val supplier: Supplier<Exception?>) {
    public constructor(delegate: () -> Unit) : this(makeSupplier(delegate))

    fun get() {
        supplier.get()?.let { throw RunOnceException(it) }
    }
}

open class RunOnceException(cause: Exception) : RuntimeException(cause)

private fun makeSupplier(delegate: () -> Unit): Supplier<Exception?> {
    return Suppliers.memoize {
        try {
            delegate()
            null
        } catch (e: Exception) {
            e
        }
    }
}