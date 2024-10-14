package algeo;

public class LinearRegression {
    /* untuk menambahkan quadratic features dalam metode Quadratic Linear Regression */
    public Matriks addQuadratic(Matriks X) {
        int row = X.getRow();
        int col = X.getCol();
        Matriks newX = new Matriks(row, col + col + (col * (col - 1)) / 2);
        for (int i = 0; i < row; i++) {
            int idx = 0;

            // variabel linier
            for (int j = 0; j < col; j++) {
                newX.Mat[i][idx++] = X.Mat[i][j];
            }

            // variabel kuadrat
            for (int j = 0; j < col; j++) {
                newX.Mat[i][idx++] = (float) Math.pow(X.Mat[i][j], 2);
            }

            // variabel interaksi
            for (int j = 0; j < col; j++) {
                for (int k = j + 1; k < col; k++) {
                    newX.Mat[i][idx++] = X.Mat[i][j] * X.Mat[i][k];
                }
            }
        }
        return newX;
    }
    /* perhitungan Miltiple Linear Regression dengan Normal Equation */
    public Matriks normalEquation(Matriks X, Matriks Y) {
        Linalg linalg = new Linalg();
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
        // X^T
        Matriks XT = linalg.transposeMatriks(X_bias);
        // X^T * X
        Matriks XTX = linalg.perkalianMatriks(XT, X_bias);
        // XTX inverse
        Matriks XTX_inv = linalg.inversMatriks(XTX);
        // XTX inverse * X^T
        Matriks XTX_inv_XT = linalg.perkalianMatriks(XTX_inv, XT);
        // XTX inverse * X^T * Y
        Matriks b = linalg.perkalianMatriks(XTX_inv_XT, Y);

        return b;
    }
    /* prediksi nilai Y berdasarkan perhitungan */
    public double predict(Matriks X, Matriks b) {
        double Y = b.Mat[0][0];
        for (int i = 1; i < b.getCol(); i++) {
            Y += b.Mat[0][i] * X.Mat[0][i];
        }
        return Y;
    }
}