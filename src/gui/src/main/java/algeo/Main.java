package algeo;

public class Main {
    public static void main(String[] args) {
        Linalg linalg = new Linalg();
        SistemPersamaanLinier SPL = new SistemPersamaanLinier();
        // float[][] A = {
        //     {1, 1, 1, 0},
        //     {2, 3, 1, 1},
        //     {3, 1, 2, 1}
        // };
        double[][] A = {
            {2, 3, -1, 5},
            {4, 4, -3, 3}, 
            {-2, 3, -1, 1}
        };
        double[][] B = {
            {1, 3, -2, 0, 2, 0, 0},              
            {2, 6, -5, -2, 4, -3, -1},
            {0, 0, 5, 10, 0, 15, 5},
            {2, 6, 0, 8, 4, 18, 6}
        };
        Matriks M = new Matriks(B);
        // Matriks M2 = linalg.toEselonBarisTereduksi(M);
        // M2.printMatriks();
        Matriks S = SPL.metodeGauss(M);
        S.printMatriks();
        System.out.println();
        Matriks S2 = SPL.metodeGaussJordan(M);
        S2.printMatriks();
        System.out.println();
        Matriks S3 = SPL.metodeCramer(M);
        if (S3 != null) {
            S3.printMatriks();
        } else {
            System.out.println("Tidak ada solusi");
        }
        System.out.println();
        Matriks S4 = SPL.metodeInversMatriks(M);
        if (S4 != null) {
            S4.printMatriks();
        } else {
            System.out.println("Tidak ada solusi");
        }
    }
}

