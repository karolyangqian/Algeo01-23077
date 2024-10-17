package com.myapp;

import algeo.*;
import java.io.IOException;
import javafx.fxml.FXML;
import javafx.scene.control.*;
import javafx.scene.shape.Line;
import javafx.scene.text.*;

public class RegresiBergandaController {

    enum RegresiType {
        LINIER, KUADRATIK
    }

    private LinearRegression regressor = new LinearRegression();
    private Matriks b;
    private boolean regressed;
    private int nPeubah;
    private int mSampel;
    private RegresiType regresiType;

    @FXML
    TextField jumlahTitikInput = new TextField();
    @FXML
    TextField jumlahSampelInput = new TextField();
    @FXML
    TextArea inputX = new TextArea();
    @FXML
    TextArea inputY = new TextArea();
    @FXML
    Text alertMsgRegresi = new Text();
    @FXML
    Button regresiButton = new Button();
    @FXML
    RadioButton selectLinier = new RadioButton();
    @FXML
    RadioButton selectKuadratik = new RadioButton();
    @FXML
    ToggleGroup jenisRegresi = new ToggleGroup();
    @FXML
    TextArea inputVariabelBebas = new TextArea();
    @FXML
    Text alertMsgTaksiran = new Text();
    @FXML
    Button taksirButton = new Button();
    @FXML
    TextFlow regresiTextFlow = new TextFlow();
    @FXML
    TextFlow taksiranTextFlow = new TextFlow();

    public void initialize() {
        selectLinier.setToggleGroup(jenisRegresi);
        selectKuadratik.setToggleGroup(jenisRegresi);
        regressed = false;
    }
    
    @FXML
    private void switchToMainMenu() throws IOException {
        App.setRoot("mainMenu");
    }

    @FXML
    private void regresi() {
        // ----------------- CLEAR OUTPUT SOLUSI REGRESI DI GUI -----------------
        regresiTextFlow.getChildren().clear();
        taksiranTextFlow.getChildren().clear();

        // ----------------- VALIDASI INPUT -----------------
        nPeubah = Integer.parseInt(jumlahTitikInput.getText());
        mSampel = Integer.parseInt(jumlahSampelInput.getText());
        if (nPeubah < 1 || mSampel < 1){
            alertMsgRegresi.setText("*Jumlah titik dan sampel harus lebih dari 0");
            return;
        }

        String xString = inputX.getText().replaceAll("\n", " ");
        String yString = inputY.getText().replaceAll("\n", " ");
        String[] xElements = xString.split("\\s+");
        String[] yElements = yString.split("\\s+");
        for (int i = 0; i < yElements.length; i++){
            System.out.println(yElements[i]);
        }
        System.out.println(xElements.length);
        System.out.println(yElements.length);
        if (xElements.length != nPeubah * mSampel || yElements.length != mSampel){
            alertMsgRegresi.setText("*Jumlah X dan Y tidak sesuai dengan input jumlah peubah dan sampel");
            return;
        }

        double[][] x = new double[mSampel][nPeubah];
        double[][] y = new double[mSampel][1];

        for (int i = 0; i < mSampel; i++){
            for (int j = 0; j < nPeubah; j++){
                x[i][j] = Double.parseDouble(xElements[i * nPeubah + j]);
            }
            y[i][0] = Double.parseDouble(yElements[i]);
        }

        Matriks inputSampelX = new Matriks(x);
        Matriks inputSampelY = new Matriks(y);

        System.out.println("Matriks X:");
        inputSampelX.printMatriks();
        System.out.println("Matriks Y:");
        inputSampelY.printMatriks();

        alertMsgRegresi.setText("");

        if (selectLinier.isSelected()){
            try {

                regresiType = RegresiType.LINIER;
                b = regressor.normalEquation(inputSampelX, inputSampelY);
                displayLinearRegression(b);
            } catch (Exception e) {
                alertMsgRegresi.setText("*Terjadi kesalahan dalam regresi linier");
                return;
            }
        } else if (selectKuadratik.isSelected()){
            // try {
                regresiType = RegresiType.KUADRATIK;
                Matriks kuadratikX = regressor.addQuadratic(inputSampelX);
                kuadratikX.printMatriks();
                b = regressor.normalEquation(kuadratikX, inputSampelY);
                displayQuadraticRegression(b);
            // } catch (Exception e) {
            //     System.err.println(e);
            //     alertMsgRegresi.setText("*Terjadi kesalahan dalam regresi kuadratik");
            //     return;
            // }
        } else {
            alertMsgRegresi.setText("*Pilih jenis regresi terlebih dahulu");
            return;
        }
        regressed = true;
    }

