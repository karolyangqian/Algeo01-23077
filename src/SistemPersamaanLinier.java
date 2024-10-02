

public class SistemPersamaanLinier {
    /**
     * Konstruktor kelas SistemPersamaanLinier
     */
    public SistemPersamaanLinier(){}

    /**
     * @param M matriks augmented sistem persamaan linier
     * @return matriks solusi dari sistem persamaan linier
     */
    // Matriks metodeGauss(Matriks M) {
    //     Matriks Mtemp = new Matriks(M);
    //     int row = Mtemp.getRow();
    //     int col = Mtemp.getCol();
    //     int i = 0;
    //     int j = 0;
    //     while (i < row && j < col - 1) {
    //         if (Mtemp.Mat[i][j] == 0) {
    //             boolean found = false;
    //             for (int k = i + 1; k < row; k++) {
    //                 if (Mtemp.Mat[k][j] != 0) {
    //                     found = true;
    //                     OBE.tukarBaris(Mtemp, i, k);
    //                     break;
    //                 }
    //             }
    //             if (!found) {
    //                 j++;
    //                 continue;
    //             }
    //         }
    //         for (int k = i + 1; k < row; k++) {
    //             float x = Mtemp.Mat[k][j] / Mtemp.Mat[i][j];
    //             OBE.jumlahKelipatanBaris(Mtemp, k, i, -x);
    //         }
    //         i++;
    //         j++;
    //     }
    //     for (int k = i; k < row; k++) {
    //         if (Mtemp.Mat[k][col - 1] != 0) {
    //             return new Matriks(0, 0);
    //         }
    //     }
    //     Matriks solusi = new Matriks(1, col - 1);
    //     for (int k = row - 1; k >= 0; k--) {
    //         float sum = 0;
    //         for (int l = k + 1; l < col - 1; l++) {
    //             sum += Mtemp.Mat[k][l] * solusi.Mat[0][l];
    //         }
    //         solusi.Mat[0][k] = (Mtemp.Mat[k][col - 1] - sum) / Mtemp.Mat[k][k];
    //     }
    //     return solusi;
    // }
}
