package algeo;
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
        this.Mat = new float[this.row][this.col];
        for (int i = 0; i < this.row; i++){
            for (int j = 0; j < this.col; j++){
                this.Mat[i][j] = M.Mat[i][j];
            }
        }
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
     * 
     * @param col
     * @param N
     * @return
     */
    public Matriks replaceColumn(int col, Matriks N) {
        Matriks M = new Matriks(this);
        for (int i = 0; i < this.row; i++) {
            M.Mat[i][col] = N.Mat[i][0];
        }
        return M;
    }

    /**
     * 
     * @param row
     * @param N
     * @return
     */
    public Matriks replaceRow(int row, Matriks N) {
        Matriks M = new Matriks(this);
        for (int i = 0; i < this.col; i++) {
            M.Mat[row][i] = N.Mat[0][i];
        }
        return M;
    }


    /**
     * Mengisi setiap elemen matriks dengan nilai x
     */
    public void fill(float x){
        for (int i = 0; i < this.row; i++){
            for (int j = 0; j < this.col; j++){
                this.Mat[i][j] = x;
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
     * Membuat nilai -0.00 menjadi 0.00
     */
    public void makePositiveZero(){
        for (int i = 0; i < this.row; i++){
            for (int j = 0; j < this.col; j++){
                if (this.Mat[i][j] == -0){
                    this.Mat[i][j] = 0;
                }
            }
        }
    }

    /**
     * Menambahkan baris sebanyak numRowAdded dan baris-baris baru diisi dengan 0
    */
    public Matriks addRowZero(int numRowAdded){
        Matriks M = new Matriks(this.row + numRowAdded, this.col);
        for (int i = 0; i < this.row; i++){
            for (int j = 0; j < this.col; j++){
                M.Mat[i][j] = this.Mat[i][j];
            }
        }
        for (int i = this.row; i < M.getRow(); i++){
            for (int j = 0; j < this.col; j++){
                M.Mat[i][j] = 0;
            }
        }
        return M;
    }

    /**
     * Menambahkan kolom sebanyak numColAdded dan kolom-kolom baru diisi dengan 0
    */
    public Matriks addColZero(int numColAdded){
        Matriks M = new Matriks(this.row, this.col + numColAdded);
        for (int i = 0; i < this.row; i++){
            for (int j = 0; j < this.col; j++){
                M.Mat[i][j] = this.Mat[i][j];
            }
        }
        for (int i = 0; i < this.row; i++){
            for (int j = this.col; j < M.getCol(); j++){
                M.Mat[i][j] = 0;
            }
        }
        return M;
    }
    
    /**
     * Menggabungkan dua matriks
     * @param M
     * @param isRight jika true, matriks digabungkan ke kanan, jika false, matriks digabungkan ke bawah
     * @return
     */
    public Matriks concat(Matriks M, boolean isRight) {
        Matriks N;
        if (isRight) {
            N = new Matriks(this.row, this.col + M.col);
            for (int i = 0; i < this.row; i++) {
                for (int j = 0; j < this.col; j++) {
                    N.Mat[i][j] = this.Mat[i][j];
                }
                for (int j = 0; j < M.col; j++) {
                    N.Mat[i][j + this.col] = M.Mat[i][j];
                }
            }
        } else {
            N = new Matriks(this.row + M.row, this.col);
            for (int i = 0; i < this.row; i++) {
                for (int j = 0; j < this.col; j++) {
                    N.Mat[i][j] = this.Mat[i][j];
                }
            }
            for (int i = 0; i < M.row; i++) {
                for (int j = 0; j < M.col; j++) {
                    N.Mat[i + this.row][j] = M.Mat[i][j];
                }
            }
        }
        return N;
    }

    /**
     * Menghapus baris tertentu dari matriks
     * @param row
     * @return
     */
    public Matriks popRow(int row) {
        Matriks M = new Matriks(this.row - 1, this.col);
        for (int i = 0; i < this.row; i++) {
            if (i < row) {
                for (int j = 0; j < this.col; j++) {
                    M.Mat[i][j] = this.Mat[i][j];
                }
            } else if (i > row) {
                for (int j = 0; j < this.col; j++) {
                    M.Mat[i - 1][j] = this.Mat[i][j];
                }
            }
        }
        return M;
    }

    /**
     * Menghapus kolom tertentu dari matriks
     * @param col
     * @return
     */
    public Matriks popCol(int col) {
        Matriks M = new Matriks(this.row, this.col - 1);
        for (int i = 0; i < this.row; i++) {
            for (int j = 0; j < this.col; j++) {
                if (j < col) {
                    M.Mat[i][j] = this.Mat[i][j];
                } else if (j > col) {
                    M.Mat[i][j - 1] = this.Mat[i][j];
                }
            }
        }
        return M;
    }

    /**
     * Mengambil baris tertentu dari matriks
     * @param row
     * @return
     */
    public float[] getRowElements(int row) {
        float[] M = new float[this.col];
        for (int i = 0; i < this.col; i++) {
            M[i] = this.Mat[row][i];
        }
        return M;
    }

    /**
     * Mengambil kolom tertentu dari matriks
     * @param col
     * @return
     */
    public float[] getColElements(int col) {
        float[] M = new float[this.row];
        for (int i = 0; i < this.row; i++) {
            M[i] = this.Mat[i][col];
        }
        return M;
    }
}