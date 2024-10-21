package com.myapp;

import algeo.*;
import java.io.IOException;
import javafx.fxml.FXML;
import javafx.scene.control.*;
import javafx.scene.text.*;
import javafx.stage.Stage;
import java.util.Scanner;
import java.io.File;
import javafx.stage.FileChooser;

public class InterpolasiPolinomialController {

    
    @FXML
    TextField jumlahTitikInput = new TextField();
    @FXML
    TextArea inputTitikList = new TextArea();
    @FXML
    Text alertMsg = new Text();
    @FXML
    Button interpolasiButton = new Button();
    @FXML
    TextFlow polinomTextFlow = new TextFlow();
    @FXML
    TextField inputX = new TextField();
    @FXML
    Button hitungFungsiButton = new Button();
    @FXML
    TextFlow fungsiTextFlow = new TextFlow();
    
    private Polinomial polinomial = new Polinomial();
    private boolean interpolated;
    private boolean calculated;
    private double xmin;
    private double xmax;
    private File inputFile;
    private File outputFile;
    private Scanner scanner;
    FileChooser fileChooser = new FileChooser();
    private String outputPolinomString;
    private String outputFungsiString;

    @FXML
    public void initialize() {
        interpolated = false;
        calculated = false;
    }

    /**
    * Export text file pada lokasi sesuai input pengguna
    * @throws IOException
    */
    @FXML
    private void exportFile() throws IOException {
        FileHandler fh = new FileHandler();
        if (!(interpolated && calculated)) {
            alertMsg.setText("*Lakukan interpolasi dan hitung nilai terikatnya terlebih dahulu");
            return;
        }

        fileChooser.setTitle("Save Text File");
        fileChooser.getExtensionFilters().add(new FileChooser.ExtensionFilter("Text Files", "*.txt"));

        outputFile = fileChooser.showSaveDialog(new Stage());

        if (outputFile == null) {
            return;
        }

        alertMsg.setText("");

        String out = String.join("", outputPolinomString, "\n\n", outputFungsiString);

        fh.saveTextToFile(out, outputFile);
    }

