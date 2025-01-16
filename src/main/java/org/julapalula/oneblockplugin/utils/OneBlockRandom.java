package org.julapalula.oneblockplugin.utils;

/**
 * Mersenne Twister random number generator implementation.
 *
 * <p>
 * This class provides methods to generate random numbers using the Mersenne Twister algorithm,
 * which is known for its high-quality random number generation and long period (2^19937 − 1).
 * </p>
 *
 * <strong>Features:</strong>
 * <ul>
 *   <li>Seeded initialization for reproducible sequences.</li>
 *   <li>Support for generating random integers, doubles, and numbers within specific ranges.</li>
 * </ul>
 *
 * <strong>Usage Example:</strong>
 * <pre>
 * OneBlockRandom random = new OneBlockRandom(12345); // Initialize with a seed
 * int randomInt = random.nextInt();                // Generate a random integer
 * double randomDouble = random.nextDouble();       // Generate a random double in [0, 1)
 * int boundedInt = random.nextIntInRange(10, 50);  // Generate a random integer in [10, 50)
 * </pre>
 */

public class OneBlockRandom {

    private static final int N = 624;
    private static final int M = 397;
    private static final int MATRIX_A = 0x9908b0df;   // Constant vector 'a'
    private static final int UPPER_MASK = 0x80000000; // Most significant w-r bits
    private static final int LOWER_MASK = 0x7fffffff; // Least significant r bits

    private final int[] mt = new int[N]; // The state vector
    private int mti = N + 1;             // mti == N+1 indicates uninitialized state vector

    /**
     * Constructs a new instance of {@code OneBlockRandom} with a default seed.
     *
     * <p>
     * The default seed initializes the random number generator with a known value (5489),
     * which ensures consistent behavior across different runs if the same seed is used.
     * </p>
     */
    public OneBlockRandom() {
        mt[0] = 5489;
        for (mti = 1; mti < N; mti++) {
            mt[mti] = (1812433253 * (mt[mti - 1] ^ (mt[mti - 1] >>> 30)) + mti);
            mt[mti] &= 0xffffffff; // Ensure a 32-bit integer
        }
    }

    /**
     * Constructs a new instance of {@code OneBlockRandom} with a specific seed.
     *
     * <p>
     * The seed determines the initial state of the generator, enabling reproducibility.
     * </p>
     *
     * @param seed the seed value to initialize the random number generator
     */
    public OneBlockRandom(int seed) {
        mt[0] = seed;
        for (mti = 1; mti < N; mti++) {
            mt[mti] = (1812433253 * (mt[mti - 1] ^ (mt[mti - 1] >>> 30)) + mti);
            mt[mti] &= 0xffffffff; // Ensure a 32-bit integer
        }
    }

    /**
     * Generates the next random integer.
     *
     * @return a randomly generated 32-bit signed integer
     */
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

    /**
     * Generates a random integer within the range [0, max).
     *
     * @param max the upper bound (exclusive)
     * @return a randomly generated integer in the range [0, max)
     */
    public int nextInt(int max) {
        return Math.abs(nextInt() % max);
    }

    /**
     * Generates a random double in the range [0, 1).
     *
     * @return a randomly generated double in the range [0, 1)
     */
    public double nextDouble() {
        return (nextInt() >>> 1) / ((double) Integer.MAX_VALUE + 1);
    }

    /**
     * Generates a random integer within the specified range [min, max).
     *
     * @param min the lower bound (inclusive)
     * @param max the upper bound (exclusive)
     * @return a randomly generated integer in the range [min, max)
     * @throws IllegalArgumentException if {@code max <= min}
     */
    public int nextIntInRange(int min, int max) {
        if (min >= max) {
            throw new IllegalArgumentException("max must be greater than min");
        }
        return min + Math.abs(nextInt() % (max - min));
    }
}
