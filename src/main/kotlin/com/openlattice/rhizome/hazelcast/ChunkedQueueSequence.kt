package com.openlattice.rhizome.hazelcast

import java.lang.IllegalArgumentException
import java.util.concurrent.BlockingQueue

/**
 * ChunkedQueueSequence takes a blocking queue and generates an infinite sequence of enqueued values broken up into
 * chunks no bigger than #chunkSize. It's intended to replace `generateSequence(queue::take).chunked(chunkSize)`, with
 * the added improvement that it will return partial blocks without waiting for a full chunk.
 *
 * BlockingQueue has no intrinsic close semantics, so this call, like BlockingQueue.take(), will block (up to) forever on
 * input. Terminating the queue requires some kind of in-band or out of band signalling. Examples include:
 *
 * - putting a sentinel object indicating shutdown in the queue (note that `null` is not a valid sentinel).
 * - calling `Thread.interrupt()` and handling the resulting `InterruptedException`, possibly with other external state
 * - Calling `destroy` on a Hazelcast `IQueue`
 *
 * @see BlockingQueue for more discussion of general purpose producer consumer queues.
 */
class ChunkedQueueSequence<T>(private val queue: BlockingQueue<T>, private val chunkSize: Int) : Sequence<List<T>> {
    init {
        if (chunkSize < 1) {
            throw IllegalArgumentException("chunkSize")
        }
    }

    override fun iterator(): Iterator<List<T>> {
        return iterator() {
            while (true) {
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
