package com.openlattice.util;

public class Hashcodes {

    private Hashcodes() {
        /* No-Op */
    }

    public static int generate( Object a, Object b ) {
        int result = 31 + (a == null ? 0 : a.hashCode());
        return 31 * result + ( b == null ? 0 : b.hashCode());
    }

    public static int generate( Object a, Object b, Object c ) {
        return 31 * generate(a, b) + ( c == null ? 0 : c.hashCode());
    }

    public static int generate( Object a, Object b, Object c, Object d) {
        return 31 * generate(a, b, c) + ( d == null ? 0 : d.hashCode());
    }

    public static int generate( Object a, Object b, Object c, Object d, Object e) {
        return 31 * generate(a, b, c, d) + ( e == null ? 0 : e.hashCode());
    }

    public static int generate( Object a, Object b, Object c, Object d, Object e, Object f) {
        return 31 * generate(a, b, c, d, e) + ( f == null ? 0 : f.hashCode());
    }
}
