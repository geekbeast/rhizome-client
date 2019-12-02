package com.openlattice.util

class Hashcodes {

    companion object {
        @JvmStatic
        fun generate(a: Any?, b: Any?): Int {
            val result = 31 * 1 + if (a == null) 0 else a.hashCode()
            return 31 * result + if (b == null) 0 else b.hashCode()
        }

        @JvmStatic
        fun generate(a: Any?, b: Any?, c: Any?): Int {
            return 31 * generate( a, b ) + if (c == null) 0 else c.hashCode()
        }

        @JvmStatic
        fun generate(a: Any?, b: Any?, c: Any?, d: Any?): Int {
            return 31 * generate(a, b, c) + if (d == null) 0 else d.hashCode()
        }

        @JvmStatic
        fun generate(a: Any?, b: Any?, c: Any?, d: Any?, e: Any?): Int {
            return 31 * generate( a, b, c, d ) + if (e == null) 0 else e.hashCode()
        }

        @JvmStatic
        fun generate(a: Any?, b: Any?, c: Any?, d: Any?, e: Any?, f: Any?): Int {
            return 31 * generate( a, b, c, d, e ) + if (f == null) 0 else f.hashCode()
        }
    }
}