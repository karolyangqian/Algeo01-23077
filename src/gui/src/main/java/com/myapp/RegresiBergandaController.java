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

        if (!selectLinier.isSelected() && !selectKuadratik.isSelected()){
            alertMsgRegresi.setText("*Pilih jenis regresi terlebih dahulu");
            return;
        }

        if (inputX.getText().isBlank() || inputY.getText().isBlank()){
            alertMsgRegresi.setText("*Masukkan titik terlebih dahulu");
            return;
        }

        int nMinLinier = nPeubah + 1;

        if (selectLinier.isSelected() && mSampel < nMinLinier){
            alertMsgRegresi.setText(String.format("*Jumlah sampel harus minimal %d (n + 1) untuk regresi linier", nMinLinier));
            return;
        }

        int nMinKuadratik = 1 + 2*nPeubah + nPeubah*(nPeubah-1)/2;
        if (selectKuadratik.isSelected() && mSampel < nMinKuadratik){
            alertMsgRegresi.setText(String.format("*Jumlah sampel harus minimal %d (1 + 2*n + n*(n-1)/2) untuk regresi kuadratik.", nMinKuadratik));
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
            alertMsgRegresi.setText("*Jumlah elemen X dan Y tidak sesuai dengan input jumlah peubah dan sampel");
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

        // ----------------- REGRESI -----------------

        if (selectLinier.isSelected()){
            try {
                regresiType = RegresiType.LINIER;
                b = regressor.normalEquation(inputSampelX, inputSampelY);
            } catch (Exception e) {
                alertMsgRegresi.setText("*Terjadi kesalahan dalam regresi linier");
                return;
            }
        } else if (selectKuadratik.isSelected()){
            try {
                regresiType = RegresiType.KUADRATIK;
                Matriks kuadratikX = regressor.addQuadratic(inputSampelX);
                System.out.println("Matriks X kuadratik:");
                kuadratikX.printMatriks();
                b = regressor.normalEquation(kuadratikX, inputSampelY);
            } catch (Exception e) {
                System.err.println(e);
                alertMsgRegresi.setText("*Terjadi kesalahan dalam regresi kuadratik");
                return;
            }
        } 

        if (!hasOneSolution(b)){
            alertMsgRegresi.setText("*Regresi memiliki banyak solusi, coba dengan sampel yang berbeda atau tambah jumlah sampel");
            return;
        }

        // ----------------- OUTPUT SOLUSI REGRESI DI GUI -----------------

        if (regresiType == RegresiType.LINIER){
            try {
                displayLinearRegression(b);
            } catch (Exception e) {
                System.err.println(e);
                alertMsgRegresi.setText("*Terjadi kesalahan dalam menampilkan regresi linier");
                return;
            }
        } else {
            try {
                displayQuadraticRegression(b);
            } catch (Exception e) {
                System.err.println(e);
                alertMsgRegresi.setText("*Terjadi kesalahan dalam menampilkan regresi kuadratik");
                return;
            }
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
            taksiranTextFlow.getChildren().clear();
            
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
            if (regresiType == RegresiType.KUADRATIK){
                Linalg linalg = new Linalg();
                taksiranX = new Matriks(linalg.transposeMatriks(taksiranX));
                Matriks XKuadratik = regressor.addQuadratic(taksiranX);
                System.out.println("Matriks X kuadratik:");
                XKuadratik.printMatriks();
                taksiranX = new Matriks(linalg.transposeMatriks(XKuadratik));
            }
            System.err.println("Matriks taksiran X:");
            taksiranX.printMatriks();
            System.out.println("Matriks b:");
            b.printMatriks();

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

                text = new Text(String.format("%.2f", taksiranX.Mat[i][0]));
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
        // b.makePositiveZero();
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
                    } else if (b.Mat[i][0] > 0) {
                        text = new Text(" + ");
                        regresiTextFlow.getChildren().add(text);
                    } else {
                        continue;
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
        System.out.println("Matriks b:");
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

            // ------------------- KONSTANTA -------------------
            if (b.Mat[0][0] != 0) {
                text = new Text(String.format("%.4f", b.Mat[0][0]));
                regresiTextFlow.getChildren().add(text);
            }

            // ------------------- VARIABEL LINEAR -------------------
            for (int i = 1; i <= nPeubah; i++) {
                if (b.Mat[i][0] < 0) {
                    text = new Text(" - ");
                    regresiTextFlow.getChildren().add(text);
                } else if (b.Mat[i][0] > 0) {
                    text = new Text(" + ");
                    regresiTextFlow.getChildren().add(text);
                } else {
                    continue;
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

            // ------------------- VARIABEL KUADRATIK -------------------
            int idxstart = nPeubah+1;
            for (int i = 0; i < nPeubah; i++) {
                if (b.Mat[idxstart+i][0] < 0) {
                    text = new Text(" - ");
                    regresiTextFlow.getChildren().add(text);
                } else if (b.Mat[idxstart+i][0] > 0) {
                    text = new Text(" + ");
                    regresiTextFlow.getChildren().add(text);
                } else {
                    continue;
                }

                if (Math.abs(b.Mat[idxstart+i][0]) != 1) {
                    text = new Text(String.format("%.4f", Math.abs(b.Mat[idxstart+i][0])));
                    regresiTextFlow.getChildren().add(text);
                }

                text = new Text("x");
                text.setStyle("-fx-font-size: 10pt;");
                regresiTextFlow.getChildren().add(text);

                Text baseText = new Text(Integer.toString(i+1));
                baseText.setStyle("-fx-font-size: 8pt;");
                baseText.setTranslateY(5);
                regresiTextFlow.getChildren().add(baseText);

                baseText = new Text("2");
                baseText.setStyle("-fx-font-size: 8pt;");
                baseText.setTranslateY(-5);
                regresiTextFlow.getChildren().add(baseText);
            }

            // ------------------- VARIABEL INTERAKSI -------------------
            idxstart = idxstart + nPeubah;
            for (int i = 0; i < nPeubah; i++) {
                for (int j = i + 1; j < nPeubah; j++) {
                    if (b.Mat[idxstart+i][0] < 0) {
                        text = new Text(" - ");
                        regresiTextFlow.getChildren().add(text);
                    } else if (b.Mat[idxstart][0] > 0) {
                        text = new Text(" + ");
                        regresiTextFlow.getChildren().add(text);
                    } else {
                        continue;
                    }

                    if (Math.abs(b.Mat[idxstart][0]) != 1) {
                        text = new Text(String.format("%.4f", Math.abs(b.Mat[idxstart][0])));
                        regresiTextFlow.getChildren().add(text);
                    }

                    text = new Text("x");
                    text.setStyle("-fx-font-size: 10pt;");
                    regresiTextFlow.getChildren().add(text);

                    Text baseText = new Text(Integer.toString(i+1));
                    baseText.setStyle("-fx-font-size: 8pt;");
                    baseText.setTranslateY(5);
                    regresiTextFlow.getChildren().add(baseText);

                    text = new Text("x");
                    text.setStyle("-fx-font-size: 10pt;");
                    regresiTextFlow.getChildren().add(text);

                    baseText = new Text(Integer.toString(j+1));
                    baseText.setStyle("-fx-font-size: 8pt;");
                    baseText.setTranslateY(5);
                    regresiTextFlow.getChildren().add(baseText);

                    idxstart++;
                }
            }
            
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

    private boolean hasOneSolution(Matriks solution) {
        if (solution.getCol() == 1) {
            return true;
        }

        for (int i = 0; i < solution.getRow(); i++) {
            for (int j = 1; j < solution.getCol(); j++) {
                if (solution.Mat[i][j] != 0) {
                    return false;
                }
            }
        }
        return true;
    }
}