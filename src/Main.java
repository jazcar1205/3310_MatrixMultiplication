import java.util.Random;

public class Main {
    private static final int TRIALS = 11;

    public static void main(String[] args) {
        System.out.printf("%-8s | %-28s | %-32s | %-28s%n",
                "Size (n)", "Classic (ns / ms)", "Divide & Conquer (ns / ms)", "Strassen (ns / ms)");
        System.out.println("-".repeat(108));

        Random rng = new Random(42); // fixed seed so datasets are reproducible across runs

        int n = 2;
        while (true) {
            int[][] A, B;
            try {
                A = generateMatrix(n, rng);
                B = generateMatrix(n, rng);
            } catch (OutOfMemoryError e) {
                System.out.printf("%nStopped at n=%d: not enough memory to allocate input matrices.%n", n);
                break;
            }

            Long avgClassic        = benchmark("Classic",          A, B);
            Long avgDivideConquer  = benchmark("DivideAndConquer", A, B);
            Long avgStrassen       = benchmark("Strassen",         A, B);

            if (avgClassic == null && avgDivideConquer == null && avgStrassen == null) {
                System.out.printf("%nStopped at n=%d: all algorithms ran out of memory.%n", n);
                break;
            }

            System.out.printf("%-8d | %-28s | %-32s | %-28s%n",
                    n,
                    avgClassic       == null ? "OOM" : fmtNsMs(avgClassic),
                    avgDivideConquer == null ? "OOM" : fmtNsMs(avgDivideConquer),
                    avgStrassen      == null ? "OOM" : fmtNsMs(avgStrassen));

            if (n >= 4096) break;
            n *= 2;
        }
    }

    private static Long benchmark(String algorithm, int[][] A, int[][] B) {
        long[] times = new long[TRIALS];

        for (int t = 0; t < TRIALS; t++) {
            try {
                long start = System.nanoTime();
                int[][] result = runAlgorithm(algorithm, A, B);
                times[t] = System.nanoTime() - start;
                // touch result to prevent JIT dead-code elimination
                if (result == null) return null;
            } catch (OutOfMemoryError e) {
                return null;
            }
        }

        return trimmedAverage(times);
    }

    private static long trimmedAverage(long[] times) {
        long sum  = 0;
        long best  = Long.MAX_VALUE;
        long worst = Long.MIN_VALUE;

        for (long t : times) {
            sum += t;
            if (t < best)  best  = t;
            if (t > worst) worst = t;
        }

        return (sum - best - worst) / (TRIALS - 2);
    }

    /** Dispatches to the correct algorithm class. */
    private static int[][] runAlgorithm(String algorithm, int[][] A, int[][] B) {
        switch (algorithm) {
            case "Classic":
                return new Classic(A, B).multiply();
            case "DivideAndConquer":
                return new DivideAndConquer(A, B).multiply();
            case "Strassen":
                return new Strassen(A, B).multiply();
            default:
                throw new IllegalArgumentException("Unknown algorithm: " + algorithm);
        }
    }

    private static String fmtNsMs(long ns) {
        return ns + " ns / " + String.format("%.3f", ns / 1_000_000.0) + " ms";
    }

    private static int[][] generateMatrix(int n, Random rng) {
        int[][] M = new int[n][n];
        for (int i = 0; i < n; i++)
            for (int j = 0; j < n; j++)
                M[i][j] = rng.nextInt(100) + 1;
        return M;
    }
}
