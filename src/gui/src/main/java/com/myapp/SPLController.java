package com.myapp;

import algeo.*;
import java.io.IOException;
import javafx.fxml.FXML;
import javafx.scene.control.RadioButton;
import javafx.scene.control.TextArea;
import javafx.scene.control.TextField;
import javafx.scene.control.ToggleGroup;
import javafx.scene.text.TextFlow;
import javafx.scene.text.Text;
import java.io.File;
import javafx.stage.FileChooser;
import javafx.stage.Stage;
import java.util.Scanner;

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
    
    private File inputFile;
    private File outputFile;
    private Scanner scanner;
    FileChooser fileChooser = new FileChooser();
    private String outputString;
    private boolean splSolved;
    
    @FXML
    public void initialize() {
        selectEliminasiGauss.setToggleGroup(MetodeSPL);
        selectEliminasiGaussJordan.setToggleGroup(MetodeSPL);
        selectMatriksBalikan.setToggleGroup(MetodeSPL);
        selectKaidahCramer.setToggleGroup(MetodeSPL);
        splSolved = false;
    }

    /**
    * Export text file pada lokasi sesuai input pengguna
    * @throws IOException
    */
    @FXML
    private void exportFile() throws IOException {
        FileHandler fh = new FileHandler();
        if (!splSolved) {
            alertMsg.setText("*Hitung solusi SPL terlebih dahulu");
            return;
        }

        fileChooser.setTitle("Save Text File");
        fileChooser.getExtensionFilters().add(new FileChooser.ExtensionFilter("Text Files", "*.txt"));

        outputFile = fileChooser.showSaveDialog(new Stage());

        if (outputFile == null) {
            return;
        }

        alertMsg.setText("");

        fh.saveTextToFile(outputString, outputFile);
    }

    /**
     * Buka file matriks SPL dan memasukkan ke text field dan text area
     * @throws IOException
     */
    @FXML
    private void chooseFile() throws IOException {
        inputFile = fileChooser.showOpenDialog(new Stage());
        
        if (inputFile == null) {
            return;
        }
        
        alertMsg.setText("");

        kolomInput.clear();
        barisInput.clear();
        inputMatriks.clear();
        try {
            scanner = new Scanner(inputFile);
            String mn = scanner.nextLine();
            String[] mnSplit = mn.split("\\s+");
            barisInput.setText(mnSplit[0]);
            kolomInput.setText(mnSplit[1]);
            int m = Integer.parseInt(mnSplit[0]);
            for (int i = 0; i < m; i++) {
                inputMatriks.appendText(scanner.nextLine() + "\n");
            }
            scanner.close();
        } catch (Exception e) {
            System.out.println("Error: " + e);
            alertMsg.setText("*File input tidak valid");
        }
    }
    
    /**
     * Kembali ke main menu
     * @throws IOException
     */
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
        // ----------------- CLEAR ALERT DAN OUTPUT SOLUSI SPL DI GUI -----------------
        alertMsg.setText("");
        solutionTextFlow.getChildren().clear();

        // ----------------- INPUT MATRIKS SPL -----------------
        SistemPersamaanLinier SPL = new SistemPersamaanLinier();
        
        if (barisInput.getText().isBlank() || kolomInput.getText().isBlank() || inputMatriks.getText().isBlank()){
            alertMsg.setText("*Masukkan baris, kolom, dan matriks yang sesuai terlebih dahulu");
            return;
        }


        String matriksString = inputMatriks.getText().replaceAll("\n", " ");
        int row = Integer.parseInt(barisInput.getText());
        int col = Integer.parseInt(kolomInput.getText());
        String[] elements = matriksString.split("\\s+");


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
        
        
        // ----------------- SOLVE SPL -----------------
        Matriks solution = new Matriks();

        if (selectEliminasiGauss.isSelected()){
            System.out.println("Eliminasi Gauss selected");
            try {
                solution = new Matriks(SPL.metodeGauss(Mat));
            } catch (Exception e) {
                alertMsg.setText("*Tidak ditemukan solusi menggunakan metode eliminasi Gauss");
                System.out.println("Tidak ditemukan solusi menggunakan metode eliminasi Gauss");
                return;
            }
        } else if (selectEliminasiGaussJordan.isSelected()){
            System.out.println("Eliminasi Gauss-Jordan selected");
            try {
                solution = new Matriks(SPL.metodeGaussJordan(Mat));
            } catch (Exception e) {
                alertMsg.setText("*Tidak ditemukan solusi menggunakan metode eliminasi Gauss-Jordan");
                System.out.println("Tidak ditemukan solusi menggunakan metode eliminasi Gauss-Jordan");
                return;
            }
        } else if (selectMatriksBalikan.isSelected()){
            System.out.println("Matriks balikan selected");
            try {
                solution = new Matriks(SPL.metodeInversMatriks(Mat));
            } catch (Exception e) {
                alertMsg.setText("*Tidak ditemukan solusi menggunakan metode invers");
                System.out.println("Tidak ditemukan solusi menggunakan metode invers");
                return;
            }
        } else if (selectKaidahCramer.isSelected()){
            System.out.println("Cramer selected");
            try {
                solution = new Matriks(SPL.metodeCramer(Mat));
            } catch (Exception e) {
                alertMsg.setText("*Tidak ditemukan solusi menggunakan metode Cramer");
                System.out.println("Tidak ditemukan solusi menggunakan metode Cramer");
                return;
            }
        } else {
            alertMsg.setText("*Pilih metode terlebih dahulu");
            System.out.println("No method selected");
            return;
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
                
                boolean printed = false;
                // Nilai solusi
                if (solution.Mat[i][0] != 0) {
                    if (solution.Mat[i][0] < 0) {
                        Text minusText = new Text("- ");
                        solutionTextFlow.getChildren().add(minusText);
                    }
                    Text valueText = new Text(String.format("%.2f", Math.abs(solution.Mat[i][0])));
                    solutionTextFlow.getChildren().add(valueText); 
                    printed = true;
                } else if (solution.getCol() == 1 || checkZeroRow(solution, i)) {
                    Text zeroText = new Text("0\n");
                    solutionTextFlow.getChildren().add(zeroText);
                    continue;
                }


                // Koefisien variabel lain
                for (int j = 1; j < solution.getCol(); j++) {

                    if (solution.Mat[i][j] == 0) {
                        continue;
                    }

                    if (solution.Mat[i][j] > 0 && printed) {
                        Text plusText = new Text(" + ");
                        solutionTextFlow.getChildren().add(plusText);
                    } else if (solution.Mat[i][j] < 0) {
                        Text minusText = new Text(" - ");
                        solutionTextFlow.getChildren().add(minusText);
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
                    printed = true;
                }
                Text newLineText = new Text("\n");
                solutionTextFlow.getChildren().add(newLineText);
            }
        }

        TextFlowHandler tfh = new TextFlowHandler();
        outputString = tfh.textFlowToString(solutionTextFlow);

        splSolved = true;
    }

    

    /**
     * Menghapus kolom-kolom yang berisi nol dimulai dari kolom startCol
     * @param M
     * @param startCol
     * @return Matriks baru yang telah dihapus kolom-kolom yang berisi nol
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

    /**
     * Mengecek apakah kolom matriks M semuanya bernilai 0
     * @param M
     * @param row
     * @return true jika semua elemen kolom col bernilai 0
     */
    private boolean checkZeroCol(Matriks M, int col){
        for (int i = 0; i < M.getRow(); i++){
            if (M.Mat[i][col] != 0){
                return false;
            }
        }
        return true;
    }

    /**
     * Mengecek apakah baris matriks M semuanya bernilai 0
     * @param M
     * @param row
     * @return true jika semua elemen baris row bernilai 0
     */
    private boolean checkZeroRow(Matriks M, int row){
        for (int i = 0; i < M.getCol(); i++){
            if (M.Mat[row][i] != 0){
                return false;
            }
        }
        return true;
    }        
}