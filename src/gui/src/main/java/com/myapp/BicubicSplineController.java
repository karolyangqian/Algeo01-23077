package com.myapp;

import algeo.*;
import java.io.IOException;
import javafx.fxml.FXML;
import javafx.scene.control.*;
import javafx.scene.text.*;

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
