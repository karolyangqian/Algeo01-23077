package algeo;

/**
 * Kelas Matriks
 */
public class Linalg {

    // ------------------------------------------------------- Operasi Matriks
    // -------------------------------------------------------

    /**
     * Mengalikan matriks dengan skalar x
     * 
     * @param x nilai skalar
     * @return matriks baru yang sudah dikali dengan skalar x
     */
    public Matriks kaliXMatriks(Matriks Mat, double x) {

        Matriks M = new Matriks(Mat);
        for (int i = 0; i < M.getRow(); i++) {
            M = this.kalikanBaris(M, i, x);
        }
        return M;
    }

    /**
     * Pembagian matriks dengan skalar x
     * @param Mat
     * @param x
     * @return
     */
    public Matriks bagiXMatriks(Matriks Mat, double x) {

        Matriks M = new Matriks(Mat);
        for (int i = 0; i < M.getRow(); i++) {
            M = this.bagikanBaris(M, i, x);
        }
        return M;
    }

    /**
     * Melakukan operasi perkalian matriks (instance * M2)
     * 
     * @param M2 Matriks yang akan dikalikan
     * @return matriks baru hasil perkalian
     */
    public Matriks perkalianMatriks(Matriks M1, Matriks M2) {
        // HANDLE ERROR

        Matriks M = new Matriks(M1.getRow(), M2.getCol());
        for (int i = 0; i < M1.getRow(); i++) {
            for (int j = 0; j < M2.getCol(); j++) {
                M.Mat[i][j] = 0;
                for (int k = 0; k < M1.getCol(); k++) {
                    M.Mat[i][j] += M1.Mat[i][k] * M2.Mat[k][j];
                }
            }
        }

        return M;
    }

    /**
     * Melakukan operasi penjumlahan matriks
     * 
     * @param M2 Matriks yang akan dijumlahkan dengan instance
     * @return matriks baru hasil penjumlahan
     */
    public Matriks penjumlahanMatriks(Matriks M1, Matriks M2) {
        // HANDLE ERROR

        Matriks M = new Matriks(M1.getRow(), M2.getCol());
        for (int i = 0; i < M1.getRow(); i++) {
            for (int j = 0; j < M2.getCol(); j++) {
                M.Mat[i][j] = M1.Mat[i][j] + M2.Mat[i][j];
            }
        }

        return M;
    }

    /**
     * Melakukan operasi transpose matriks
     * 
     * @return matriks baru hasil transpose
     */
    public Matriks transposeMatriks(Matriks Mat) {
        Matriks M = new Matriks(Mat.getCol(), Mat.getRow());
        for (int i = 0; i < Mat.getRow(); i++) {
            for (int j = 0; j < Mat.getCol(); j++) {
                M.Mat[j][i] = Mat.Mat[i][j];
            }
        }
        return M;
    }

    /**
     * Membuat matriks kofaktor
     * 
     * @return matriks kofaktor
     */
    public Matriks kofaktorMatriks(Matriks Mat) {
        Matriks M = new Matriks(Mat.getRow(), Mat.getCol());
        for (int i = 0; i < Mat.getRow(); i++) {
            for (int j = 0; j < Mat.getCol(); j++) {
                Matriks M2 = Mat.removeRowColMatriks(i, j);
                M.Mat[i][j] = (double) Math.pow(-1, i + j) * this.determinanMatriks(M2, "reduksi");

            }
        }
        return M;
    }

    /**
     * Membuat matriks adjoint
     * 
     * @return matriks adjoint
     */
    public Matriks adjointMatriks(Matriks Mat) {
        Matriks M = this.kofaktorMatriks(Mat);
        M = this.transposeMatriks(M);
        return M;
    }

    /**
     * Menghitung determinan matriks
     * 
     * @param method metode yang digunakan untuk menghitung determinan
     * @return nilai determinan matriks
     */
    public double determinanMatriks(Matriks M, String method) {
        // belom handle kalo matriksnya bukan persegi
        if (method == "reduksi") {
            return this.detReduksi(M);
        } else {
            return this.detKofaktor(M);
        }

    }

    /**
     * Menghitung invers invers
     * 
     * @return invers matriks
     */
    public Matriks inversMatriks(Matriks Mat, String method) {
        if (method == "adjoin") {
            return this.inversAdjoint(Mat);
        } else {
            return this.inversOBE(Mat);
        }
    }
    // ------------------------------------------------------------------------------------------------------------------------------------

    // ------------------------------------------------------- Matriks Eselon Baris -------------------------------------------------------

    public Matriks toEselonBaris(Matriks Mat) {
        Matriks M = new Matriks(Mat);
        int i = 0;
        for (int j = 0; j < M.getCol(); j++) {
            if (i >= M.getRow()) {
                break;
            }
            int notZeroIdx = -1;
            for (int k = i; k < M.getRow(); k++) {
                if (M.Mat[k][j] != 0) {
                    notZeroIdx = k;
                    break;
                }
            }
            if (notZeroIdx == -1) {
                continue;
            }
            M = this.tukarBaris(M, i, notZeroIdx);
            M = this.bagikanBaris(M, i, M.Mat[i][j]);
            M = this.reduksiKolomKeBawah(M, i, j);
            i++;

        }
        return M;
    }

    public Matriks toEselonBarisTereduksi(Matriks Mat) {
        Matriks M = this.toEselonBaris(Mat);
        int i = 0;
        for (int j = 0; j < M.getCol(); j++) {
            if (i >= M.getRow()) {
                break;
            }
            if (M.Mat[i][j] == 1) {
                M = this.reduksiKolomKeAtas(M, i, j);
                i++;
            }
        }
        return M;
    }

