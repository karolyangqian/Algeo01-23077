import algeo.Matriks;
import algeo.SistemPersamaanLinier;
import algeo.Linalg;

public class Main {
    public static void main(String[] args) {
        Linalg linalg = new Linalg();
        SistemPersamaanLinier SPL = new SistemPersamaanLinier();
        // float[][] A = {
        //     {1, 1, 1, 0},
        //     {2, 3, 1, 1},
        //     {3, 1, 2, 1}
        // };
        float[][] A = {
            {1, 1, 2, 4},
            {2, -1, 1, 2},
            {1, 2, 3, 6}
        };
        // float[][] B = {
        //     {1, 3, -2, 0, 2, 0, 0},              
        //     {2, 6, -5, -2, 4, -3, -1},
        //     {0, 0, 5, 10, 0, 15, 5},
        //     {2, 6, 0, 8, 4, 18, 6}
        // };
        Matriks M = new Matriks(A);
        Matriks M2 = linalg.toEselonBarisTereduksi(M);
        // M2.printMatriks();
        Matriks S = SPL.metodeGaussJordan(M);
        S.printMatriks();
    }
}