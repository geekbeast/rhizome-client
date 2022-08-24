package com.geekbeast.util

import org.slf4j.Logger
import org.slf4j.event.Level

/**
 *
 * @author Matthew Tamayo-Rios &lt;matthew@openlattice.com&gt;
 */
fun log(logger: Logger, level: Level = Level.INFO, vararg args: Any, mesg: () -> String) {
    when (level) {
        Level.INFO -> logger.info(mesg(), *args)
        Level.DEBUG -> logger.debug(mesg(), *args)
        Level.WARN -> logger.warn(mesg(), *args)
        Level.ERROR -> logger.error(mesg(), *args)
        else -> logger.info(mesg(), *args)
    }
}