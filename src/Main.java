import algeo.Matriks;
import algeo.SistemPersamaanLinier;
import algeo.Linalg;

public class Main {
    public static void main(String[] args) {
        Linalg linalg = new Linalg();
        // float[][] A = {
        //     {1, 2, 3, 4},
        //     {0, 1, 6, 7},
        //     {0, 1, 0, 2}
        // };
        float[][] B = {
            {1, 2, 3},
            {0, 1, 6},
            {0, 1, 0}
        };
        Matriks M = new Matriks(B);
        // Matriks M2 = M.kalikanBaris(0, 0);
        // Matriks M3 = M2.kaliXMatriks(1/M.determinanMatriks("reduksi"));
        M = linalg.inversMatriks(M);
        M.printMatriks();
        // M2.printMatriks();
        // M3.printMatriks();
        // SistemPersamaanLinier SPL = new SistemPersamaanLinier(M);
        // System.err.println(SPL.isEselonBaris());
        // SPL.toEselonBaris().printMatriks();
    }
}