    @FXML
    private void taksir(){
        if (!regressed){
            alertMsgTaksiran.setText("*Lakukan regresi terlebih dahulu");
            return;
        } else {
            alertMsgTaksiran.setText("");
            
            double[][] xTaksiran = new double[nPeubah][1];
            String[] xTaksiranString = inputVariabelBebas.getText().split("\\s+");
            if (xTaksiranString.length != nPeubah){
                alertMsgTaksiran.setText("*Jumlah variabel bebas tidak sesuai dengan jumlah peubah");
                return;
            }

            for (int i = 0; i < nPeubah; i++){
                xTaksiran[i][0] = Double.parseDouble(xTaksiranString[i]);
            }

            Matriks taksiranX = new Matriks(xTaksiran);

            double y = regressor.predict(taksiranX, b);

            if (regresiType == RegresiType.LINIER){
                Text text = new Text("Hasil taksiran regresi linier:\n");
                taksiranTextFlow.getChildren().add(text);
            } else {
                Text text = new Text("Hasil taksiran regresi kuadratik:\n");
                taksiranTextFlow.getChildren().add(text);
            }

            Text text = new Text("Pada titik ");
            taksiranTextFlow.getChildren().add(text);

            for (int i = 0; i < nPeubah; i++){
                text = new Text("x");
                text.setStyle("-fx-font-size: 10pt;");  
                taksiranTextFlow.getChildren().add(text);

                Text baseText = new Text(Integer.toString(i+1));
                baseText.setStyle("-fx-font-size: 8pt;");
                baseText.setTranslateY(5);
                taksiranTextFlow.getChildren().add(baseText);

                text = new Text(" = ");
                taksiranTextFlow.getChildren().add(text);

                text = new Text(String.format("%.2f", xTaksiran[i][0]));
                taksiranTextFlow.getChildren().add(text);
                if (i != nPeubah - 1){
                    text = new Text(", ");
                    taksiranTextFlow.getChildren().add(text);
                }
            }

            text = new Text(":\n");
            taksiranTextFlow.getChildren().add(text);

            text = new Text(String.format("Y = %.4f", y));
            taksiranTextFlow.getChildren().add(text);

        }
    }

    private void displayLinearRegression(Matriks b) {
        b.makePositiveZero();
        System.out.println("Matriks regresi:");
        b.printMatriks();

        Text text = new Text("Fungsi regresi linier:\n");
        regresiTextFlow.getChildren().add(text);

        text = new Text("Y = ");
        regresiTextFlow.getChildren().add(text);

        if (checkZeroCol(b, 0)) {
            text = new Text(String.format("%.4f", 0));
            regresiTextFlow.getChildren().add(text);
        }
        else {
            for (int i = 0; i < b.getRow(); i++) {
                if (i == 0) {
                    text = new Text(String.format("%.4f", b.Mat[i][0]));
                    regresiTextFlow.getChildren().add(text);
                } else {
                    if (b.Mat[i][0] < 0) {
                        text = new Text(" - ");
                        regresiTextFlow.getChildren().add(text);
                    } else {
                        text = new Text(" + ");
                        regresiTextFlow.getChildren().add(text);
                    }

                    if (Math.abs(b.Mat[i][0]) != 1) {
                        text = new Text(String.format("%.4f", Math.abs(b.Mat[i][0])));
                        regresiTextFlow.getChildren().add(text);
                    }

                    text = new Text("x");
                    text.setStyle("-fx-font-size: 10pt;");
                    regresiTextFlow.getChildren().add(text);

                    Text baseText = new Text(Integer.toString(i));
                    baseText.setStyle("-fx-font-size: 8pt;");
                    baseText.setTranslateY(5);
                    regresiTextFlow.getChildren().add(baseText);
                }
            }
        }
    }

    private void displayQuadraticRegression(Matriks b) {
        b.makePositiveZero();
        System.out.println("Matriks regresi:");
        b.printMatriks();

        Text text = new Text("Fungsi regresi linier:\n");
        regresiTextFlow.getChildren().add(text);

        text = new Text("Y = ");
        regresiTextFlow.getChildren().add(text);

        if (checkZeroCol(b, 0)) {
            text = new Text(String.format("%.4f", 0));
            regresiTextFlow.getChildren().add(text);
        }
        else {
            // AAAAAAAAAAAAAAAAAAAAAAAAAAAA
        }
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