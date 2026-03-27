public class Strassen {

    /** Matrix A (n × m). */
    private final int[][] matrixA;

    /** Matrix B (m × q). */
    private final int[][] matrixB;

    /** Number of rows in A. */
    private final int n;

    /** # of columns in A / rows in B. */
    private final int m;

    /** # of columns in B. */
    private final int q;

    /** Sub-matrix size at which recursion switches to the classic algorithm. */
    private static final int THRESHOLD = 32;

    /**
     * Constructs a Strassen multiplier for the two given matrices.
     *
     * @param matrixA  left-hand matrix (n × m)
     * @param matrixB  right-hand matrix (m × q)
     */
    public Strassen(int[][] matrixA, int[][] matrixB) {
        this.matrixA = matrixA;
        this.matrixB = matrixB;
        this.n = matrixA.length;
        this.m = matrixA[0].length;
        this.q = matrixB[0].length;
    }

    /**
     * Computes and returns the matrix product A × B.
     * Pads inputs to the next power of two, runs Strassen's algorithm,
     * then trims the result back to the true n×q dimensions.
     *
     * @return a new n×q matrix representing the product
     */
    public int[][] multiply() {
        int size = nextPowerOfTwo(Math.max(Math.max(n, m), q));
        int[][] A = padMatrix(matrixA, size);
        int[][] B = padMatrix(matrixB, size);
        int[][] C = strassen(A, B, size);
        return trimMatrix(C, n, q);
    }

    /**
     * Recursively computes A × B using Strassen's 7-multiplication scheme.
     * Falls back to {@link #naiveMultiply} when size ≤ {@code THRESHOLD}.
     *
     * @param A     left-hand matrix (size × size)
     * @param B     right-hand matrix (size × size)
     * @param size  current matrix dimension
     * @return      product of A and B
     */
    private int[][] strassen(int[][] A, int[][] B, int size) {
        if (size <= THRESHOLD) {
            return naiveMultiply(A, B, size);
        }

        int half = size / 2;

        int[][] A11 = split(A, 0,    0,    half);
        int[][] A12 = split(A, 0,    half, half);
        int[][] A21 = split(A, half, 0,    half);
        int[][] A22 = split(A, half, half, half);

        int[][] B11 = split(B, 0,    0,    half);
        int[][] B12 = split(B, 0,    half, half);
        int[][] B21 = split(B, half, 0,    half);
        int[][] B22 = split(B, half, half, half);

        int[][] P = strassen(add(A11, A22), add(B11, B22), half);
        int[][] Q = strassen(add(A21, A22), B11,           half);
        int[][] R = strassen(A11,           sub(B12, B22), half);
        int[][] S = strassen(A22,           sub(B21, B11), half);
        int[][] T = strassen(add(A11, A12), B22,           half);
        int[][] U = strassen(sub(A21, A11), add(B11, B12), half);
        int[][] V = strassen(sub(A12, A22), add(B21, B22), half);

        int[][] C11 = add(sub(add(P, S), T), V);
        int[][] C12 = add(R, T);
        int[][] C21 = add(Q, S);
        int[][] C22 = add(sub(add(P, R), Q), U);

        return join(C11, C12, C21, C22, size);
    }

    /**
     * Classic triple-loop multiplication used as the base case.
     *
     * @param A     left-hand matrix (size × size)
     * @param B     right-hand matrix (size × size)
     * @param size  matrix dimension
     * @return      product of A and B
     */
    private int[][] naiveMultiply(int[][] A, int[][] B, int size) {
        int[][] C = new int[size][size];
        for (int i = 0; i < size; i++)
            for (int k = 0; k < size; k++)
                for (int j = 0; j < size; j++)
                    C[i][j] += A[i][k] * B[k][j];
        return C;
    }

    /**
     * Extracts a sub-matrix of the given size starting at (row, col).
     *
     * @param M     source matrix
     * @param row   starting row index
     * @param col   starting column index
     * @param size  dimensions of the sub-matrix
     * @return      extracted sub-matrix
     */
    private int[][] split(int[][] M, int row, int col, int size) {
        int[][] result = new int[size][size];
        for (int i = 0; i < size; i++)
            for (int j = 0; j < size; j++)
                result[i][j] = M[row + i][col + j];
        return result;
    }

    /**
     * Combines four quadrant matrices into a single size×size matrix.
     *
     * @param C11  top-left quadrant
     * @param C12  top-right quadrant
     * @param C21  bottom-left quadrant
     * @param C22  bottom-right quadrant
     * @param size size of the output matrix
     * @return     combined size×size matrix
     */
    private int[][] join(int[][] C11, int[][] C12, int[][] C21, int[][] C22, int size) {
        int[][] result = new int[size][size];
        int half = size / 2;
        for (int i = 0; i < half; i++) {
            for (int j = 0; j < half; j++) {
                result[i][j]               = C11[i][j];
                result[i][j + half]        = C12[i][j];
                result[i + half][j]        = C21[i][j];
                result[i + half][j + half] = C22[i][j];
            }
        }
        return result;
    }

    /**
     * Returns the element-wise sum of two same-sized matrices.
     *
     * @param A  first matrix
     * @param B  second matrix
     * @return   element-wise sum
     */
    private int[][] add(int[][] A, int[][] B) {
        int size = A.length;
        int[][] result = new int[size][size];
        for (int i = 0; i < size; i++)
            for (int j = 0; j < size; j++)
                result[i][j] = A[i][j] + B[i][j];
        return result;
    }

    /**
     * Returns the element-wise difference of two same-sized matrices.
     *
     * @param A  first matrix
     * @param B  second matrix
     * @return   element-wise difference (A - B)
     */
    private int[][] sub(int[][] A, int[][] B) {
        int size = A.length;
        int[][] result = new int[size][size];
        for (int i = 0; i < size; i++)
            for (int j = 0; j < size; j++)
                result[i][j] = A[i][j] - B[i][j];
        return result;
    }

    /**
     * Zero-pads matrix M into a new size×size matrix.
     *
     * @param M     source matrix
     * @param size  target dimension
     * @return      padded matrix
     */
    private int[][] padMatrix(int[][] M, int size) {
        int[][] result = new int[size][size];
        for (int i = 0; i < M.length; i++)
            for (int j = 0; j < M[0].length; j++)
                result[i][j] = M[i][j];
        return result;
    }

    /**
     * Trims a padded matrix back to rows×cols dimensions.
     *
     * @param M     padded matrix
     * @param rows  number of rows to keep
     * @param cols  number of columns to keep
     * @return      trimmed matrix
     */
    private int[][] trimMatrix(int[][] M, int rows, int cols) {
        int[][] result = new int[rows][cols];
        for (int i = 0; i < rows; i++)
            for (int j = 0; j < cols; j++)
                result[i][j] = M[i][j];
        return result;
    }

    /**
     * Returns the smallest power of two that is ≥ n.
     *
     * @param n  input value
     * @return   next power of two ≥ n
     */
    private int nextPowerOfTwo(int n) {
        int power = 1;
        while (power < n) power *= 2;
        return power;
    }
}
