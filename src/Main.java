import algeo.Matriks;
import algeo.SistemPersamaanLinier;
import algeo.Linalg;
import algeo.BicubicSplineInterpolation;

public class Main {
    public static void main(String[] args) {
        Linalg linalg = new Linalg();
        BicubicSplineInterpolation bsi = new BicubicSplineInterpolation();
        SistemPersamaanLinier SPL = new SistemPersamaanLinier();
        // double[][] A = {
        //     {1, 1, 1, 0},
        //     {2, 3, 1, 1},
        //     {3, 1, 2, 1}
        // };
        double[][] A = {
            {2, 3, -1, 5},
            {4, 4, -3, 3}, 
            {-2, 3, -1, 1}
        };
        double[][] B = {
            {1, 3, -2, 0, 2, 0, 0},              
            {2, 6, -5, -2, 4, -3, -1},
            {0, 0, 5, 10, 0, 15, 5},
            {2, 6, 0, 8, 4, 18, 6}
        };
        Matriks M = bsi.matriksX();
        M.printMatriks();
    }
}    

