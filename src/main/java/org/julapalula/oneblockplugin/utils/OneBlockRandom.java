package org.julapalula.oneblockplugin.utils;
/*
 *   Mersenne Twister Random algorithm
 */
public class OneBlockRandom {
    private static final int N = 624;
    private static final int M = 397;
    private static final int MATRIX_A = 0x9908b0df;   // Constant vector a
    private static final int UPPER_MASK = 0x80000000; // Most significant w-r bits
    private static final int LOWER_MASK = 0x7fffffff; // Least significant r bits

    private final int[] mt = new int[N]; // The array for the state vector
    private int mti = N + 1;             // mti == N+1 means mt[N] is not initialized

    // Constructor to initialize with a seed
    public OneBlockRandom(int seed) {
        mt[0] = seed;
        for (mti = 1; mti < N; mti++) {
            mt[mti] = (1812433253 * (mt[mti - 1] ^ (mt[mti - 1] >>> 30)) + mti);
            mt[mti] &= 0xffffffff; // Ensure it's a 32-bit integer
        }
    }

    // Generate the next random number
    public int nextInt() {
        int y;
        int[] mag01 = {0x0, MATRIX_A};

        if (mti >= N) { // Generate N words at a time
            int kk;

            for (kk = 0; kk < N - M; kk++) {
                y = (mt[kk] & UPPER_MASK) | (mt[kk + 1] & LOWER_MASK);
                mt[kk] = mt[kk + M] ^ (y >>> 1) ^ mag01[y & 0x1];
            }
            for (; kk < N - 1; kk++) {
                y = (mt[kk] & UPPER_MASK) | (mt[kk + 1] & LOWER_MASK);
                mt[kk] = mt[kk + (M - N)] ^ (y >>> 1) ^ mag01[y & 0x1];
            }
            y = (mt[N - 1] & UPPER_MASK) | (mt[0] & LOWER_MASK);
            mt[N - 1] = mt[M - 1] ^ (y >>> 1) ^ mag01[y & 0x1];

            mti = 0;
        }

        y = mt[mti++];

        // Tempering
        y ^= (y >>> 11);
        y ^= (y << 7) & 0x9d2c5680;
        y ^= (y << 15) & 0xefc60000;
        y ^= (y >>> 18);

        return y;
    }

    // Generate a random number in the range [0, max)
    public int nextInt(int max) {
        return Math.abs(nextInt() % max);
    }

    // Generate a random double in the range [0, 1)
    public double nextDouble() {
        return (nextInt() >>> 1) / ((double) (Integer.MAX_VALUE) + 1);
    }

    // Generate a random number in the range [min, max)
    public int nextIntInRange(int min, int max) {
        if (min >= max) {
            throw new IllegalArgumentException("max must be greater than min");
        }
        return min + Math.abs(nextInt() % (max - min));
    }
}
