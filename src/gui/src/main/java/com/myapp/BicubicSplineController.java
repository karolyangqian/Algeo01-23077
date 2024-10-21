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

public class BicubicSplineController {

    @FXML
    TextArea inputKonfigurasi = new TextArea();
    @FXML
    TextField inputXBebas = new TextField();
    @FXML
    TextField inputYBebas = new TextField();
    @FXML
    Button interpolasiBSButton = new Button();
    @FXML
    Text alertMsg = new Text();
    @FXML
    TextFlow bicubicSplineTextFlow = new TextFlow();

    private File inputFile;
    private Scanner scanner;
    FileChooser fileChooser = new FileChooser();

    @FXML
    public void initialize() {
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
     * Buka file matriks konfigurasi dan input x y bicubic spline dan memasukkan ke text field dan text area
     * @throws IOException
     */
    @FXML
    private void chooseFile() throws IOException {
        inputFile = fileChooser.showOpenDialog(new Stage());

        if (inputFile == null) {
            return;
        }
        
        alertMsg.setText("");

        inputKonfigurasi.clear();
        inputXBebas.clear();
        inputYBebas.clear();
        try {
            scanner = new Scanner(inputFile);
            for (int i = 0; i < 4; i++) {
                inputKonfigurasi.appendText(scanner.nextLine() + "\n");
            }
            String[] xy = scanner.nextLine().split("\\s+");
            inputXBebas.setText(xy[0]);
            inputYBebas.setText(xy[1]);
            scanner.close();
        } catch (Exception e) {
            System.out.println("Error: " + e);
            alertMsg.setText("*File input tidak valid");
        }
    }

    /**
     * Interpolasi bicubic spline
     */
    @FXML
    private void interpolasiBicubicSpline() {
        // ----------------- CLEAR OUTPUT DI GUI -----------------
        bicubicSplineTextFlow.getChildren().clear();

        // ----------------- VALIDASI INPUT -----------------
        String konfigurasiString = inputKonfigurasi.getText().replaceAll("\n", " ");
        String[] elements = konfigurasiString.split("\\s+");
        int n = elements.length;

        if (n != 16){
            alertMsg.setText("*Jumlah elemen konfigurasi harus 16");
            return;
        }

        if (konfigurasiString.isBlank()){
            alertMsg.setText("*Masukkan konfigurasi terlebih dahulu");
            return;
        }

        if (inputXBebas.getText().isBlank() || inputYBebas.getText().isBlank()){
            alertMsg.setText("*Masukkan nilai x dan y terlebih dahulu");
            return;
        }

        double[][] konfigurasi = new double[4][4];
        for (int i = 0; i < 4; i++){
            for (int j = 0; j < 4; j++){
                konfigurasi[i][j] = Double.parseDouble(elements[i * 4 + j]);
            }
        }

        Matriks fValue = new Matriks(konfigurasi);
        double x = Double.parseDouble(inputXBebas.getText());
        double y = Double.parseDouble(inputYBebas.getText());

        // ----------------------- INTERPOLATE -----------------------

        BicubicSplineInterpolation BS = new BicubicSplineInterpolation();

        double result = BS.BicubicSplineInterpolate(fValue, x, y);

        // ----------------------- OUTPUT GUI -----------------------

        Text resultText = new Text(String.format("f(%.6f, %.6f) = %.6f", x, y, result));
        bicubicSplineTextFlow.getChildren().add(resultText);

    }
}
