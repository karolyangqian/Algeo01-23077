package algeo;
public class SistemPersamaanLinier {
    
     /**
     * Menyelesaikan SPL dengan Metode Gauss
     * @param Mat Matriks Augmented dari SPL
     * @return matriks solusi dari SPL, dengan baris ke-i adalah solusi untuk xi
    */
    public Matriks metodeGauss(Matriks Mat){
        Linalg linalg = new Linalg();
        Matriks M = new Matriks(Mat);
        M = M.addRowZero(Math.max(0, (M.getCol()-1) - M.getRow()));
        M = linalg.toEselonBaris(M);
        M = this.sesuaikanBarisLeadingOne(M);   

        Matriks A = M.popCol(M.getCol() - 1);
        double[] B = M.getColElements(M.getCol() - 1);
        
        if (!this.solutionExist(A, B)) return null;
        
        Matriks solution = this.generateSolution(A, B);
        return solution;
    }

    /**
     * Menyelesaikan SPL dengan Metode Gauss-Jordan
     * @param Mat Matriks Augmented dari SPL
     * @return matriks solusi dari SPL, dengan baris ke-i adalah solusi untuk xi
    */
    public Matriks metodeGaussJordan(Matriks Mat){
        Linalg linalg = new Linalg();
        Matriks M = new Matriks(Mat);
        M = M.addRowZero(Math.max(0, (M.getCol()-1) - M.getRow()));
        
        M = linalg.toEselonBarisTereduksi(M);
        M = this.sesuaikanBarisLeadingOne(M);

        Matriks A = M.popCol(M.getCol() - 1);
        double[] B = M.getColElements(M.getCol() - 1);
        
        if (!this.solutionExist(A, B)) return null;
        
        Matriks solution = this.generateSolution(A, B);
        return solution;
    }

    /**
     * Menyelesaikan SPL dengan Metode Cramer
     * @param Mat Matriks Augmented dari SPL
     * @return matriks solusi dari SPL, dengan baris ke-i adalah solusi untuk xi
    */
    public Matriks metodeCramer(Matriks Mat){
        Linalg linalg = new Linalg();

        Matriks A = Mat.popCol(Mat.getCol() - 1);
        double[] B = Mat.getColElements(Mat.getCol() - 1);
        double det = linalg.determinanMatriks(A, "reduksi");
        if (det == 0 || A.getRow() != A.getCol()){
            return null;
        }

        Matriks solution = new Matriks(A.getCol(), A.getCol()+1);
        solution.fill(0);

        for (int i = 0; i < solution.getRow(); i++){
            Matriks M = A.replaceColumn(i, B);
            solution.Mat[i][0] = linalg.determinanMatriks(M, "reduksi")/det;
        }

        return solution;

    }

    /**
     * Menyelesaikan SPL dengan Metode Invers
     * @param Mat Matriks Augmented dari SPL
     * @return matriks solusi dari SPL, dengan baris ke-i adalah solusi untuk xi
    */
    public Matriks metodeInversMatriks(Matriks Mat){
        Linalg linalg = new Linalg();

        Matriks A = Mat.popCol(Mat.getCol() - 1);
        double[] B = Mat.getColElements(Mat.getCol() - 1);

        Matriks inversA = linalg.inversMatriks(A);
        
        if (inversA == null) return null;

        Matriks b = new Matriks(B.length, 1);
        for (int i = 0; i < b.getRow(); i++){
            b.Mat[i][0] = B[i];
        }

        Matriks solution = linalg.perkalianMatriks(inversA, b);

        return solution;
    }


    /**
     * Menyusun matriks sesuai dengan baris leading one
    */
    private Matriks sesuaikanBarisLeadingOne(Matriks M){
        Linalg linalg = new Linalg();
        for (int i = M.getRow()-1; i >= 0; i--){
            int idx = indexNotZero(M.Mat[i]);
            if (idx == -1) continue;
            M = linalg.tukarBaris(M, i, idx);

        }
        return M;
    }

    /**
     * Mengecek apakah ada solusi dari SPL
    */
    private boolean solutionExist(Matriks A, double[] B){
        boolean semuanol = true;
        for (int j = 0; j < A.getCol(); j++){
            if (A.Mat[A.getRow()-1][j] != 0){
                semuanol = false;
                break;
            }   
        }
        if (semuanol && B[A.getRow()-1] != 0){
            return false;
        }
        return true;
    }
    
    /**
     * Mencari index pertama yang bernilai tidak nol dari array
    */
    private int indexNotZero(double[] arr){
        for (int i = 0; i < arr.length; i++){
            if (arr[i] != 0) return i; 
        }
        return -1;
    }

    /**
     * Menghasilkan matriks solusi dari SPL
    */
    private Matriks generateSolution(Matriks A, double[] B){
        Matriks solution = new Matriks (A.getCol(), A.getCol()+1);
        Linalg linalg = new Linalg();
        // set nilai konstanta awal
        for (int i = 0; i < A.getCol(); i++){
            solution.Mat[i][0] = B[i];
        }

        // nama variabel7
        char [] var = new char[A.getCol()+1];
        for (int i = 1; i <= A.getCol(); i++){
            var[i] = (char) ('a' + i);
        }

        for (int i = A.getRow()-1; i >=0; i--){
            int idx = indexNotZero(A.Mat[i]);
            if (idx == -1){
                solution.Mat[i][i+1] = 1;
                continue;
            }
            for (int j = idx+1; j < A.getCol(); j++){
                if (A.Mat[i][j] != 0){
                    solution = linalg.jumlahKelipatanBaris(solution, idx, j, -1*A.Mat[i][j]);
                }
            }

        }
        // solution.printMatriks();
        return solution;

    }
    
}
