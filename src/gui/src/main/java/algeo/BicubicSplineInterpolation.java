package algeo;

import java.awt.image.BufferedImage;

public class BicubicSplineInterpolation {

    private static double resizeProgress = 0.0;
    private final double resizeProgressIncrement = 0.1;

    /**
     * Menghasilkan interpolasi bicubic spline dari nilai-nilai f pada titik (x, y)
     * 
     * @param x absis titik yang akan diinterpolasi
     * @param y ordinat titik yang akan diinterpolasi
     * @return nilai interpolasi bicubic spline pada titik (x, y)
     */
    public double BicubicSplineInterpolate(Matriks fValue, double x, double y){
        double[] a = this.findAMatriks(fValue);
        double res = 0;
        int idx = 0;
        for (int j = 0; j < 4; j++){
            for (int i = 0; i < 4; i++){
                res += a[idx]*Math.pow(x, i)*Math.pow(y, j);
                idx++;
            }
        }
        return res;
    }

    /**
     * Menghasilkan gambar yang telah diresize dengan skala x dan skala y dengan metode bicubic spline interpolation
     * 
     * @param image gambar yang akan diresize
     * @param scale_x pengali lebar gambar
     * @param scale_y pengali tinggi gambar
     * @return gambar yang telah diresize
     */
    public BufferedImage resizeImage(BufferedImage image, double scale_x, double scale_y){
        setResizeProgress(0);
        int width = image.getWidth();
        int height = image.getHeight();

        // buat matriks untuk setiap  warna
        Matriks imageMatriksRed = new Matriks(height, width);
        Matriks imageMatriksGreen = new Matriks(height, width);
        Matriks imageMatriksBlue = new Matriks(height, width);
        Matriks imageMatriksAlpha = new Matriks(height, width);

        for (int i = 0; i < height; i++){
            for (int j = 0; j < width; j++){
                imageMatriksAlpha.Mat[i][j] = (image.getRGB(j, i) >> 24) & 0xff;
                imageMatriksRed.Mat[i][j] = (image.getRGB(j, i) >> 16) & 0xff;
                imageMatriksGreen.Mat[i][j] = (image.getRGB(j, i) >> 8) & 0xff;
                imageMatriksBlue.Mat[i][j] = image.getRGB(j, i) & 0xff;
            }
        }

        // lakukan interpolasi untuk setiap warna
        Matriks newImageMatriksRed = this.imageInterpolate(imageMatriksRed, scale_x, scale_y);
        // setResizeProgress(getResizeProgress() + resizeProgressIncrement);
        Matriks newImageMatriksGreen = this.imageInterpolate(imageMatriksGreen, scale_x, scale_y);
        // setResizeProgress(getResizeProgress() + resizeProgressIncrement);
        Matriks newImageMatriksBlue = this.imageInterpolate(imageMatriksBlue, scale_x, scale_y);
        // setResizeProgress(getResizeProgress() + resizeProgressIncrement);
        Matriks newImageMatriksAlpha = this.imageInterpolate(imageMatriksAlpha, scale_x, scale_y);
        // setResizeProgress(getResizeProgress() + resizeProgressIncrement);

        BufferedImage newImage = new BufferedImage(newImageMatriksRed.getCol(), newImageMatriksRed.getRow(), BufferedImage.TYPE_INT_ARGB);

        for (int i = 0; i < newImageMatriksRed.getRow(); i++){
            for (int j = 0; j < newImageMatriksRed.getCol(); j++){
                int alpha = (int)newImageMatriksAlpha.Mat[i][j];
                int red = (int)newImageMatriksRed.Mat[i][j];
                int green = (int)newImageMatriksGreen.Mat[i][j];
                int blue = (int)newImageMatriksBlue.Mat[i][j];
                int rgb = (alpha << 24) | (red << 16) | (green << 8) | blue;
                newImage.setRGB(j, i, rgb);
            }
            setResizeProgress(getResizeProgress() + 0.2/(double)newImageMatriksRed.getRow());
        }

        setResizeProgress(1.0);


        return newImage;
    }

