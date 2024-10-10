import algeo.Matriks;
import algeo.SistemPersamaanLinier;
import algeo.Linalg;
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

        
        Matriks m1 = new Matriks(new float[][]{
                                            {1, 2, 3}, 
                                            {4, 5, 6}, 
                                            {7, 8, 9}});
    }
}    

