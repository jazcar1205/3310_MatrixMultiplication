public class test {
    public static void main(String[] args) {
        int[][] A = {
                {2, 1, 3, 0},
                {4, 2, 1, 5},
                {3, 0, 2, 1},
                {1, 4, 3, 2}
        };

        int[][] B = {
                {2, 3, 1, 0},
                {1, 5, 2, 4},
                {3, 2, 6, 1},
                {4, 0, 3, 2}
        };

        int[][] expected = {
                {14, 17, 22, 7},
                {33, 24, 29, 19},
                {16, 13, 18, 4},
                {23, 29, 33, 23}
        };

        test("Classic",          new Classic(A, B).multiply(),          expected);
        test("DivideAndConquer", new DivideAndConquer(A, B).multiply(), expected);
        test("Strassen",         new Strassen(A, B).multiply(),         expected);
    }

    private static void test(String name, int[][] C, int[][] expected) {
        boolean correct = true;
        for (int i = 0; i < C.length; i++)
            for (int j = 0; j < C[0].length; j++)
                if (C[i][j] != expected[i][j]) correct = false;

        System.out.println(name + ": " + (correct ? "PASS" : "FAIL"));

        for (int[] row : C) {
            for (int val : row)
                System.out.printf("%4d", val);
            System.out.println();
        }
        System.out.println();
    }
}