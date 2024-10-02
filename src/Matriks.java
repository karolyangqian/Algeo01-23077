class Matriks{
    /**
     * Atribut
     */
    float[][] Mat;
    private int row, col;

    /**
     * Constructor
     * @param row jumlah baris
     * @param col jumlah kolom
     */
    Matriks(int row, int col){
        this.row = row;
        this.col = col;
        this.Mat = new float[row][col];
    }

    /**
     * Constructor
     * @param M Matriks yang akan di copy
     */
    Matriks(Matriks M){
        this.row = M.row;
        this.col = M.col;
        this.Mat = M.Mat;
    }

    /**
     * Getter baris
     * @return jumlah baris dari matriks
     */
    int getRow(){
        return this.row;
    }

    /**
     * Getter kolom
     * @return jumlah kolom dari matriks
     */
    int getCol(){
        return this.col;
    }

    /**
     * Mngisi setiap elemen matriks dengan nilai 0
     */
    void fillMatriksZero(){
        for (int i = 0; i < this.row; i++){
            for (int j = 0; j < this.col; j++){
                this.Mat[i][j] = 0;
            }
        }
    }

    /**
     * Menghapus baris dan kolom tertentu dari matriks
     * @param row baris yang akan dihapus
     * @param col kolom yang akan dihapus
     * @return matriks baru yang sudah dihapus baris dan kolom tertentu
    */
    Matriks removeRowColMatriks(int row, int col){
        Matriks M = new Matriks(this.row - 1, this.col - 1);
        for (int i = 0; i < this.row; i++){
            for (int j = 0; j < this.col; j++){
                if (i < row && j < col){
                    M.Mat[i][j] = this.Mat[i][j];
                }
                else if (i < row && j > col){
                    M.Mat[i][j - 1] = this.Mat[i][j];
                }
                else if (i > row && j < col){
                    M.Mat[i - 1][j] = this.Mat[i][j];
                }
                else if (i > row && j > col){
                    M.Mat[i - 1][j - 1] = this.Mat[i][j];
                }
            }
        }
        return M;
    }

    /**
     * Mengalikan matriks dengan skalar x
     * @param x nilai skalar
     * @return matriks baru yang sudah dikali dengan skalar x
     */
    Matriks kaliXMatriks(float x){
        Matriks M = new Matriks(this.row, this.col);
        for (int i = 0; i < this.row; i++){
            for (int j = 0; j < this.col; j++){
                M.Mat[i][j] = this.Mat[i][j] * x;
            }
        }
        return M;
    }

    /**
     * Melakukan operasi perkalian matriks (instance * M2)
     * @param M2 Matriks yang akan dikalikan
     * @return matriks baru hasil perkalian
     */
    Matriks perkalianMatriks(Matriks M2){
        Matriks M = new Matriks(this.row, M2.col);
        for (int i = 0; i < this.row; i++){
            for (int j = 0; j < M2.col; j++){
                M.Mat[i][j] = 0;
                for (int k = 0; k < this.col; k++){
                    M.Mat[i][j] += this.Mat[i][k] * M2.Mat[k][j];
                }
            }
        }

        return M;
    }

    /**
     * Melakukan operasi penjumlahan matriks
     * @param M2 Matriks yang akan dijumlahkan dengan instance
     * @return matriks baru hasil penjumlahan
     */
    Matriks penjumlahamMatriks(Matriks M2){
        Matriks M = new Matriks(this.row, this.col);
        for(int i = 0; i < this.row; i++){
            for(int j = 0; j < this.col; j++){
                M.Mat[i][j] = this.Mat[i][j] + M2.Mat[i][j];
            }
        }

        return M;
    }

    /**
     * Melakukan operasi transpose matriks
     * @return matriks baru hasil transpose
     */
    Matriks transposeMatriks(){
        Matriks M = new Matriks(this.col, this.row);
        for(int i = 0; i < this.row; i++){
            for(int j = 0; j < this.col; j++){
                M.Mat[j][i] = this.Mat[i][j];
            }
        }

        return M;
    }


    /**
     * Membuat matriks kofaktor
     * @return matriks kofaktor
     */
    Matriks kofaktorMatriks(){
        Matriks M = new Matriks(this.row, this.col);
        for (int i = 0; i < this.row; i++){
            for (int j = 0; j < this.col; j++){
                Matriks M2 = this.removeRowColMatriks(i, j);
                M.Mat[i][j] = (float) Math.pow(-1, i + j) * M2.determinanMatriks("reduksi");
                
            }
        }
        return M;
    }

    /**
     * Membuat matriks adjoint
     * @return matriks adjoint
     */
    Matriks adjointMatriks(){
        Matriks M = this.kofaktorMatriks();
        M = M.transposeMatriks();
        return M;
    }

    /**
     * Menghitung determinan matriks
     * @param method metode yang digunakan untuk menghitung determinan
     * @return nilai determinan matriks
     */
    float determinanMatriks(String method){
        
        // ingat cek apakah this.n == this.m
        if (method == "reduksi"){
            return this.detReduksi();
        }
        else{
            return this.detKofaktor();
        }
        
    }

    /**
     * Menghitung invers invers
     * @return invers matriks
     */
    Matriks inversMatriks(){
        Matriks M = new Matriks(this.row, this.col);
        float det = this.determinanMatriks("reduksi");
        M = this.adjointMatriks();

        M = M.kaliXMatriks(1/det);



        return M;
    }

    // Private
    /**
     * Menghitung determinan matriks dengan metode kofaktor
     * @return nilai determinan matriks
     */
    private float detKofaktor(){
        Matriks M = this.kofaktorMatriks();
        
        float det = 0;
        for (int i = 0; i < this.row; i++){
            det += this.Mat[0][i] * M.Mat[0][i];
        }
        return det;
    }

    /**
     * Menghitung determinan matriks dengan metode reduksi baris
     * @return nilai determinan matriks
     */
    private float detReduksi(){

        Matriks M = new Matriks(this);
        int barisAtas = 0;
        for (int i = 0; i < M.row; i++){
            if (M.Mat[i][0] != 0){
                barisAtas = i;
                break;
            }
        }
        
        OperasiBarisElementer OBE = new OperasiBarisElementer();
        OBE.tukarBaris(M, 0, barisAtas);

        for (int j = 0; j < M.col; j++){
            OBE.kalikanBaris(M, j, (float) 1/ (float) M.Mat[j][j]);
            for (int i = 0; i < M.row; i++){
                if (i != j){
                    if (M.Mat[i][j] != 0){
                        OBE.jumlahKelipatanBaris(M, i, j, -1 * M.Mat[i][j]);
                    }
                }
            }
        }

        float det = 1;
        for (int i = 0; i < M.row; i++){
            det *= M.Mat[i][i];
        };

        return det;

    }
    

}