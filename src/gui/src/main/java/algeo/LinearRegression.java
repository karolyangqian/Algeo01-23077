package algeo;

public class LinearRegression {
    /**
     * Atribut
     */
    private Matriks X;
    private Matriks Y;
    private Matriks b;

    /**
     * Constructor
     * Membentuk model Linear Regression tanpa data
     */
    public LinearRegression() {
        this.X = null;
        this.Y = null;
        this.b = null;
    }

    /**
     * Constructor
     * Membentuk model Linear Regression dengan data awal
     * @param X Matriks fitur
     * @param Y Matriks target
     */
    public LinearRegression(Matriks X, Matriks Y) {
        this.X = X;
        this.Y = Y;
        this.b = null;
    }


    /**
     * Menambahkan quadratic features dalam metode Quadratic Linear Regression
     * sebelum diolah dengan Normal Equation dan prediksi nilai Y
     * @param X Matriks input
     * @return Matriks dengan quadratic features ditambahkan
     */
    public Matriks addQuadratic() {
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

    /* perhitungan Multiple Linear Regression dengan Normal Equation */
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
        Matriks b = SPL.metodeGauss(augmented);

        this.b = b;
        return b;
    }

    /**
     * Memprediksi nilai Y baru berdasarkan model
     * @param X Matriks data baru
     * @param b Matriks koefisien regresi
     * @return nilai prediksi Y
     */
    public double predict() {
        double Y = b.Mat[0][0];
        for (int i = 1; i < b.getRow(); i++) {
            Y += b.Mat[i][0] * X.Mat[0][i - 1];
        }
        return Y;
    }

    /**
     * Mengatur data input X dan Y
     * @param X Matriks fitur
     * @param Y Matriks target
     */
    public void setData(Matriks X, Matriks Y) {
        this.X = X;
        this.Y = Y;
    }
}
