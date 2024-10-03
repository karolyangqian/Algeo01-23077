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
     * Mngisi setiap elemen matriks dengan nilai 0
     */
    public void fillMatriksZero(){
        for (int i = 0; i < this.row; i++){
            for (int j = 0; j < this.col; j++){
                this.Mat[i][j] = 0;
            }
        }
    }

    public Matriks replaceColumn(int col, Matriks N) {
        Matriks M = new Matriks(this);
        for (int i = 0; i < this.row; i++) {
            M.Mat[i][col] = N.Mat[i][0];
        }
        return M;
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

    
    

}