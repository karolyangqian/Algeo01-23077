/**
 * Kelas Matriks
 */
public class Matriks{
    /**
     * Atribut
     */
    float[][] Mat;
    private int row, col;

    /**
     * Constructor
     */
    public Matriks(){
        this.row = 0;
        this.col = 0;
        this.Mat = new float[0][0];
    }

    /**
     * Constructor
     * @param row jumlah baris
     * @param col jumlah kolom
     */
    public Matriks(int row, int col){
        this.row = row;
        this.col = col;
        this.Mat = new float[row][col];
    }

    /**
     * Constructor
     * @param M Matriks yang akan di copy
     */
    public Matriks(Matriks M){
        this.row = M.row;
        this.col = M.col;
        this.Mat = M.Mat;
    }

    /**
     * Constructor
     * @param arrayOfArrayFloat array of array float yang akan dijadikan matriks
     */
    public Matriks(float[][] arrayOfArrayFloat){
        this.row = arrayOfArrayFloat.length;
        this.col = arrayOfArrayFloat[0].length;
        this.Mat = new float[this.row][this.col];
        for (int i = 0; i < this.row; i++){
            for (int j = 0; j < this.col; j++){
                this.Mat[i][j] = arrayOfArrayFloat[i][j];
            }
        }
    }

    /**
     * Getter baris
     * @return jumlah baris dari matriks
     */
    public int getRow(){
        return this.row;
    }

    /**
     * Getter kolom
     * @return jumlah kolom dari matriks
     */
    public int getCol(){
        return this.col;
    }

    /**
     * Menampilkan matriks ke terminal
     */
    public void printMatriks(){
        for (int i = 0; i < this.row; i++){
            for (int j = 0; j < this.col; j++){
                System.out.print(this.Mat[i][j] + " ");
            }
            System.out.println();
        }
    }

    /**
     * Mngisi setiap elemen matriks dengan nilai 0
     */
    public void fillMatriksZero(){
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
    public Matriks removeRowColMatriks(int row, int col){
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
    public Matriks kaliXMatriks(float x){
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
    public Matriks perkalianMatriks(Matriks M2){
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
    public Matriks penjumlahamMatriks(Matriks M2){
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
    public Matriks transposeMatriks(){
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
    public Matriks kofaktorMatriks(){
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
    public Matriks adjointMatriks(){
        Matriks M = this.kofaktorMatriks();
        M = M.transposeMatriks();
        return M;
    }

    /**
     * Menghitung determinan matriks
     * @param method metode yang digunakan untuk menghitung determinan
     * @return nilai determinan matriks
     */
    public float determinanMatriks(String method){
        
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
    public Matriks inversMatriks(){
        Matriks M = new Matriks(this.row, this.col);
        float det = this.determinanMatriks("reduksi");
        M = this.adjointMatriks();

        M = M.kaliXMatriks(1/det);
        return M;
    }

    // Operasi Baris Elementer

    /**
     * Tukar baris pada matriks
     * @category Operasi Baris Elementer
     * @param row1 baris yang mau ditukar
     * @param row2 baris yang mau ditukar
     * @return matriks baru yang sudah ditukar barisnya
     */
    public Matriks tukarBaris(int row1, int row2) {
        if (row1 == row2) {
            return new Matriks(this);
        }
        Matriks M = new Matriks(this);
        int col = M.getRow();
        for (int i = 0; i < col; i++) {
            float temp = M.Mat[row1][i];
            M.Mat[row1][i] = M.Mat[row2][i];
            M.Mat[row2][i] = temp;
        }
        return M;
    }

    /**
     * Kalikan baris pada matriks dengan skalar x
     * @category Operasi Baris Elementer
     * @param row baris yang akan dikalikan
     * @param x nilai kelipatan
     * @return matriks baru yang sudah dikalikan barisnya
     */
    public Matriks kalikanBaris(int row, float x) {
        Matriks M = new Matriks(this);
        int col = M.getCol();
        for (int i = 0; i < col; i++) {
            M.Mat[row][i] *= x;
        }
        return M;
    }

    /**
     * Jumlahkan kelipatan baris pada matriks
     * @category Operasi Baris Elementer
     * @param M matriks yang akan diubah
     * @param row1 baris yang akan diubah
     * @param row2 baris yang akan dijumlahkan ke row1
     * @param x nilai kelipatan
     */
    public Matriks jumlahKelipatanBaris(int row1, int row2, float x) {
        Matriks M = new Matriks(this);
        int col = M.getCol();
        for (int i = 0; i < col; i++) {
            M.Mat[row1][i] += x * M.Mat[row2][i];
        }
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
        
        M = this.tukarBaris(0, barisAtas);

        for (int j = 0; j < M.col; j++){
            M = this.kalikanBaris(j, (float) 1/ (float) M.Mat[j][j]);
            for (int i = 0; i < M.row; i++){
                if (i != j){
                    if (M.Mat[i][j] != 0){
                        M = this.jumlahKelipatanBaris(i, j, -1 * M.Mat[i][j]);
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