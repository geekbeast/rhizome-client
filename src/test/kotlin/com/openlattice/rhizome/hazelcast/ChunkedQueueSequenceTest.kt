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
        queue.addAll(0..100)
        val batch = ChunkedQueueSequence(queue, 100).iterator().next()
        Assert.assertEquals(100, batch.size)
    }
    @Test(timeout = 1000)
    fun testTwoChunks() {
        val queue = LinkedBlockingQueue<Int>()
        queue.addAll(0..200)
        val iter = ChunkedQueueSequence(queue, 100).iterator()
        Assert.assertEquals(100, iter.next().size)
        Assert.assertEquals(100, iter.next().size)
    }

    @Test(timeout = 1000)
    fun testSmallBatch() {
        val queue = LinkedBlockingQueue<Int>()
        queue.addAll(0..10)
        val lists = ChunkedQueueSequence(queue, 1).take(10)
    }


    @Test(timeout = 1000)
    fun testAsyncProducer() {
        val queue = LinkedBlockingQueue<Int>()
        val pool = Executors.newCachedThreadPool()
        try {
            val cdl = CountDownLatch(1)
            pool.execute(Runnable {
                queue.add(1)
                cdl.await(100, TimeUnit.MILLISECONDS)
                queue.add(2)
            })
            val seq = ChunkedQueueSequence(queue, 10)
            val l1 = seq.take(1).single()
            cdl.countDown()
            Assert.assertEquals(1, l1.size)
            val l2 = seq.take(1).single()
            Assert.assertEquals(1, l2.size)
        } finally {
            pool.shutdown()
        }
    }
}
