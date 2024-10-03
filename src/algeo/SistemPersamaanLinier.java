package algeo;
public class SistemPersamaanLinier extends Matriks {
    /**
     * Konstruktor kelas SistemPersamaanLinier
     * @param A matriks koefisien
     * @param b matriks konstanta
     */
    public SistemPersamaanLinier(Matriks A, Matriks b){
        super(A.getRow(), A.getCol() + 1);
        for (int i = 0; i < A.getRow(); i++) {
            for (int j = 0; j < A.getCol(); j++) {
                this.Mat[i][j] = A.Mat[i][j];
            }
            this.Mat[i][A.getCol()] = b.Mat[i][0];
        }
    }

    /**
     * Konstruktor kelas SistemPersamaanLinier
     * @param SPL matriks augmented yang merepresentasikan SPL
     */
    public SistemPersamaanLinier(Matriks SPL){
        super(SPL);
    }

    public SistemPersamaanLinier(float[][] SPL){
        super(SPL);
    }

    /**
     * @param M matriks augmented sistem persamaan linier
     * @return matriks solusi dari sistem persamaan linier
     */
    // public Matriks metodeGauss(Matriks M) {
        
    // }
    public boolean isEselonBaris() {
        int i = 0; int j = 0;
        while (i < this.getRow()) {
            while (j < this.getCol() && this.Mat[i][j] == 0) {
                j++;
            }
            if (this.Mat[i][j] == 1 && this.Mat[i][j+1] == 0) {
                i++;
            } else {
                return false;
            }
        }
        return true;
    }

    
    public SistemPersamaanLinier toEselonBaris() {
        Matriks M = new Matriks(this);
        int i = 0, j = 0;
        while (i < this.getRow() && j < this.getCol()) {
            for (int k = i; k < this.getRow(); k++) {
                if (M.Mat[k][j] != 0) {
                    M = this.reduksiKolomKeBawah(i, j);
                    break;
                }
                if (k == this.getRow() - 1) {
                    i--;
                }
            }
            j++;
            i++;
        }
        return new SistemPersamaanLinier(M);
    }

    // public SistemPersamaanLinier toEselonBarisTereduksi(){
    //     Matriks M = this.toEselonBaris();
    //     int i = 0, j = 0;

    // }

    /**
     * Mereduksi kolom di bawah row dan col menjadi 0 dan elemen di leadingOneRow dan leadingOneCol menjadi 1
     * Elemen di leadingOneRow dan leadingOneCol bebas
     * @param leadingOneRow
     * @param leadingOneCol
     * @return Matriks hasil reduksi
     */
    private SistemPersamaanLinier reduksiKolomKeBawah(int leadingOneRow, int leadingOneCol) {
        Linalg linalg = new Linalg();
        Matriks M = new Matriks(this);
        int i = leadingOneRow;
        while (i < this.getRow() && this.Mat[i][leadingOneCol] == 0) {
            i++;
        }
        if (i == this.getRow() && this.Mat[i][leadingOneCol] == 0) {
            return new SistemPersamaanLinier(M);
        }
        M = linalg.tukarBaris(M, i, leadingOneRow);
        M = linalg.kalikanBaris(M, leadingOneRow, 1 / M.Mat[leadingOneRow][leadingOneCol]);
        i = leadingOneRow + 1;
        while (i < this.getRow()) {
            if (M.Mat[i][leadingOneCol] != 0){
                M = linalg.jumlahKelipatanBaris(M, i, leadingOneRow, -M.Mat[i][leadingOneCol]);
            }
            i++;
        }
        return new SistemPersamaanLinier(M);
    }

    /**
     * Mereduksi kolom di atas row dan col menjadi 0.
     * Prakondisi: eLemen di leadingOneRow dan leadingOneCol harus 1
     * @param leadingOneRow
     * @param leadingOneCol
     * @return
     */
    private SistemPersamaanLinier reduksiKolomKeAtas(int leadingOneRow, int leadingOneCol) {
        Linalg linalg = new Linalg();
        Matriks M = new Matriks(this);
        if (M.Mat[leadingOneRow][leadingOneCol] != 1) {
            System.err.println("Leading one bukan 1");
            return new SistemPersamaanLinier(M);
        }
        for (int i = leadingOneRow - 1; i >= 0; i--) {
            if (M.Mat[i][leadingOneCol] != 0) {
                M = linalg.jumlahKelipatanBaris(M, i, leadingOneRow, -M.Mat[i][leadingOneCol]);
            }
        }
        return new SistemPersamaanLinier(M);
    }

    public static void main(String[] args) {
        float[][] A = {
            {4, 2, 3, 4},
            {5, 1, 6, 7},
            {0, 4, 0, 2}
        };
        Matriks M = new Matriks(A);
        M.printMatriks();
        SistemPersamaanLinier SPL = new SistemPersamaanLinier(M);
        System.err.println(SPL.isEselonBaris());
        SPL = SPL.toEselonBaris();
        SPL.printMatriks();
        System.out.println();
        SPL.reduksiKolomKeAtas(1, 1).printMatriks();
        // System.out.println(M.getCol());
        // SPL.reduksiKolomKeBawah(2, 3).printMatriks();
    }
}
