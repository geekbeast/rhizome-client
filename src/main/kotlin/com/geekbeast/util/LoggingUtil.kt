package com.geekbeast.util

import org.slf4j.Logger
import org.slf4j.event.Level

/**
 *
 * @author Matthew Tamayo-Rios &lt;matthew@openlattice.com&gt;
 */
fun log(logger: Logger, level: Level = Level.INFO, mesg: () -> String) {
    when (level) {
        Level.INFO -> logger.info(mesg())
        Level.DEBUG -> logger.debug(mesg())
        Level.WARN -> logger.warn(mesg())
        Level.ERROR -> logger.error(mesg())
        else -> logger.info(mesg())
    }
}