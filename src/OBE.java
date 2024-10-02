class OperasiBarisElementer {
    void tukarBaris(Matriks M, int row1, int row2) {
        int col = M.getRow();
        for (int i = 0; i < col; i++) {
            float temp = M.Mat[row1][i];
            M.Mat[row1][i] = M.Mat[row2][i];
            M.Mat[row2][i] = temp;
        }
    }
    void kalikanBaris(Matriks M, int row, float x) {
        int col = M.getRow();
        for (int i = 0; i < col; i++) {
            M.Mat[row][i] *= x;
        }
    }
    void jumlahKelipatanBaris(Matriks M, int row1, int row2, float x) {
        int col = M.getRow();
        for (int i = 0; i < col; i++) {
            M.Mat[row1][i] += x * M.Mat[row2][i];
        }
    }
}