    private Matriks reduksiKolomKeBawah(Matriks Mat, int leadingOneRow, int leadingOneCol) {
        Matriks M = new Matriks(Mat);
        for (int i = leadingOneRow + 1; i < M.getRow(); i++) {
            if (M.Mat[i][leadingOneCol] != 0) {
                M = this.jumlahKelipatanBaris(M, i, leadingOneRow, -M.Mat[i][leadingOneCol]);
            }
        }
        return M;
    }

    private Matriks reduksiKolomKeAtas(Matriks Mat, int leadingOneRow, int leadingOneCol) {
        Matriks M = new Matriks(Mat);
        for (int i = leadingOneRow - 1; i >= 0; i--) {
            if (M.Mat[i][leadingOneCol] != 0) {
                M = this.jumlahKelipatanBaris(M, i, leadingOneRow, -M.Mat[i][leadingOneCol]);
            }
        }
        return M;
    }

    // ------------------------------------------------------------------------------------------------------------------------------------

    // ------------------------------------------------------- Operasi Baris Elementer -------------------------------------------------------

    /**
     * Tukar baris pada matriks
     * 
     * @category Operasi Baris Elementer
     * @param row1 baris yang mau ditukar
     * @param row2 baris yang mau ditukar
     * @return matriks baru yang sudah ditukar barisnya
     */
    public Matriks tukarBaris(Matriks Mat, int row1, int row2) {
        if (row1 == row2) {
            return new Matriks(Mat);
        }
        Matriks M = new Matriks(Mat);
        int col = M.getCol();
        for (int i = 0; i < col; i++) {
            double temp = M.Mat[row1][i];
            M.Mat[row1][i] = M.Mat[row2][i];
            M.Mat[row2][i] = temp;
        }
        return M;
    }

    /**
     * Kalikan baris pada matriks dengan skalar x
     * 
     * @category Operasi Baris Elementer
     * @param row baris yang akan dikalikan
     * @param x   nilai kelipatan
     * @return matriks baru yang sudah dikalikan barisnya
     */
    public Matriks kalikanBaris(Matriks Mat, int row, double x) {
        Matriks M = new Matriks(Mat);
        int col = M.getCol();
        for (int i = 0; i < col; i++) {
            M.Mat[row][i] *= x;
        }
        return M;
    }

    public Matriks bagikanBaris(Matriks Mat, int row, double x) {
        Matriks M = new Matriks(Mat);
        int col = M.getCol();
        for (int i = 0; i < col; i++) {
            M.Mat[row][i] /= x;
        }
        return M;
    }

    /**
     * Jumlahkan kelipatan baris pada matriks
     * 
     * @category Operasi Baris Elementer
     * @param M    matriks yang akan diubah
     * @param row1 baris yang akan diubah
     * @param row2 baris yang akan dijumlahkan ke row1
     * @param x    nilai kelipatan
     */
    public Matriks jumlahKelipatanBaris(Matriks Mat, int row1, int row2, double x) {
        Matriks M = new Matriks(Mat);
        int col = M.getCol();
        for (int i = 0; i < col; i++) {
            M.Mat[row1][i] += x * M.Mat[row2][i];
        }
        return M;
    }

    // Private

    /**
     * Menghitung invers matriks dengan metode adjoin
     */
    private Matriks inversAdjoint(Matriks Mat) {
        double det = this.determinanMatriks(Mat, "reduksi");
        Matriks M = this.adjointMatriks(Mat);
        if (det == 0)
            return null;

        M = this.bagiXMatriks(M, det);
        return M;
    }

    /**
     * Menghitung invers matriks dengan metode OBE
     */
    private Matriks inversOBE(Matriks Mat) {
        double det = this.determinanMatriks(Mat, "reduksi");
        if (det == 0)
            return null;

        Matriks identitas = Mat.identityMatrix();

        Matriks M = Mat.concat(identitas, true);
        M = this.toEselonBarisTereduksi(M);

        for (int i = 0; i < Mat.getRow(); i++) {
            for (int j = Mat.getCol(); j < Mat.getCol() * 2; j++) {
                Mat.Mat[i][j - Mat.getCol()] = M.Mat[i][j];
            }
        }
        return Mat;
    }

    /**
     * Menghitung determinan matriks dengan metode kofaktor
     * 
     * @return nilai determinan matriks
     */
    private double detKofaktor(Matriks Mat) {
        Matriks M = this.kofaktorMatriks(Mat);

        double det = 0;
        for (int i = 0; i < Mat.getRow(); i++) {
            det += Mat.Mat[0][i] * M.Mat[0][i];
        }
        return det;
    }

    /**
     * Menghitung determinan matriks dengan metode reduksi baris
     * 
     * @return nilai determinan matriks
     */
    private double detReduksi(Matriks Mat) {

        Matriks M = new Matriks(Mat);
        double det = 1;

        for (int j = 0; j < M.getCol(); j++) {
            int barisAtas = -1;
            for (int i = j; i < M.getRow(); i++) {
                if (M.Mat[i][j] != 0) {
                    barisAtas = i;
                    break;
                }
            }
            if (barisAtas == -1) {
                return 0;
            }

            M = this.tukarBaris(M, j, barisAtas);
            if (j != barisAtas)
                det *= -1;
            for (int i = 0; i < M.getRow(); i++) {
                if (i > j) {
                    if (M.Mat[i][j] != 0) {
                        M = this.jumlahKelipatanBaris(M, i, j, -1 * M.Mat[i][j] / M.Mat[j][j]);
                    }
                }
            }
        }

        for (int i = 0; i < M.getRow(); i++) {
            det *= M.Mat[i][i];
        }
        ;

        return det;

    }
    // ------------------------------------------------------------------------------------------------------------------------------------

}