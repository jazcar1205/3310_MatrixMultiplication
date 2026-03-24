public class DivideAndConquer {
    private final int[][] matrixA;
    private final int[][] matrixB;

    public DivideAndConquer(int[][] matrixA, int[][] matrixB) {
        this.matrixA = matrixA;
        this.matrixB = matrixB;
    }

    public int[][] multiply() {
        return multiply(matrixA, matrixB);
    }

    // Assumes square matrices with power-of-2 dimension
    private static int[][] multiply(int[][] A, int[][] B) {
        int n = A.length;

        // Base case: 1x1
        if (n == 1) {
            return new int[][]{{A[0][0] * B[0][0]}};
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

    private static int[][] split(int[][] mat, int row, int col, int size) {
        int[][] sub = new int[size][size];
        for (int i = 0; i < size; i++)
            for (int j = 0; j < size; j++)
                sub[i][j] = mat[row + i][col + j];
        return sub;
    }

    private static int[][] combine(int[][] C11, int[][] C12, int[][] C21, int[][] C22, int n) {
        int[][] result = new int[n][n];
        int half = n / 2;
        for (int i = 0; i < half; i++) {
            for (int j = 0; j < half; j++) {
                result[i][j]             = C11[i][j];
                result[i][j + half]      = C12[i][j];
                result[i + half][j]      = C21[i][j];
                result[i + half][j + half] = C22[i][j];
            }
        }
        return result;
    }

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