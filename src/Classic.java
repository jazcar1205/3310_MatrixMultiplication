
public class Classic {

    /** Matrix (n × m). */
    private final int[][] matrixA;

    /** Matrix (m × q). */
    private final int[][] matrixB;

    /** Number of rows in A. */
    private final int n;

    /** # of columns in A / rows in B. */
    private final int m;

    /** # of columns in B. */
    private final int q;

    /**
     * Constructs a Classic multiplier for the two given matrices.
     *
     * @param matrixA  (n × m)
     * @param matrixB  (m × q)
     */
    public Classic(int[][] matrixA, int[][] matrixB) {
        this.matrixA = matrixA;
        this.matrixB = matrixB;
        this.n = matrixA.length;
        this.m = matrixA[0].length;
        this.q = matrixB[0].length;
    }

    /**
     * Computes and returns the matrix product A × B.
     *
     * @return a new n×q matrix representing the product
     */
    public int[][] multiply() {
        int[][] result = new int[n][q];

        for (int i = 0; i < n; i++) {
            for (int j = 0; j < q; j++) {
                for (int k = 0; k < m; k++) {
                    result[i][j] += matrixA[i][k] * matrixB[k][j];
                }
            }
        }
        return result;
    }

}