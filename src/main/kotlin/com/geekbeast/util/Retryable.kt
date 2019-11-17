/*
 * Copyright (C) 2019. OpenLattice, Inc.
 *
 * This program is free software: you can redistribute it and/or modify
 * it under the terms of the GNU General Public License as published by
 * the Free Software Foundation, either version 3 of the License, or
 * (at your option) any later version.
 *
 * This program is distributed in the hope that it will be useful,
 * but WITHOUT ANY WARRANTY; without even the implied warranty of
 * MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the
 * GNU General Public License for more details.
 *
 * You should have received a copy of the GNU General Public License
 * along with this program.  If not, see <http://www.gnu.org/licenses/>.
 *
 * You can contact the owner of the copyright at support@openlattice.com
 *
 *
 */

package com.geekbeast.util

import org.apache.commons.lang3.RandomUtils
import org.slf4j.LoggerFactory
import kotlin.math.max
import kotlin.math.sqrt

/**
 *
 * @author Matthew Tamayo-Rios &lt;matthew@openlattice.com&gt;
 */
class Retryable {
    companion object
}

class RetryableCallFailedException : RuntimeException {
    constructor(message: String) : super(message)
}

open class RetryStrategy(val strategy: (Long) -> Long, private var currentDelayMillis: Long = 10L) {
    private val logger = LoggerFactory.getLogger(this.javaClass)

    fun backoff() {
        currentDelayMillis = strategy(currentDelayMillis)
        logger.info("Waiting $currentDelayMillis to retry upload.")
        Thread.sleep(currentDelayMillis)
    }
}

public inline fun <T, R> T.attempt(retryStrategy: RetryStrategy, maxAttempts: Int, block: T.() -> R): R {
    for (i in 0 until maxAttempts) {
        try {
            return block()
        } catch (ex: Exception) {
            val logger = LoggerFactory.getLogger((this ?: Retryable)::class.java)
            logger.error("Error occured while attempting to perform retryable operation.", ex)

            retryStrategy.backoff()
        }
    }

    throw RetryableCallFailedException("Reached max number of retry attempts ($maxAttempts)")
}


open class ExponentialBackoff @JvmOverloads constructor(
        private val maxInterval: Long,
        minRatio: Double = 1.5,
        maxRatio: Double = 2.5
) : RetryStrategy(
        { currentInterval ->
            max(maxInterval, (RandomUtils.nextDouble(minRatio, maxRatio) * currentInterval).toLong())
        }
)

open class QuadraticBackoff(
        private val maxInterval: Long
) : RetryStrategy(
        { currentInterval -> max(maxInterval, (currentInterval + 2 * sqrt(currentInterval.toDouble())).toLong()) }
) 

open class LinearBackoff(
        val maxInterval: Long,
        val offset: Long
) : RetryStrategy(
        { currentInterval -> max(maxInterval, currentInterval + offset) }
)
