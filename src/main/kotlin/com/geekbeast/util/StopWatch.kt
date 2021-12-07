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

import com.google.common.base.Stopwatch
import org.slf4j.Logger
import org.slf4j.LoggerFactory
import org.slf4j.event.Level
import java.util.concurrent.TimeUnit


class StopWatch(
        val log: String,
        val level: Level = Level.INFO,
        private val logger: Logger = DEFAULT_LOGGER,
        vararg args: Any
) : AutoCloseable {
    private val _args = args

    companion object {
        private val DEFAULT_LOGGER = LoggerFactory.getLogger(StopWatch::class.java)
    }
    override fun close() {
        val duration = getDuration()
        val mesg = "$log took $duration ms."
        log(logger, level, _args) { mesg }
        sw.stop()
    }

    fun getDuration(): Long {
        return sw.elapsed(TimeUnit.MILLISECONDS)
    }

    private val sw = Stopwatch.createStarted()
}