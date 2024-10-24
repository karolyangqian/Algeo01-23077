package com.myapp;

import java.io.IOException;

import algeo.*;
import javafx.fxml.FXML;
import javafx.scene.control.Button;
import javafx.scene.control.RadioButton;
import javafx.scene.control.TextArea;
import javafx.scene.control.TextField;
import javafx.scene.control.ToggleGroup;
import javafx.scene.text.Text;
import javafx.scene.text.TextFlow;
import javafx.stage.Stage;
import java.io.File;

import javafx.stage.FileChooser;
import java.util.Scanner;

public class DeterminanController {

    @FXML
    TextArea inputMatriks = new TextArea();
    @FXML
    TextField barisInput = new TextField();
    @FXML
    Text alertMsg = new Text();
    @FXML
    ToggleGroup MetodeDeterminan = new ToggleGroup();
    @FXML
    RadioButton selectEkspansiKofaktor = new RadioButton();
    @FXML
    RadioButton selectMatriksSegitiga = new RadioButton();
    @FXML
    Button determinanButton = new Button();
    @FXML
    TextFlow solutionTextFlow = new TextFlow();

    private File inputFile;
    private File outputFile;
    private Scanner scanner;
    FileChooser fileChooser = new FileChooser();
    private boolean determinanSolved;
    private String outputString;
    
    @FXML
    public void initialize() {
        selectEkspansiKofaktor.setToggleGroup(MetodeDeterminan);
        selectMatriksSegitiga.setToggleGroup(MetodeDeterminan);
        determinanSolved = false;
    }

    /**
     * Export text file pada lokasi sesuai input pengguna
     * @throws IOException
     */
    @FXML
    private void exportFile() throws IOException {
        if (!determinanSolved) {
            alertMsg.setText("*Hitung determinan terlebih dahulu");
            return;
        }

        FileHandler fh = new FileHandler();

        File initialDirectory = new File("./../../test");
        if (initialDirectory.exists()) {
            fileChooser.setInitialDirectory(initialDirectory);
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
     * Buka file matriks yang akan dicari determinannya dan memasukkan ke text field dan text area
     * @throws IOException
     */
    @FXML
    private void chooseFile() throws IOException {
        File initialDirectory = new File("./../../test");
        if (initialDirectory.exists()) {
            fileChooser.setInitialDirectory(initialDirectory);
        }
        fileChooser.setTitle("Open Text File");
        fileChooser.getExtensionFilters().add(new FileChooser.ExtensionFilter("Text Files", "*.txt"));
        
        inputFile = fileChooser.showOpenDialog(new Stage());
        
        if (inputFile == null) {
            return;
        }
        
        alertMsg.setText("");

        barisInput.clear();
        inputMatriks.clear();
        try {
            scanner = new Scanner(inputFile);
            String nString = scanner.nextLine();
            barisInput.setText(nString);
            int n = Integer.parseInt(nString);
            for (int i = 0; i < n; i++) {
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
    private void findDeterminan() {
        // ----------------- CLEAR ALERT DAN OUTPUT SOLUSI DETERMINAN DI GUI -----------------
        alertMsg.setText("");
        solutionTextFlow.getChildren().clear();

        // ----------------- VALIDASI INPUT -----------------
        if (barisInput.getText().isBlank() || inputMatriks.getText().isBlank()){
            alertMsg.setText("*Masukkan baris/kolom dan matriks yang sesuai terlebih dahulu");
            return;
        }
        int row, col;
        try {
            row = Integer.parseInt(barisInput.getText());
            col = row;
        } catch (Exception e) {
            alertMsg.setText("*Masukkan baris dan kolom yang valid");
            return;
        }

        String matriksString = inputMatriks.getText().replaceAll("\n", " ");
        String[] elements = matriksString.split("\\s+");

        if (elements.length != row * col){
            alertMsg.setText("*Jumlah elemen matriks tidak sesuai dengan input baris dan kolom");
            return;
        }

        double[][] matriks = new double[row][col];

        try {
            for (int i = 0; i < row; i++){
                for (int j = 0; j < col; j++){
                    matriks[i][j] = Double.parseDouble(elements[i * col + j]);
                }
            }
        } catch (Exception e) {
            alertMsg.setText("*Masukkan elemen matriks yang valid");
            return;
        }

        Matriks Mat = new Matriks(matriks);
        System.out.println("Matriks input:");
        Mat.printMatriks();
        System.out.println();
        

        // ----------------- HITUNG DETERMINAN -----------------
        Linalg linalg = new Linalg();
        double det;

        if (selectEkspansiKofaktor.isSelected()){
            try {
                det = linalg.determinanMatriks(Mat, "kofaktor");
            } catch (Exception e) {
                alertMsg.setText("*Determinan matriks tidak dapat dihitung dengan metode ekspansi kofaktor");
                return;
            }

        } else if (selectMatriksSegitiga.isSelected()){
            try {
                det = linalg.determinanMatriks(Mat, "reduksi");
            } catch (Exception e) {
                alertMsg.setText("*Determinan matriks tidak dapat dihitung dengan metode matriks segitiga");
                return;
            }

        } else {
            alertMsg.setText("*Pilih metode perhitungan determinan matriks");
            return;
        }

        // ----------------- OUTPUT DETERMINAN -----------------
        if (Math.abs(det) < 0.0000001){
            det = 0;
        }

        Text text = new Text("Determinan matriks: " + String.format("%.2f", det));
        solutionTextFlow.getChildren().add(text);
        
        System.out.println("Determinan matriks: " + det);

        TextFlowHandler tfh = new TextFlowHandler();
        outputString = tfh.textFlowToString(solutionTextFlow);
        determinanSolved = true;
    }

    
}
