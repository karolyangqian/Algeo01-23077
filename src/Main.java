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
        Matriks S = SPL.MetodeGauss(M);
        // if (S != null){
        //     M2.printMatriks();
        // }
        // else{
        //     System.out.println("Tidak ada solusi");
        // }
        // Matriks M2 = M.kalikanBaris(0, 0);
        // Matriks M3 = M2.kaliXMatriks(1/M.determinanMatriks("reduksi"));
        // M = linalg.toEselonBaris(M);
        // M.makePositiveZero();
        // M.printMatriks();
        // M2.printMatriks();
        // M3.printMatriks();
        // SistemPersamaanLinier SPL = new SistemPersamaanLinier(M);
        // System.err.println(SPL.isEselonBaris());
        // SPL.toEselonBaris().printMatriks();
    }
}