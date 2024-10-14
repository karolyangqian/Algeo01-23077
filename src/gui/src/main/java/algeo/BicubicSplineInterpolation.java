package algeo;

public class BicubicSplineInterpolation {

    public double BicubicSplineInterpolate(Matriks fValue, double x, double y){
        double[] a = this.findAMatriks(fValue);
        double res = 0;
        int idx = 0;
        for (int i = 0; i < 4; i++){
            for (int j = 0; j < 4; j++){
                res += a[idx]*Math.pow(x, i)*Math.pow(y, j);
                idx++;
            }
        }
        return res;
    }

    private double[] findAMatriks(Matriks fValue){
        SistemPersamaanLinier spl = new SistemPersamaanLinier();
        Matriks X = this.matriksX();
        Matriks augmented = X.addColZero(1);
        int row = 0;
        for (int i = 0; i < 4; i++){
            for (int j = 0; j < 4; j++){
                augmented.Mat[row][augmented.getCol()-1] = fValue.Mat[i][j];
                row++;
            }
        }
        Matriks a = spl.metodeInversMatriks(augmented);
        double []res = new double[16];
        for (int i = 0; i < 16; i++){
            res[i] = a.Mat[i][0];
        }

        return res;
    }

    public Matriks matriksX(){
        Matriks M = new Matriks(16, 16);
        int row = 0;
        for (int k = 0; k < 4; k++){
            for (int x = 0; x <=1; x++){
                for (int y = 0; y <= 1; y++){
                    int col = 0;
                    if (k == 0){
                        for (int i = 0; i <= 3; i++){
                            for (int j = 0; j <= 3; j++){
                                M.Mat[row][col] = Math.pow(x, i)*Math.pow(y, j);
                                col++;
                            }
                        }
                    }
                    else if (k == 1){
                        for (int j = 0; j <= 3; j++){
                            for (int i = 0; i <= 3; i++){
                                if (i == 0) M.Mat[row][col] = 0;
                                else M.Mat[row][col] = i*Math.pow(y, i-1)*Math.pow(x, j);
                                col++;
                            }
                        }
                    }
                    else if (k == 2){
                        for (int j = 0; j <= 3; j++){
                            for (int i = 0; i <= 3; i++){
                                if (j==0) M.Mat[row][col] = 0;
                                else M.Mat[row][col] = j*Math.pow(y, i)*Math.pow(x, j-1);
                                col++;
                            }
                        }
                    }
                    else{
                        for (int j = 0; j <= 3; j++){
                            for (int i = 0; i <= 3; i++){
                                if (i == 0 || j == 0) M.Mat[row][col] = 0;
                                else M.Mat[row][col] = i*j*Math.pow(y, i-1)*Math.pow(x, j-1);
                                col++;
                            }
                        }
                    }
                    row++;
                }
            }

        }
        return M;
        

    }
}
