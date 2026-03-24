public class Strassen {
    private final int[][] matrixA;
    private final int[][] matrixB;
    private final int n, m, q;

    public Strassen(int[][] matrixA, int[][] matrixB) {
        this.matrixA = matrixA;
        this.matrixB = matrixB;
        this.n = matrixA.length;
        this.m = matrixA[0].length;
        this.q = matrixB[0].length;
    }
}
