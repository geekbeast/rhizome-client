package com.openlattice.util

/**
 * @author Drew Bailey &lt;drew@openlattice.com&gt;
 */
class Hashcodes private constructor(){
    companion object {
        @JvmStatic
        fun generate(a: Any?, b: Any?): Int {
            if (a == null) return 0
            val result = 31 + a.hashCode()
            return 31 * result + (b?.hashCode() ?: 0)
        }

        @JvmStatic
        fun generate(a: Any?, b: Any?, c: Any?): Int {
            if (a == null) return 0
            var result = 31 + a.hashCode()
            result = 31 * result + (b?.hashCode() ?: 0)
            return 31 * result + (c?.hashCode() ?: 0)
        }

        @JvmStatic
        fun generate(a: Any?, b: Any?, c: Any?, d: Any?): Int {
            if (a == null) return 0
            var result = 31 + a.hashCode()
            result = 31 * result + (b?.hashCode() ?: 0)
            result = 31 * result + (c?.hashCode() ?: 0)
            return 31 * result + (d?.hashCode() ?: 0)
        }

        @JvmStatic
        fun generate(a: Any?, b: Any?, c: Any?, d: Any?, e: Any?): Int {
            if (a == null) return 0
            var result = 31 + a.hashCode()
            result = 31 * result + (b?.hashCode() ?: 0)
            result = 31 * result + (c?.hashCode() ?: 0)
            result = 31 * result + (d?.hashCode() ?: 0)
            return 31 * result + (e?.hashCode() ?: 0)
        }

        @JvmStatic
        fun generate(a: Any?, b: Any?, c: Any?, d: Any?, e: Any?, f: Any?): Int {
            if (a == null) return 0
            var result = 31 + a.hashCode()
            result = 31 * result + (b?.hashCode() ?: 0)
            result = 31 * result + (c?.hashCode() ?: 0)
            result = 31 * result + (d?.hashCode() ?: 0)
            result = 31 * result + (e?.hashCode() ?: 0)
            return 31 * result + (f?.hashCode() ?: 0)
        }
    }
}