    private double[][][] preprocessAMatriks(Matriks imageMatriks, Matriks XD){
        double [][][] precalculatedA = new double[imageMatriks.getRow()][imageMatriks.getCol()][16];
        for (int i = 0; i < imageMatriks.getRow(); i++){
            setResizeProgress(resizeProgress + 0.1/(double)imageMatriks.getRow());
            for (int j = 0; j < imageMatriks.getCol(); j++){
                Matriks fValue = new Matriks(16, 1);
                for (int l = i; l < i + 4; l++){
                    for (int k = j; k < j + 4; k++){
                        // jika di luar gambar, ambil nilai paling ujung
                        if (j == 0 || i == 0){
                            if (j == 0){
                                if (l >= imageMatriks.getRow()){
                                    fValue.Mat[(k-j)*4 + (l-i)][0] = imageMatriks.Mat[imageMatriks.getRow()-1][0];
                                }
                                else{
                                    fValue.Mat[(k-j)*4 + (l-i)][0] = imageMatriks.Mat[l][0];
                                }
                            }
                            else{
                                if (k >= imageMatriks.getCol()){
                                    fValue.Mat[(k-j)*4 + (l-i)][0] = imageMatriks.Mat[0][imageMatriks.getCol()-1];
                                }
                                else{
                                    fValue.Mat[(k-j)*4 + (l-i)][0] = imageMatriks.Mat[0][k];
                                }
                            }
                        }
                        else if (k >= imageMatriks.getCol() && l >= imageMatriks.getRow()){
                            fValue.Mat[(k-j)*4 + (l-i)][0] = imageMatriks.Mat[imageMatriks.getRow()-1][imageMatriks.getCol()-1];
                        }
                        else if (k >= imageMatriks.getCol()){
                            fValue.Mat[(k-j)*4 + (l-i)][0] = imageMatriks.Mat[l][imageMatriks.getCol()-1];
                        }
                        else if (l >= imageMatriks.getRow()){
                            fValue.Mat[(k-j)*4 + (l-i)][0] = imageMatriks.Mat[imageMatriks.getRow()-1][k];
                        }

                        // jika di dalam gambar, ambil nilai sesuai koordinat
                        else{
                            fValue.Mat[(k-j)*4 + (l-i)][0] = imageMatriks.Mat[l][k];
                        }
                    }
                }
                Matriks a = this.findImprovedAMatrix(fValue, XD);
                for (int k = 0; k < 16; k++){
                    precalculatedA[i][j][k] = a.Mat[k][0];
                }
            }
        } 
        return precalculatedA;
        
    }

    /**
     * Mencari nilai f pada titik (x, y) dengan interpolasi bicubic spline
     * 
     * @param imageMatriks matriks gambar original
     * @param row baris ujung kiri atas dari blok 4x4
     * @param col kolom ujung kiri atas dari blok 4x4
     * @param x absis titik yang akan diinterpolasi
     * @param x ordinat titik yang akan diinterpolasi
     * @param XD matriks hasil kali invers X dan D
     * @return nilai f pada titik (x, y)
     */

    private int findFValue(Matriks imageMatriks, int row, int col, double x, double y, Matriks XD, double[][][] a){
        // hitung nilai f pada titik (x, y)
        double res = 0;
        int idx = 0;
        for (int j = 0; j < 4; j++){
            for (int i = 0; i < 4; i++){
                res += a[row][col][idx]*Math.pow(x, i)*Math.pow(y, j);
                idx++;
            }
        }
        if (res < 0) return 0;          // batas warna rgb adalah 0-255
        if (res > 255) return 255;
        return (int)res;
    }

    
    /**
     * Melakukan interpolasi bicubic spline pada gambar yang akan diresize
     * 
     * @param imageMatriks matriks gambar original
     * @param scale_x pengali lebar gambar
     * @param scale_y pengali tinggi gambar
     * 
     * @return matriks gambar hasil interpolasi
     */
    private Matriks imageInterpolate(Matriks imageMatriks, double scale_x, double scale_y){
        int height = imageMatriks.getRow();
        int width = imageMatriks.getCol();
        int newheight = (int)(height*scale_y);
        int newwidth = (int)(width*scale_x);

        double ratio_height = (double)height/newheight;
        double ratio_width = (double)width/newwidth;
        
        Matriks expandedMatriks = new Matriks(newheight, newwidth);
        
        Linalg linalg = new Linalg();
        Matriks inversX = linalg.inversMatriks(this.matriksX(), "adjoin");
        Matriks D = this.matriksD();
        
        // melakukan precomputing untuk mempercepat proses interpolasi
        Matriks XD = linalg.perkalianMatriks(inversX, D);

        // melakukan precomputing nilai a untuk setiap blok 4x4
        // complexity: O(n*m*16*16)
        // butuh waktu sekitar 25 detik untuk gambar 1000x1000
        double [][][] a = this.preprocessAMatriks(imageMatriks, XD);
        // setResizeProgress(getResizeProgress() + resizeProgressIncrement);
        
        // mencari nilai interpolasi pada setiap titik
        // complexity: O(n*m*scale_x*scale_y*16)
        // butuh waktu sekitar 6.5 detik untuk gambar 1000x1000 dengan scale_x = 2, scale_y = 2
        for (int i = 0; i < newheight; i++){
            for (int j = 0; j < newwidth; j++){
                int actual_i = (int)(i*ratio_height);
                int actual_j = (int)(j*ratio_width);
                double x = (i*ratio_height) - actual_i;
                double y = (j*ratio_width) - actual_j;
                expandedMatriks.Mat[i][j] = this.findFValue(imageMatriks, actual_i, actual_j, x, y, XD, a);
            }
            setResizeProgress(resizeProgress + 0.1/(double)newheight);
        }
        // setResizeProgress(getResizeProgress() + resizeProgressIncrement);
        return expandedMatriks;
    }

