package algeo;

interface PolinomialInterface {
    public void setCoefficients(float[] coefficients);
    public void interpolate(Matriks points);
    public int getDegree();
    public float[] getCoefficients();
    public float calculate(float x);
}

public class Polinomial implements PolinomialInterface{
    /**
     * Atribut
     */
    private int degree;
    private float[] coefficients;

    /**
     * Constructor
     * Membentuk polinomial konstan 0
     */
    public Polinomial() {
        this.degree = 0;
        this.coefficients = new float[1];
        this.coefficients[0] = 0;
    }

    /**
     * Constructor
     * Membentuk polinomial dari koefisien tertentu
     * Contoh: Polinomial([1, 2, 3]) akan membentuk polinomial 1 + 2x + 3x^2
     * @param coefficients bukan array kosong
     */
    public Polinomial(float[] coefficients){
        this.coefficients = coefficients;
        this.updateDegree();
    }


    /**
     * Constructor
     * Membentuk polinomial dari derajat tertentu dengan koefisien 1  dan konstanta 0
     * Contoh: Polinomial(3) akan membentuk polinomial x + x^2 + x^3
     * @param degree
     */
    public Polinomial(int degree){
        this.degree = degree;
        this.coefficients = new float[degree + 1];
        this.coefficients[0] = 0;
        for (int i = 1; i <= degree; i++){
            this.coefficients[i] = 1;
        }
    }

    /**
     * Memperbarui koefisien polinomial
     */
    public void setCoefficients(float[] coefficients){
        this.coefficients = coefficients;
        this.updateDegree();
    }

    /**
     * Melakukan interpolasi terhadap titik-titik yang diberikan
     * @param points matriks titik-titik yang akan diinterpolasi
     * Contoh: points = [[1, 2], [3, 4], [5, 6]] akan menghasilkan polinomial yang melewati titik (1, 2), (3, 4), dan (5, 6)
     */
    public void interpolate(Matriks points){
        Linalg linalg = new Linalg();
        SistemPersamaanLinier SPL = new SistemPersamaanLinier();
        points = linalg.transposeMatriks(points);
        Matriks Augmented = new Matriks(points.getCol(), points.getCol() + 1);
        for (int i = 0; i < Augmented.getRow(); i++){
            for (int j = 0; j < Augmented.getCol() - 1; j++){
                Augmented.Mat[i][j] = (float) Math.pow(points.Mat[0][i], j);
            }
            Augmented.Mat[i][points.getCol()] = points.Mat[1][i];
        }
        Matriks Coefficients = SPL.metodeGaussJordan(Augmented);
        this.setCoefficients(Coefficients.getColElements(0));
        this.updateDegree();
    }

    /**
     * Mengembalikan derajat polinomial
     */
    public int getDegree(){
        return this.degree;
    }

    /**
     * Mengembalikan koefisien polinomial
     * Contoh: Polinomial([1, 2, 3]).getCoefficients() akan mengembalikan [1, 2, 3]
     */
    public float[] getCoefficients(){
        return this.coefficients;
    }

    /**
     * Menghitung nilai polinomial pada titik x
     * Contoh: Polinomial([1, 2, 3]).calculate(2) akan mengembalikan 17
     */
    public float calculate(float x){
        float result = 0;
        for (int i = 0; i <= this.degree; i++){
            result += this.coefficients[i] * Math.pow(x, i);
        }
        return result;
    }

    /**
     * Menampilkan koefisien polinomial
     * Format: menampilkan a0 a1 a2 ... an untuk polinomial a0 + a1x + a2x^2 + ... + anx^n
     */
    public void printCoefficients() {
        for (int i = 0; i < this.coefficients.length; i++) {
            System.out.print(this.coefficients[i] + " ");
        }
        System.out.println();
    }

    private void updateDegree(){
        for (int i = this.coefficients.length - 1; i >= 0; i--) {
            if (this.coefficients[i] != 0) {
                this.degree = i;
                break;
            }
        }
    }
}
