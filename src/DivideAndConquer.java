/**
 * Divide and Conquer matrix multiplication algorithm.
 *
 * <p>Recursively splits each input matrix into four n/2 × n/2 quadrants,
 * computes eight sub-multiplications, and combines the results:
 * <pre>
 *   C11 = A11*B11 + A12*B21
 *   C12 = A11*B12 + A12*B22
 *   C21 = A21*B11 + A22*B21
 *   C22 = A21*B12 + A22*B22
 * </pre>
 *
 * <p>Recursion bottoms out at sub-matrices of size ≤ {@code THRESHOLD},
 * where the classic triple-loop is used instead.
 *
 * <p>Time complexity:  O(n³) — recurrence T(n) = 8T(n/2) + O(n²).
 * <p>Space complexity: O(n² log n) due to recursive call stack and sub-matrix allocations.
 */
public class DivideAndConquer {

    /** Matrix A (n × n). */
    private final int[][] matrixA;

    /** Matrix B (n × n). */
    private final int[][] matrixB;

    /** Sub-matrix size at which recursion switches to the classic algorithm. */
    private static final int THRESHOLD = 32;

    /**
     * Constructs a DivideAndConquer multiplier for the two given matrices.
     *
     * @param matrixA  matrix (n × n)
     * @param matrixB  matrix (n × n)
     */
    public DivideAndConquer(int[][] matrixA, int[][] matrixB) {
        this.matrixA = matrixA;
        this.matrixB = matrixB;
    }

    /**
     * Computes and returns the matrix product A × B.
     *
     * @return a new n×n matrix representing the product
     */
    public int[][] multiply() {
        return multiply(matrixA, matrixB);
    }

    /**
     * Recursively computes A × B by dividing each matrix into quadrants.
     * Falls back to {@link #naiveMultiply} when size ≤ {@code THRESHOLD}.
     *
     * @param A  left-hand matrix
     * @param B  right-hand matrix
     * @return   product of A and B
     */
    private static int[][] multiply(int[][] A, int[][] B) {
        int n = A.length;

        if (n <= THRESHOLD) {
            return naiveMultiply(A, B, n);
        }

        int half = n / 2;

        int[][] A11 = split(A, 0,    0,    half);
        int[][] A12 = split(A, 0,    half, half);
        int[][] A21 = split(A, half, 0,    half);
        int[][] A22 = split(A, half, half, half);

        int[][] B11 = split(B, 0,    0,    half);
        int[][] B12 = split(B, 0,    half, half);
        int[][] B21 = split(B, half, 0,    half);
        int[][] B22 = split(B, half, half, half);

        // C11 = A11*B11 + A12*B21
        int[][] C11 = add(multiply(A11, B11), multiply(A12, B21));
        // C12 = A11*B12 + A12*B22
        int[][] C12 = add(multiply(A11, B12), multiply(A12, B22));
        // C21 = A21*B11 + A22*B21
        int[][] C21 = add(multiply(A21, B11), multiply(A22, B21));
        // C22 = A21*B12 + A22*B22
        int[][] C22 = add(multiply(A21, B12), multiply(A22, B22));

        return combine(C11, C12, C21, C22, n);
    }

    /**
     * Classic triple-loop multiplication used as the base case.
     *
     * @param A  left-hand matrix (n × n)
     * @param B  right-hand matrix (n × n)
     * @param n  matrix dimension
     * @return   product of A and B
     */
    private static int[][] naiveMultiply(int[][] A, int[][] B, int n) {
        int[][] C = new int[n][n];
        for (int i = 0; i < n; i++)
            for (int k = 0; k < n; k++)
                for (int j = 0; j < n; j++)
                    C[i][j] += A[i][k] * B[k][j];
        return C;
    }

    /**
     * Extracts a sub-matrix of the given size starting at (row, col).
     *
     * @param mat   source matrix
     * @param row   starting row index
     * @param col   starting column index
     * @param size  dimensions of the sub-matrix
     * @return      extracted sub-matrix
     */
    private static int[][] split(int[][] mat, int row, int col, int size) {
        int[][] sub = new int[size][size];
        for (int i = 0; i < size; i++)
            for (int j = 0; j < size; j++)
                sub[i][j] = mat[row + i][col + j];
        return sub;
    }

    /**
     * Combines four quadrant matrices into a single n×n matrix.
     *
     * @param C11  top-left quadrant
     * @param C12  top-right quadrant
     * @param C21  bottom-left quadrant
     * @param C22  bottom-right quadrant
     * @param n    size of the output matrix
     * @return     combined n×n matrix
     */
    private static int[][] combine(int[][] C11, int[][] C12, int[][] C21, int[][] C22, int n) {
        int[][] result = new int[n][n];
        int half = n / 2;
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
     * @param mat1  first matrix
     * @param mat2  second matrix
     * @return      element-wise sum
     */
    public static int[][] add(int[][] mat1, int[][] mat2) {
        int r = mat1.length;
        int c = mat1[0].length;
        int[][] res = new int[r][c];
        for (int i = 0; i < r; i++)
            for (int j = 0; j < c; j++)
                res[i][j] = mat1[i][j] + mat2[i][j];
        return res;
    }
}
