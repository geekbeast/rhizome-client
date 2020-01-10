package com.openlattice.rhizome.hazelcast

import java.lang.IllegalArgumentException
import java.util.concurrent.BlockingQueue

class ChunkedQueueSequence<T>(private val queue: BlockingQueue<T>, private val chunkSize: Int): Sequence<List<T>> {
    init {
        if (chunkSize < 1) {
            throw IllegalArgumentException("chunkSize")
        }
    }
    override fun iterator(): Iterator<List<T>> {
        return iterator() {
            while(true) {
                val out = ArrayList<T>(chunkSize)
                out.add(queue.take())
                if (chunkSize > 1) {
                    queue.drainTo(out, chunkSize - 1)
                }
                yield(out)
            }
        }
    }
}
