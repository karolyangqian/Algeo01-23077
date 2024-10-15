package com.myapp;

import java.io.IOException;
import javafx.fxml.FXML;
import javafx.scene.control.RadioButton;
import javafx.scene.control.TextArea;
import javafx.scene.control.TextField;
import javafx.scene.control.ToggleGroup;
import javafx.scene.text.TextFlow;
import javafx.scene.text.Text;
import algeo.*;

public class SPLController {
    
    @FXML
    TextArea inputMatriks = new TextArea();
    @FXML
    ToggleGroup MetodeSPL = new ToggleGroup();
    @FXML
    RadioButton selectEliminasiGauss = new RadioButton();
    @FXML
    RadioButton selectEliminasiGaussJordan = new RadioButton();
    @FXML
    RadioButton selectMatriksBalikan = new RadioButton();
    @FXML
    RadioButton selectKaidahCramer = new RadioButton();
    @FXML
    Text alertMsg = new Text();
    @FXML
    TextFlow solutionTextFlow = new TextFlow();
    @FXML
    TextField barisInput = new TextField();
    @FXML
    TextField kolomInput = new TextField();
    
    @FXML
    public void initialize() {
        selectEliminasiGauss.setToggleGroup(MetodeSPL);
        selectEliminasiGaussJordan.setToggleGroup(MetodeSPL);
        selectMatriksBalikan.setToggleGroup(MetodeSPL);
        selectKaidahCramer.setToggleGroup(MetodeSPL);
    }
    
    @FXML
    private void switchToMainMenu() throws IOException {
        App.setRoot("mainMenu");
    }

