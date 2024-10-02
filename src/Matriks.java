class Matriks{
    // Atribut
    float[][] Mat;
    private int row, col;

    // Constructor
    Matriks(int row, int col){
        this.row = row;
        this.col = col;
        this.Mat = new float[row][col];
    }

    Matriks(Matriks M){
        this.row = M.row;
        this.col = M.col;
        this.Mat = M.Mat;
    }


    int getRow(){
        return this.row;
    }

    int getCol(){
        return this.col;
    }

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

    Matriks penjumlahamMatriks(Matriks M2){
        Matriks M = new Matriks(this.row, this.col);
        for(int i = 0; i < this.row; i++){
            for(int j = 0; j < this.col; j++){
                M.Mat[i][j] = this.Mat[i][j] + M2.Mat[i][j];
            }
        }

        return M;
    }
    

}