    /**
     * Buka file input interpolasi dan memasukkan ke text field dan text area
     * @throws IOException
     */
    @FXML
    private void chooseFile() throws IOException {
        inputFile = fileChooser.showOpenDialog(new Stage());
        
        if (inputFile == null) {
            return;
        }
        
        alertMsg.setText("");
        
        jumlahTitikInput.clear();
        inputTitikList.clear();
        try {
            scanner = new Scanner(inputFile);
            String nString = scanner.nextLine();
            jumlahTitikInput.setText(nString);
            int n = Integer.parseInt(nString);
            for (int i = 0; i < n; i++) {
                inputTitikList.appendText(scanner.nextLine() + "\n");
            }
            String x = scanner.nextLine();
            inputX.setText(x);
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
     * Interpolasi polinomial
     */
    @FXML
    private void interpolasiPolinomial() {
        // ----------------- CLEAR ALERT DAN OUTPUT SOLUSI BALIKAN DI GUI -----------------
        alertMsg.setText("");
        polinomTextFlow.getChildren().clear();


        // ----------------- VALIDASI INPUT -----------------

        if (jumlahTitikInput.getText().isBlank() || inputTitikList.getText().isBlank()){
            alertMsg.setText("*Masukkan jumlah titik dan sampel titik yang sesuai terlebih dahulu");
            return;
        }

        String titikString = inputTitikList.getText().replaceAll("\n", " ");
        int row = Integer.parseInt(jumlahTitikInput.getText());
        int col = 2;
        String[] elements = titikString.split("\\s+");

        if (elements.length != row * col){
            alertMsg.setText("*Jumlah titik tidak sesuai dengan yang diinputkan");
            return;
        }

        double[][] matriks = new double[row][col];
        xmin = Double.parseDouble(elements[0]);
        xmax = Double.parseDouble(elements[0]);

        for (int i = 0; i < row; i++){
            for (int j = 0; j < col; j++){
                matriks[i][j] = Double.parseDouble(elements[i * col + j]);
                if (j == 0) {
                    if (matriks[i][j] < xmin) {
                        xmin = matriks[i][j];
                    }
                    if (matriks[i][j] > xmax) {
                        xmax = matriks[i][j];
                    }
                }
            }
        }

        Matriks Points = new Matriks(matriks);
        System.out.println("Titik input:");
        Points.printMatriks();
        System.out.println();
        


        // ----------------- INTERPOLASI POLINOMIAL -----------------
        try {
            polinomial.interpolate(Points);
        } catch (Exception e) {
            System.out.println("Error: " + e);
            alertMsg.setText("*Polinomial tidak dapat diinterpolasi");
            return;
        }
        
        System.out.println("Polinomial:");
        polinomial.printCoefficients();


        // ----------------- OUTPUT SOLUSI -----------------

        Text text = new Text("Polinomial:\n");
        polinomTextFlow.getChildren().add(text);

        text = new Text("P(x) = ");
        polinomTextFlow.getChildren().add(text);

        boolean printed = false;

        if (polinomial.getCoefficients().length == 1) {
            text = new Text(String.format("%.2f", polinomial.getCoefficients()[0]));
            polinomTextFlow.getChildren().add(text);
            interpolated = true;
            return;
        }

        if (polinomial.getCoefficients()[0] != 0) {
            text = new Text(String.format("%.2f", polinomial.getCoefficients()[0]));
            polinomTextFlow.getChildren().add(text);
            printed = true;
        }

        for (int i = 1; i < polinomial.getCoefficients().length; i++) {
            if (polinomial.getCoefficients()[i] == 0) {
                continue;
            } 

            if (polinomial.getCoefficients()[i] > 0 && printed) {
                Text plusText = new Text(" + ");
                polinomTextFlow.getChildren().add(plusText);
            } else if (polinomial.getCoefficients()[i] < 0) {
                Text minusText = new Text(" - ");
                polinomTextFlow.getChildren().add(minusText);
            }
        

            if (Math.abs(polinomial.getCoefficients()[i]) != 1) {
                text = new Text(String.format("%.2f", Math.abs(polinomial.getCoefficients()[i])));
                polinomTextFlow.getChildren().add(text);
            }

            text = new Text("x");
            text.setStyle("-fx-font-size: 10pt;");
            polinomTextFlow.getChildren().add(text);

            // Supercript
            Text baseText = new Text(Integer.toString(i));
            baseText.setStyle("-fx-font-size: 8pt;");
            baseText.setTranslateY(-5);
            polinomTextFlow.getChildren().add(baseText);
            printed = true;
        }

        TextFlowHandler tfh = new TextFlowHandler();

        outputPolinomString = tfh.textFlowToString(polinomTextFlow);

        interpolated = true;
        calculated = false;
    }

    /**
     * Hitung nilai fungsi polinomial pada x
     */
    @FXML
    private void hitungFungsi() {
        // ----------------- CLEAR ALERT DAN OUTPUT SOLUSI BALIKAN DI GUI -----------------
        alertMsg.setText("");
        fungsiTextFlow.getChildren().clear();

        // ----------------- VALIDASI INPUT -----------------

        if (!interpolated) {
            alertMsg.setText("*Lakukan interpolasi terlebih dahulu");
            return;
        }

        if (inputX.getText().isBlank()){
            alertMsg.setText("*Masukkan nilai x terlebih dahulu");
            return;
        }

        if (Double.parseDouble(inputX.getText()) < xmin || Double.parseDouble(inputX.getText()) > xmax) {
            alertMsg.setText(String.format("*Nilai x diluar range titik interpolasi [%.2f, %.2f]", xmin, xmax));
            return;
        }

        double x = Double.parseDouble(inputX.getText());

        // ----------------- HITUNG NILAI FUNGSI -----------------
        double result = polinomial.calculate(x);

        // ----------------- OUTPUT NILAI FUNGSI -----------------
        Text text = new Text("P(" + x + ") = " + String.format("%.4f", result));
        fungsiTextFlow.getChildren().add(text);

        TextFlowHandler tfh = new TextFlowHandler();
        outputFungsiString = tfh.textFlowToString(fungsiTextFlow);

        calculated = true;
    }
}
