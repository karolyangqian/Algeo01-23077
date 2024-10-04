package algeo;
public class SistemPersamaanLinier {
    
     /**
     * Menyelesaikan SPL dengan Metode Gauss
     * @param Mat Matriks Augmented dari SPL
     * @return matriks solusi dari SPL, dengan baris ke-i adalah solusi untuk xi
    */
    public Matriks MetodeGauss(Matriks Mat){
        Linalg linalg = new Linalg();
        Matriks M = new Matriks(Mat);
        M = M.addRowZero(Math.max(0, (M.getCol()-1) - M.getRow()));
        
        M = linalg.toEselonBaris(M);

        for (int i = M.getRow()-1; i >= 0; i--){
            int idx = indexNotZero(M.Mat[i]);
            if (idx == -1) continue;
            M = linalg.tukarBaris(M, i, idx);

        }
        
        Matriks A = M.popCol(M.getCol() - 1);
        float[] B = M.getColElements(M.getCol() - 1);
        
        if (!this.solutionExist(A, B)){
            return null;
        }
        
        Matriks solution = this.generateSolution(A, B);
        return solution;
    }
    
    private boolean solutionExist(Matriks A, float[] B){
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
    
    private int indexNotZero(float[] arr){
        for (int i = 0; i < arr.length; i++){
            if (arr[i] != 0) return i; 
        }
        return -1;
    }

    private Matriks generateSolution(Matriks A, float[] B){
        Matriks solution = new Matriks (A.getCol(), A.getCol()+1);
        Linalg linalg = new Linalg();
        // set nilai konstanta awal
        for (int i = 0; i < A.getCol(); i++){
            solution.Mat[i][0] = B[i];
        }

        // nama variabel
        char [] var = new char[A.getCol()+1];
        for (int i = 1; i <= A.getCol(); i++){
            var[i] = (char) ('a' + i);
        }

        for (int i = A.getRow()-1; i >=0; i--){
            int idx = indexNotZero(A.Mat[i]);
            if (idx == -1){
                solution.Mat[i][i] = 1;
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
