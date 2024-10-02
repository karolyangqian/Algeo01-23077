public class Main {
    public static void main(String[] args) {
        Matriks M = new Matriks(3, 3);
        int val = 1;
        for (int i = 0; i < M.getRow(); i++) {
            for (int j = 0; j < M.getCol(); j++) {
                M.Mat[i][j] = val;
                val++;
            }
        }
        M.printMatriks();
        M = M.removeRowColMatriks(0, 0);
        M.printMatriks();

    }
}
