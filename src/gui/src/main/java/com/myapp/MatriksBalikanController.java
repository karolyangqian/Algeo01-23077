package com.myapp;

import algeo.*;
import java.io.IOException;
import javafx.fxml.FXML;
import javafx.scene.control.*;
import javafx.scene.text.*;
import javafx.stage.Stage;
import java.io.File;
import javafx.stage.FileChooser;
import java.util.Scanner;

public class MatriksBalikanController {

    @FXML
    TextField barisInput = new TextField();
    @FXML
    TextArea inputMatriks = new TextArea();
    @FXML
    Button balikanButton = new Button();
    @FXML
    ToggleGroup MetodeBalikan = new ToggleGroup();
    @FXML
    RadioButton selectOBE = new RadioButton();
    @FXML
    RadioButton selectMatriksAdjoin = new RadioButton();
    @FXML
    TextFlow solutionTextFlow = new TextFlow();
    @FXML
    Text alertMsg = new Text();

    private File inputFile;
    private File outputFile;
    private Scanner scanner;
    FileChooser fileChooser = new FileChooser();
    private boolean balikanSolved;
    private String outputString;

    @FXML
    public void initialize() {
        selectOBE.setToggleGroup(MetodeBalikan);
        selectMatriksAdjoin.setToggleGroup(MetodeBalikan);
        balikanSolved = false;
    }
    
    /**
    * Export text file pada lokasi sesuai input pengguna
    * @throws IOException
    */
    @FXML
    private void exportFile() throws IOException {
        FileHandler fh = new FileHandler();
        if (!balikanSolved) {
            alertMsg.setText("*Hitung balikan terlebih dahulu");
            return;
        }

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
     * Buka file matriks yang akan dicari balikannya dan memasukkan ke text field dan text area
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
            for (int i = 0; i < n; i++){
                inputMatriks.appendText(scanner.nextLine() + "\n");
            }
            scanner.close();
        } catch (Exception e) {
            System.out.println("Error: " + e);
            alertMsg.setText("*File input tidak valid");
        }
    }
    
    @FXML
    private void switchToMainMenu() throws IOException {
        App.setRoot("mainMenu");
    }
    
    /**
     * Mencari balikan matriks dengan metode yang dipilih pada GUI
     */
    @FXML
    private void findBalikan() {
        // ----------------- CLEAR ALERT DAN OUTPUT SOLUSI BALIKAN DI GUI -----------------
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
        
        
        // ----------------- HITUNG BALIKAN -----------------
        Linalg linalg = new Linalg();
        Matriks balikan = new Matriks();

        if (selectOBE.isSelected()){
            try {
                balikan = linalg.inversMatriks(Mat, "obe");
            } catch (Exception e) {
                alertMsg.setText("*Matriks tidak memiliki balikan");
                return;
            }
        } else if (selectMatriksAdjoin.isSelected()){
            try {
                balikan = linalg.inversMatriks(Mat, "adjoin");
            } catch (Exception e) {
                alertMsg.setText("*Matriks tidak memiliki balikan");
                return;
            }
        } else {
            alertMsg.setText("*Pilih metode balikan matriks");
            return;
        }

        // ----------------- OUTPUT SOLUSI BALIKAN DI GUI -----------------
        
        if (balikan == null){
            Text noSolutionText = new Text("Matriks tidak memiliki balikan\n");
            solutionTextFlow.getChildren().add(noSolutionText);
            return;
        }

        balikan.makePositiveZero();
        Text solutionText = new Text("Balikan matriks:\n");
        solutionTextFlow.getChildren().add(solutionText);
        for (int i = 0; i < balikan.getRow(); i++){
            for (int j = 0; j < balikan.getCol(); j++){
                Text text = new Text(String.format("%.2f", balikan.Mat[i][j]) + " ");
                solutionTextFlow.getChildren().add(text);
            }
            Text newline = new Text("\n");
            solutionTextFlow.getChildren().add(newline);
        }

        TextFlowHandler tfh = new TextFlowHandler();

        outputString = tfh.textFlowToString(solutionTextFlow);
        System.out.println("Balikan matriks:");
        balikan.printMatriks();

        balikanSolved = true;
    }
}
