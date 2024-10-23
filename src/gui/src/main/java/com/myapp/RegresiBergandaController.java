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

public class RegresiBergandaController {

    @FXML
    TextField jumlahPeubahInput = new TextField();
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
    
    enum RegresiType {
        LINIER, KUADRATIK
    }

    private LinearRegression regressor = new LinearRegression();
    private Matriks b;
    private boolean regressed;
    private boolean predicted;
    private int nPeubah;
    private int mSampel;
    private RegresiType regresiType;
    private File inputFile;
    private File outputFile;
    private Scanner scanner;
    FileChooser fileChooser = new FileChooser();
    private String outputRegresiString;
    private String outputTaksirString;

    public void initialize() {
        selectLinier.setToggleGroup(jenisRegresi);
        selectKuadratik.setToggleGroup(jenisRegresi);
        regressed = false;
        predicted = false;
    }

    /**
    * Export text file pada lokasi sesuai input pengguna
    * @throws IOException
    */
    @FXML
    private void exportFile() throws IOException {
        FileHandler fh = new FileHandler();
        if (!(regressed && predicted)) {
            alertMsgRegresi.setText("*Lakukan regresi dan hitung nilai terikatnya terlebih dahulu");
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

        alertMsgRegresi.setText("");
        alertMsgTaksiran.setText("");

        String out = String.join("", outputRegresiString, "\n\n", outputTaksirString);

        fh.saveTextToFile(out, outputFile);
    }

    /**
     * Buka file sampel regresi dan memasukkan ke text field dan text area
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
        
        alertMsgRegresi.setText("");
        alertMsgTaksiran.setText("");

        jumlahPeubahInput.clear();
        jumlahSampelInput.clear();
        inputX.clear();
        inputY.clear();
        inputVariabelBebas.clear();
        try {
            scanner = new Scanner(inputFile);
            String nm = scanner.nextLine();
            String[] nmSplit = nm.split("\\s+");
            jumlahPeubahInput.setText(nmSplit[0]);
            jumlahSampelInput.setText(nmSplit[1]);
            int n = Integer.parseInt(nmSplit[0]);
            int m = Integer.parseInt(nmSplit[1]);
            for (int i = 0; i < m; i++) {
                String[] xy = scanner.nextLine().split("\\s+");
                for (int j = 0; j < n; j++) {
                    inputX.appendText(xy[j] + " ");
                }
                inputX.appendText("\n");
                inputY.appendText(xy[n] + "\n");
            }
            inputVariabelBebas.appendText(scanner.nextLine());
            scanner.close();
        } catch (Exception e) {
            System.out.println("Error: " + e);
            alertMsgRegresi.setText("*File input tidak valid");
        }
    }
    
    @FXML
    private void switchToMainMenu() throws IOException {
        App.setRoot("mainMenu");
    }
    
    /**
     * Melakukan regresi linier atau kuadratik sesuai pilihan user
     */
    @FXML
    private void regresi() {
        // ----------------- CLEAR ALERT DAN OUTPUT SOLUSI REGRESI DI GUI -----------------
        alertMsgRegresi.setText("");
        regresiTextFlow.getChildren().clear();
        taksiranTextFlow.getChildren().clear();

        // ----------------- VALIDASI INPUT -----------------

        if (jumlahPeubahInput.getText().isBlank() || jumlahSampelInput.getText().isBlank() || inputX.getText().isBlank() || inputY.getText().isBlank()){
            alertMsgRegresi.setText("*Masukkan jumlah peubah dan sampel, X, dan Y yang sesuai terlebih dahulu");
            return;
        }

        nPeubah = Integer.parseInt(jumlahPeubahInput.getText());
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
        
        TextFlowHandler tfh = new TextFlowHandler();
        outputRegresiString = tfh.textFlowToString(regresiTextFlow);
        regressed = true;
        predicted = false;
    }

    /**
     * Melakukan taksiran nilai Y berdasarkan inputan user
     */
    @FXML
    private void taksir(){
        if (!regressed){
            alertMsgTaksiran.setText("*Lakukan regresi terlebih dahulu");
            return;
        } else {
            alertMsgTaksiran.setText("");
            taksiranTextFlow.getChildren().clear();

            if (inputVariabelBebas.getText().isBlank()){
                alertMsgTaksiran.setText("*Masukkan nilai variabel bebas yang akan ditaksir");
                return;
            }
            
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

        TextFlowHandler tfh = new TextFlowHandler();
        outputTaksirString = tfh.textFlowToString(taksiranTextFlow);
        predicted = true;
    }

    /**
     * Menampilkan regresi linier pada text flow
     * @param b
     */
    private void displayLinearRegression(Matriks b) {
        // b.makePositiveZero();
        System.out.println("Matriks regresi:");
        b.printMatriks();

        Text text = new Text("Persamaan regresi linier:\n");
        regresiTextFlow.getChildren().add(text);

        text = new Text("Y = ");
        regresiTextFlow.getChildren().add(text);

        boolean printed = false;

        if (b.getRow() == 1) {
            text = new Text(String.format("%.4f", b.Mat[0][0]));
            regresiTextFlow.getChildren().add(text);
            return;
        }

        if (b.Mat[0][0] != 0) {
            text = new Text(String.format("%.4f", b.Mat[0][0]));
            regresiTextFlow.getChildren().add(text);
            printed = true;
        }

        for (int i = 1; i < b.getRow(); i++) {
            if (b.Mat[i][0] == 0) {
                continue;
            }

            if (b.Mat[i][0] < 0) {
                text = new Text(" - ");
                regresiTextFlow.getChildren().add(text);
            } else if (b.Mat[i][0] > 0 && printed) {
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

    /**
     * Menampilkan regresi kuadratik pada text flow
     */
    private void displayQuadraticRegression(Matriks b) {
        b.makePositiveZero();
        System.out.println("Matriks b:");
        b.printMatriks();

        Text text = new Text("Persamaan regresi kuadratik:\n");
        regresiTextFlow.getChildren().add(text);

        text = new Text("Y = ");
        regresiTextFlow.getChildren().add(text);

        boolean printed = false;

        if (b.getRow() == 1) {
            text = new Text(String.format("%.4f", b.Mat[0][0]));
            regresiTextFlow.getChildren().add(text);
            return;
        }

        if (b.Mat[0][0] != 0) {
            text = new Text(String.format("%.4f", b.Mat[0][0]));
            regresiTextFlow.getChildren().add(text);
            printed = true;
        }
        
        // ------------------- VARIABEL LINEAR -------------------
        for (int i = 1; i <= nPeubah; i++) {
            if (b.Mat[i][0] < 0) {
                text = new Text(" - ");
                regresiTextFlow.getChildren().add(text);
            } else if (b.Mat[i][0] > 0 && printed) {
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
            printed = true;
        }

        // ------------------- VARIABEL KUADRATIK -------------------
        int idxstart = nPeubah+1;
        for (int i = 0; i < nPeubah; i++) {
            if (b.Mat[idxstart+i][0] < 0) {
                text = new Text(" - ");
                regresiTextFlow.getChildren().add(text);
            } else if (b.Mat[idxstart+i][0] > 0 && printed) {
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
            printed = true;
        }

        // ------------------- VARIABEL INTERAKSI -------------------
        idxstart = idxstart + nPeubah;
        for (int i = 0; i < nPeubah; i++) {
            for (int j = i + 1; j < nPeubah; j++) {
                if (b.Mat[idxstart][0] < 0) {
                    text = new Text(" - ");
                    regresiTextFlow.getChildren().add(text);
                } else if (b.Mat[idxstart][0] > 0 && printed) {
                    text = new Text(" + ");
                    regresiTextFlow.getChildren().add(text);
                } else {
                    idxstart++;
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

                printed = true;
                idxstart++;
            }   
        }
    }

    /**
     * Mengecek apakah matriks memiliki satu solusi
     * @param solution
     * @return true jika matriks memiliki satu solusi
     */
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