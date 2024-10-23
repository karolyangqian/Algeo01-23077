package algeo;

import javax.imageio.ImageIO;


import java.awt.image.BufferedImage;
import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.IOException;

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

    public BufferedImage resizeImage(BufferedImage image, double scale_x, double scale_y){
        int width = image.getWidth();
        int height = image.getHeight();
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

        Matriks newImageMatriksRed = this.imageInterpolate(imageMatriksRed, scale_x, scale_y);
        // System.out.println("Red");
        // newImageMatriksRed.printMatriks();
        Matriks newImageMatriksGreen = this.imageInterpolate(imageMatriksGreen, scale_x, scale_y);
        // System.out.println("Green");
        // newImageMatriksGreen.printMatriks();
        Matriks newImageMatriksBlue = this.imageInterpolate(imageMatriksBlue, scale_x, scale_y);
        // System.out.println("Blue");
        // newImageMatriksBlue.printMatriks();
        Matriks newImageMatriksAlpha = this.imageInterpolate(imageMatriksAlpha, scale_x, scale_y);
        // System.out.println("Alpha");
        // newImageMatriksAlpha.printMatriks();


        BufferedImage newImage = new BufferedImage(newImageMatriksRed.getCol(), newImageMatriksRed.getRow(), BufferedImage.TYPE_INT_ARGB);
        for (int i = 0; i < newImageMatriksRed.getRow(); i++){
            for (int j = 0; j < newImageMatriksRed.getCol(); j++){
                int alpha = (int)newImageMatriksAlpha.Mat[i][j];
                int red = (int)newImageMatriksRed.Mat[i][j];
                int green = (int)newImageMatriksGreen.Mat[i][j];
                int blue = (int)newImageMatriksBlue.Mat[i][j];
                int rgb = (alpha << 24) | (red << 16) | (green << 8) | blue;
                // System.out.println(rgb);
                newImage.setRGB(j, i, rgb);
            }
        }

        try {
            File outputFile = new File("test/output.png");
            ImageIO.write(newImage, "png", outputFile);
		}
		catch(IOException e) {
			System.out.println(e);
		}
        return newImage;
    }

    private int findFValue(Matriks imageMatriks, int row, int col, double x, double y, Matriks XD){
        Matriks fValue = new Matriks(16,1);
        int idx = 0;
        for (int j = col; j < col + 4; j++){
            for (int i = row; i < row + 4; i++){
                if (j >= imageMatriks.getCol() && i >= imageMatriks.getRow()){
                    fValue.Mat[idx][0] = imageMatriks.Mat[imageMatriks.getRow()-1][imageMatriks.getCol()-1];
                }
                else if (j >= imageMatriks.getCol()){
                    fValue.Mat[idx][0] = imageMatriks.Mat[i][imageMatriks.getCol()-1];
                }
                else if (i >= imageMatriks.getRow()){
                    fValue.Mat[idx][0] = imageMatriks.Mat[imageMatriks.getRow()-1][j];
                }
                else{
                    fValue.Mat[idx][0] = imageMatriks.Mat[i][j];
                }
                idx++;
            }
        }
        Matriks a = this.findImprovedaValue(fValue, XD);
        double res = 0;
        idx = 0;
        for (int j = 0; j < 4; j++){
            for (int i = 0; i < 4; i++){
                res += a.Mat[idx][0]*Math.pow(x, i)*Math.pow(y, j);
                idx++;
            }
        }
        if (res < 0) return 0;
        if (res > 255) return 255;
        return (int)res;
    }

    private Matriks findImprovedaValue(Matriks fValue, Matriks XD){
        Linalg linalg = new Linalg();
        Matriks a = linalg.perkalianMatriks(XD, fValue);
        a = linalg.bagiXMatriks(a, 4);
        return a;
    }

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
        Matriks XD = linalg.perkalianMatriks(inversX, D);

        for (int i = 0; i < newheight; i++){
            for (int j = 0; j < newwidth; j++){
                int actual_i = (int)(i*ratio_height);
                int actual_j = (int)(j*ratio_width);
                double x = (i*ratio_height) - actual_i;
                double y = (j*ratio_width) - actual_j;
                expandedMatriks.Mat[i][j] = this.findFValue(imageMatriks, actual_i, actual_j, x, y, XD);
            }
        }

        return expandedMatriks;
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

    public Matriks matriksD(){
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

        // D.printMatriks();

        return D;
    }
}
