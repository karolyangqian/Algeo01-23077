package algeo;

public class LinearRegression {

    /**
     * Menambahkan quadratic features dalam metode Quadratic Linear Regression
     * sebelum diolah dengan Normal Equation dan prediksi nilai Y
     * @param X Matriks input
     * @return Matriks dengan quadratic features ditambahkan
     */
    public Matriks addQuadratic(Matriks X) {
        int row = X.getRow();
        int col = X.getCol();
        Matriks newX = new Matriks(row, col + col + (col * (col - 1)) / 2);

        for (int i = 0; i < row; i++) {
            int idx = 0;

            // variabel linier
            for (int j = 0; j < col; j++) {
                newX.Mat[i][idx++] = X.Mat[i][j]; // x1, x2, x3, ...
            }

            // variabel kuadrat
            for (int j = 0; j < col; j++) {
                newX.Mat[i][idx++] = newX.Mat[i][j] * newX.Mat[i][j]; // x1^2, x2^2, x3^2, ...
            }

            // variabel interaksi
            for (int j = 0; j < col; j++) {
                for (int k = j + 1; k < col; k++) {
                    newX.Mat[i][idx++] = X.Mat[i][j] * X.Mat[i][k]; // x1 * x2, x1 * x3, x2 * x3, ...
                }
            }
        }
        return newX;
    }
    /**
     * Perhitungan Multiple Linear Regression dengan Normal Equation 
     */
    public Matriks normalEquation(Matriks X, Matriks Y) {
        Linalg linalg = new Linalg();
        SistemPersamaanLinier SPL = new SistemPersamaanLinier();
        int row = X.getRow();
        int col = X.getCol();
        Matriks X_bias = new Matriks(row, col + 1);
        int i,j;
        for (i = 0; i < row; i++) {
            X_bias.Mat[i][0] = 1; // mengisi kolom pertama dengan konstanta 1
            for (j = 0; j < col; j++) {
                X_bias.Mat[i][j + 1] = X.Mat[i][j];
            }
        }

        // b = (X^T * X)^-1 * X^T * Y
        // (X^T * X) b = X^T * Y
        // metode Eliminasi Gauss untuk mendapatkan b dengan membentuk matriks augmented [XTX | XTY]
        Matriks XT = linalg.transposeMatriks(X_bias);
        Matriks XTX = linalg.perkalianMatriks(XT, X_bias);
        Matriks XTY = linalg.perkalianMatriks(XT, Y);
        Matriks augmented = XTX.concat(XTY, true);
        System.out.println("Augmented: ");
        augmented.printMatriks();
        Matriks b = SPL.metodeGauss(augmented);

        return b;
    }
    /**
     * Prediksi nilai Y berdasarkan perhitungan 
     * @param XNew matriks input baru (matriks kolom)
     * @param b matriks koefisien (matriks kolom)
     */
    public double predict(Matriks XNew, Matriks b) {
        double Y = b.Mat[0][0];
        for (int i = 1; i < b.getRow(); i++) {
            Y += b.Mat[i][0] * XNew.Mat[i - 1][0];
        }
        return Y;
    }
}
