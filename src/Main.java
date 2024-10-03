
public class Main {
    public static void main(String[] args) {
        float[][] A = {
            {1, 2, 3, 4},
            {0, 1, 6, 7},
            {0, 1, 0, 2}
        };
        Matriks M = new Matriks(A);
        M.printMatriks();
        SistemPersamaanLinier SPL = new SistemPersamaanLinier(M);
        System.err.println(SPL.isEselonBaris());
        // SPL.toEselonBaris().printMatriks();
    }
}