    /**
     * Menyelesaikan SPL dengan metode yang dipilih pada GUI
     * @throws IOException
     */
    @FXML
    private void solveSPL() throws IOException {
        // ----------------- CLEAR OUTPUT SOLUSI SPL DI GUI -----------------
        solutionTextFlow.getChildren().clear();

        // ----------------- INPUT MATRIKS SPL -----------------
        SistemPersamaanLinier SPL = new SistemPersamaanLinier();
        String matriksString = inputMatriks.getText().replaceAll("\n", " ");
        int row = Integer.parseInt(barisInput.getText());
        int col = Integer.parseInt(kolomInput.getText());
        String[] elements = matriksString.split(" ");

        if (elements.length != row * col){
            alertMsg.setText("*Jumlah elemen matriks tidak sesuai dengan input baris dan kolom");
            return;
        }

        double[][] matriks = new double[row][col];

        for (int i = 0; i < row; i++){
            for (int j = 0; j < col; j++){
                matriks[i][j] = Double.parseDouble(elements[i * col + j]);
            }
        }

        Matriks Mat = new Matriks(matriks);
        System.out.println("Matriks input:");
        Mat.printMatriks();
        System.out.println();
        
        alertMsg.setText("");
        
        // ----------------- SOLVE SPL -----------------
        Matriks solution = new Matriks();

        if (selectEliminasiGauss.isSelected()){
            System.out.println("Eliminasi Gauss selected");
            try {
                solution = new Matriks(SPL.metodeGauss(Mat));
            } catch (Exception e) {
                alertMsg.setText("*Tidak ditemukan solusi menggunakan metode eliminasi Gauss");
                System.out.println("Tidak ditemukan solusi menggunakan metode eliminasi Gauss");
            }
        } else if (selectEliminasiGaussJordan.isSelected()){
            System.out.println("Eliminasi Gauss-Jordan selected");
            try {
                solution = new Matriks(SPL.metodeGaussJordan(Mat));
            } catch (Exception e) {
                alertMsg.setText("*Tidak ditemukan solusi menggunakan metode eliminasi Gauss-Jordan");
                System.out.println("Tidak ditemukan solusi menggunakan metode eliminasi Gauss-Jordan");
            }
        } else if (selectMatriksBalikan.isSelected()){
            System.out.println("Matriks balikan selected");
            try {
                solution = new Matriks(SPL.metodeInversMatriks(Mat));
            } catch (Exception e) {
                alertMsg.setText("*Tidak ditemukan solusi menggunakan metode invers");
                System.out.println("Tidak ditemukan solusi menggunakan metode invers");
            }
        } else if (selectKaidahCramer.isSelected()){
            System.out.println("Cramer selected");
            try {
                solution = new Matriks(SPL.metodeCramer(Mat));
            } catch (Exception e) {
                alertMsg.setText("*Tidak ditemukan solusi menggunakan metode Cramer");
                System.out.println("Tidak ditemukan solusi menggunakan metode Cramer");
            }
        } else {
            alertMsg.setText("*Pilih metode terlebih dahulu");
            System.out.println("No method selected");
        }

        // ----------------- OUTPUT SOLUSI SPL DI GUI -----------------
        if (solution != null){
            solution = new Matriks(removeZeroCols(solution, 1));
            solution.printMatriks();
            for (int i = 0; i < solution.getRow(); i++) {
                // Variabel x1, x2, x3, ...
                Text baseText = new Text("x");
                solutionTextFlow.getChildren().add(baseText);

                // Subscript
                Text subscriptText = new Text(String.valueOf(i + 1));
                subscriptText.setStyle("-fx-font-size: 8;"); 
                subscriptText.setTranslateY(5);
                solutionTextFlow.getChildren().add(subscriptText);

                // Tanda sama dengan
                Text equalText = new Text(" = ");
                solutionTextFlow.getChildren().add(equalText);

                // Nilai solusi
                if (solution.Mat[i][0] != 0) {
                    if (solution.Mat[i][0] < 0) {
                        Text minusText = new Text("- ");
                        solutionTextFlow.getChildren().add(minusText);
                    }
                    Text valueText = new Text(String.format("%.2f", Math.abs(solution.Mat[i][0])));
                    solutionTextFlow.getChildren().add(valueText); 
                } else if (solution.getCol() == 1) {
                    Text zeroText = new Text("0");
                    solutionTextFlow.getChildren().add(zeroText);
                }

                if (solution.getCol() == 1) {
                    Text newLineText = new Text("\n");
                    solutionTextFlow.getChildren().add(newLineText);
                    continue;
                }

                // Koefisien variabel lain
                for (int j = 1; j < solution.getCol(); j++) {

                    if (solution.Mat[i][j] == 0) {
                        continue;
                    }

                    if (solution.Mat[i][j-1] != 0 || solution.Mat[i][j] < 0) {
                        if (solution.Mat[i][j] > 0) {
                            Text plusText = new Text(" + ");
                            solutionTextFlow.getChildren().add(plusText);
                        } else {
                            Text minusText = new Text(" - ");
                            solutionTextFlow.getChildren().add(minusText);
                        }
                    }

                    // Koefisien variabel
                    if (Math.abs(solution.Mat[i][j]) != 1) {
                        Text coefText = new Text(String.format("%.2f", Math.abs(solution.Mat[i][j])));
                        solutionTextFlow.getChildren().add(coefText);
                    }

                    // Variabel k1, k2, k3, ...
                    Text baseText2 = new Text("k");
                    solutionTextFlow.getChildren().add(baseText2);

                    subscriptText = new Text(String.valueOf(j));
                    subscriptText.setStyle("-fx-font-size: 8;"); 
                    subscriptText.setTranslateY(5);
                    solutionTextFlow.getChildren().add(subscriptText);
                }
                Text newLineText = new Text("\n");
                solutionTextFlow.getChildren().add(newLineText);
            }
        }
    }

    /**
     * Menghapus kolom-kolom yang berisi nol dimulai dari kolom startCol
     * @param M
     * @param startCol
     * @return
     */
    private Matriks removeZeroCols(Matriks M, int startCol){
        int numZeroCols = 0;
        for (int i = startCol; i < M.getCol(); i++){
            if (checkZeroCol(M, i)){
                numZeroCols++;
            }
        }
        Matriks M2 = new Matriks(M.getRow(), M.getCol() - numZeroCols);
        int colIdx = 0;
        for (int i = 0; i < M.getCol(); i++){
            if (!checkZeroCol(M, i) || i < startCol){
                for (int j = 0; j < M.getRow(); j++){
                    M2.Mat[j][colIdx] = M.Mat[j][i];
                }
                colIdx++;
            }
        }
        return M2;
    }

    private boolean checkZeroCol(Matriks M, int col){
        for (int i = 0; i < M.getRow(); i++){
            if (M.Mat[i][col] != 0){
                return false;
            }
        }
        return true;
    }
        
}
