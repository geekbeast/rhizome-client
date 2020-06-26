package com.openlattice.rhizome.hazelcast

import com.google.common.collect.Queues
import org.junit.Assert
import org.junit.Test
import java.util.concurrent.*

/**
 * All these tests use timeouts because ChunkedQueueSequence can block forever.
 */
class ChunkedQueueSequenceTest {
    @Test(timeout = 1000)
    fun testSimpleChunk() {
        val queue = LinkedBlockingQueue<Int>()
        queue.addAll(0 until 100)
        val batch = ChunkedQueueSequence(queue, 100).iterator().next()
        Assert.assertEquals(100, batch.size)
        Assert.assertEquals((0 until 100).toList(), batch)
    }

    @Test(timeout = 1000)
    fun testTwoChunks() {
        val queue = LinkedBlockingQueue<Int>()
        queue.addAll(0 until 200)
        val iter = ChunkedQueueSequence(queue, 100).iterator()
        Assert.assertEquals(100, iter.next().size)
        Assert.assertEquals(100, iter.next().size)
    }

    @Test(timeout = 1000)
    fun testSmallBatch() {
        val queue = LinkedBlockingQueue<Int>()
        queue.addAll(0 until 10)
        val lists = ChunkedQueueSequence(queue, 1).take(10).toList()
        Assert.assertEquals((0 until 10).map { listOf(it) }.toList(), lists)
    }

    @Test(timeout = 1000)
    fun testAsyncProducer() {
        val queue = LinkedBlockingQueue<Int>()
        val pool = Executors.newCachedThreadPool()
        val size = 1000
        try {
            val sem = Semaphore(1)
            pool.execute(Runnable {
                for (i in 0 until size) {
                    sem.acquire()
                    queue.add(i + 10)
                }
            })
            val seq = ChunkedQueueSequence(queue, 10)
            for (i in 0 until size) {
                val list = seq.take(1).single()
                sem.release()
                Assert.assertEquals(listOf(i + 10), list)
            }
        } finally {
            pool.shutdownNow()
        }
    }
}
