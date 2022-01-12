package com.geekbeast.rhizome.jobs

/**
 *
 * @author Matthew Tamayo-Rios &lt;matthew@openlattice.com&gt;
 */
enum class JobStatus {
    PAUSED,
    PENDING,
    FINISHED,
    RUNNING,
    STOPPING,
    CANCELED //Due to thread interruption mechanics we don't yet have plumbing to differentiate between canceled and failed.
}