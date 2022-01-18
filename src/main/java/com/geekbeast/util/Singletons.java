package com.geekbeast.util;

import java.util.HashMap;
import java.util.Map;
import java.util.UUID;

public final class Singletons {
        public static final int[]       EMPTY_INTS  = new int[0];
        public static final byte[]      EMPTY_BYTES = new byte[0];
        public static final long[]      EMPTY_LONGS = new long[0];
        public static final double[]    EMPTY_DOUBLES = new double[0];
        public static final boolean[]   EMPTY_BOOLS   = new boolean[0];
        public static final short[]     EMPTY_SHORTS = new short[0];

        public static final Integer[]   EMPTY_INTEGERS = new Integer[0];
        public static final Byte[]      EMPTY_BYTE_OBJS = new Byte[0];
        public static final Long[]      EMPTY_LONG_OBJS = new Long[0];
        public static final Double[]    EMPTY_DOUBLE_OBJS = new Double[0];

        public static final String[]    EMPTY_STRINGS           = new String[0];
        public static final UUID[]      EMPTY_UUIDS             = new UUID[0];
        public static final Boolean[]   EMPTY_BOOLEANS          = new Boolean[0];
        public static final Short[]     EMPTY_SHORT_OBJS        = new Short[0];
        public static final Class<?>[]  EMPTY_CLASS_ARRAY       = new Class<?>[0];

        private Singletons() { /* No-Op */ }

        public static final Map<Object, Object> EMPTY_MAP = new HashMap<>(0);
}
