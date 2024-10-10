import algeo.Matriks;
import algeo.SistemPersamaanLinier;
import algeo.Linalg;
import algeo.BicubicSplineInterpolation;
import algeo.Polinomial;

public class Main {
    public static void main(String[] args) {
        Polinomial p1 = new Polinomial();
        System.out.println(p1.getDegree());
        System.out.println(p1.calculate(1000));
        p1 = new Polinomial(3);
        System.out.println(p1.getDegree());
        p1.printCoefficients();
        Matriks points = new Matriks(new float[][]{{1, 2}, {2, 3}, {3, 4}, {4, 5}}); 
        float[] coefficients = {1, 2, 3, 0, 1, 0};
        p1.interpolate(points);
        System.out.println(p1.getDegree());
        p1.printCoefficients();
        System.out.println(p1.calculate(1));
        Polinomial p2 = new Polinomial(coefficients);
        p2.printCoefficients();
        System.out.println(p2.getDegree());
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
        // Matriks M = bsi.matriksX();
        // M.printMatriks();
    }
}    

