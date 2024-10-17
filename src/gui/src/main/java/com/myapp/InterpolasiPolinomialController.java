package com.myapp;

import java.io.IOException;
import javafx.fxml.FXML;
import javafx.scene.chart.ScatterChart;
import javafx.scene.control.*;
import javafx.scene.text.*;
import algeo.*;
import javafx.scene.chart.NumberAxis;
import javafx.scene.chart.ScatterChart;
import javafx.scene.chart.XYChart;
public class InterpolasiPolinomialController {

    private Polinomial polinomial = new Polinomial();
    private boolean interpolated;

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
    // @FXML
    // ScatterChart<Number, Number> scatterChart = new ScatterChart<>();

    @FXML
    public void initialize() {
        interpolated = false;
    }
    
    @FXML
    private void switchToMainMenu() throws IOException {
        App.setRoot("mainMenu");
    }

    @FXML
    private void interpolasiPolinomial() {
        // ----------------- CLEAR OUTPUT SOLUSI BALIKAN DI GUI -----------------
        
        polinomTextFlow.getChildren().clear();


        // ----------------- VALIDASI INPUT -----------------

        String titikString = inputTitikList.getText().replaceAll("\n", " ");
        int row = Integer.parseInt(jumlahTitikInput.getText());
        int col = 2;
        String[] elements = titikString.split("\\s+");

        if (titikString.isBlank()){
            alertMsg.setText("*Masukkan titik terlebih dahulu");
            return;
        }

        if (elements.length != row * col){
            alertMsg.setText("*Jumlah titik tidak sesuai dengan yang diinputkan");
            return;
        }

        double[][] matriks = new double[row][col];

        for (int i = 0; i < row; i++){
            for (int j = 0; j < col; j++){
                matriks[i][j] = Double.parseDouble(elements[i * col + j]);
            }
        }

        Matriks Points = new Matriks(matriks);
        System.out.println("Titik input:");
        Points.printMatriks();
        System.out.println();
        
        alertMsg.setText("");


        // ----------------- INTERPOLASI POLINOMIAL -----------------

        polinomial.interpolate(Points);
        System.out.println("Polinomial:");
        polinomial.printCoefficients();


        // ----------------- OUTPUT SOLUSI -----------------

        Text text = new Text("Polinomial:\n");
        polinomTextFlow.getChildren().add(text);

        text = new Text("P(x) = ");
        polinomTextFlow.getChildren().add(text);

        if (polinomial.getCoefficients().length == 1) {
            text = new Text(String.format("%.2f", polinomial.getCoefficients()[0]));
            polinomTextFlow.getChildren().add(text);
            interpolated = true;
            return;
        }

        if (polinomial.getCoefficients()[0] != 0) {
            text = new Text(String.format("%.2f", polinomial.getCoefficients()[0]));
            polinomTextFlow.getChildren().add(text);
        }

        for (int i = 1; i < polinomial.getCoefficients().length; i++) {
            if (polinomial.getCoefficients()[i] == 0) {
                continue;
            } else {
                if (polinomial.getCoefficients()[i-1] != 0 || polinomial.getCoefficients()[i-1] < 0) {
                    if (polinomial.getCoefficients()[i-1] > 0) {
                        Text plusText = new Text(" + ");
                        polinomTextFlow.getChildren().add(plusText);
                    } else {
                        Text minusText = new Text(" - ");
                        polinomTextFlow.getChildren().add(minusText);
                    }
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
            }
        }
        interpolated = true;
    }

    @FXML
    private void hitungFungsi() {
        // ----------------- CLEAR OUTPUT SOLUSI BALIKAN DI GUI -----------------
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

        double x = Double.parseDouble(inputX.getText());

        // ----------------- HITUNG NILAI FUNGSI -----------------
        double result = polinomial.calculate(x);

        // ----------------- OUTPUT NILAI FUNGSI -----------------
        Text text = new Text("P(" + x + ") = " + String.format("%.2f", result));
        fungsiTextFlow.getChildren().add(text);
    }
}