    /**
     * Mencari nilai koefisien interpolasi a yang lebih akurat dengan matriks D
     * 
     * @param fValue matriks nilai f pada blok 4x4
     * @param XD matriks hasil kali invers X dan D
     * @return nilai f pada titik (x, y)
     */
    private Matriks findImprovedAMatrix(Matriks fValue, Matriks XD){
        Linalg linalg = new Linalg();
        Matriks a = linalg.perkalianMatriks(XD, fValue);
        a = linalg.bagiXMatriks(a, 4);
        return a;
    }
    
    /**
     * Mencari nilai koefisien interpolasi a dengan matriks X
     * 
     * @param fValue matriks nilai f pada blok 4x4
     * @return koefisien interpolasi a dalam bentuk array
     */
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

    /**
     * Membuat matriks X untuk interpolasi bicubic spline
     */
    private Matriks matriksX(){
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

    /**
     * Membuat matriks D untuk interpolasi bicubic spline
     */
    private Matriks matriksD(){
        Matriks nilaiI = new Matriks(16, 2);
        int row = 0;
        for (int j = -1; j < 3; j++){
            for (int i = -1; i < 3; i++){
                nilaiI.Mat[row][0] = i;
                nilaiI.Mat[row][1] = j;
                row++;
            }
        }

        Matriks D = new Matriks(16, 16);
        row = 0;
        for (int j = 0; j < 2; j++){
            for (int i = 0; i < 2; i++){
                for (int k = 0; k < 16; k++){
                    if (nilaiI.Mat[k][0] == i && nilaiI.Mat[k][1] == j){
                        D.Mat[row][k] = 4;
                    }
                    else{
                        D.Mat[row][k] = 0;
                    }
                }
                row++;
            }
        }

        for (int j = 0; j < 2; j++){
            for (int i = 0; i < 2; i++){
                for (int k = 0; k < 16; k++){
                    if (nilaiI.Mat[k][0] == i+1 && nilaiI.Mat[k][1] == j){
                        D.Mat[row][k] = 2;
                    }
                    else if (nilaiI.Mat[k][0] == i-1 && nilaiI.Mat[k][1] == j){
                        D.Mat[row][k] = -2;
                    }
                    else{
                        D.Mat[row][k] = 0;
                    }
                }
                row++;
            }
        }

        for (int j = 0; j < 2; j++){
            for (int i = 0; i < 2; i++){
                for (int k = 0; k < 16; k++){
                    if (nilaiI.Mat[k][0] == i && nilaiI.Mat[k][1] == j+1){
                        D.Mat[row][k] = 2;
                    }
                    else if (nilaiI.Mat[k][0] == i && nilaiI.Mat[k][1] == j-1){
                        D.Mat[row][k] = -2;
                    }
                    else{
                        D.Mat[row][k] = 0;
                    }
                }
                row++;
            }
        }

        for (int j = 0; j < 2; j++){
            for (int i = 0; i < 2; i++){
                for (int k = 0; k < 16; k++){
                    if (nilaiI.Mat[k][0] == i+1 && nilaiI.Mat[k][1] == j+1){
                        D.Mat[row][k] = 1;
                    }
                    else if (nilaiI.Mat[k][0] == i-1 && nilaiI.Mat[k][1] == j){
                        D.Mat[row][k] = -1;
                    }
                    else if (nilaiI.Mat[k][0] == i && nilaiI.Mat[k][1] == j-1){
                        D.Mat[row][k] = -1;
                    }
                    else if (nilaiI.Mat[k][0] == i && nilaiI.Mat[k][1] == j){
                        D.Mat[row][k] = -1;
                    }
                    else{
                        D.Mat[row][k] = 0;
                    }
                }
                row++;
            }
        }

        return D;
    }

    /**
     * Mengembalikan nilai progress resize
     * 
     * @return nilai progress resize
     */
    public static double getResizeProgress(){
        return resizeProgress;
    }

    /**
     * Mengeset nilai progress resize
     * 
     * @param progress nilai progress resize
     */
    public static void setResizeProgress(double progress){
        resizeProgress = progress;
    }
}
