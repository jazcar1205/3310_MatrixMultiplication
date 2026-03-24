public class Classic {

    private final int[][] matrixA;
    private final int[][] matrixB;
    private final int n, m, q;

    public Classic(int[][] matrixA, int[][] matrixB) {
        this.matrixA = matrixA;
        this.matrixB = matrixB;
        this.n = matrixA.length;
        this.m = matrixA[0].length;
        this.q = matrixB[0].length;
    }

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
