package ulid

import org.junit.jupiter.api.Test

class UlidTest {
//    @Test
//    fun testMonotonicNextUlidGen() {
//        var previousULID = ULID.nextULID()
//        for (i in 0..100) {
//            val time = System.currentTimeMillis()
//            val ulid = ULID.Monotonic.nextULID(previousULID, time)
//            println("$ulid -> ${ulid.timestamp}")
//            previousULID = ulid
//        }
//    }
//
//    @Test
//    fun testStaticNextUlidGen() {
//
//        for (i in 0..100) {
//            val ulid = ULID.nextULID()
//            //println(ulid)
//            println("$ulid -> ${ulid.timestamp}")
//        }
//    }
//
//    @Test
//    fun testFactoryNextUlidGen() {
//        val factory = ULID.Factory()
//        for (i in 0..100) {
//            val ulid = factory.nextULID()
//            println("$ulid -> ${ulid.timestamp}")
//        }
//    }
//
    @Test
    fun testMonotonicFactoryNextUlidGen() {
        val factory = UlidMonotonicFactory()
        val startTime = System.nanoTime()
        val list = mutableSetOf<ULID>()
        repeat(50000) {
            val ulid = factory.nextULID()
            list.add(ulid)
        }
        val endTime = System.nanoTime()
//        list.map {
//            println("${it.timestamp} -> $it")
//        }
        println("Time taken 1: ${endTime - startTime} ns")
    }

    @Test
    fun testMonotonicFactory2Gen() {
        val factory = UlidMonotonicFactory()
        val startTime = System.nanoTime()
        val list = mutableSetOf<ULID>()
        repeat(50000) {
            val ulid = factory.nextULID()
            list.add(ulid)
        }
        val endTime = System.nanoTime()
//        list.map {
//            println("${it.timestamp} -> $it")
//        }
        println("Time taken 2: ${endTime - startTime} ns")
    }

}
