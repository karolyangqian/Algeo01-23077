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
    
    @FXML
    public void initialize() {
        selectEkspansiKofaktor.setToggleGroup(MetodeDeterminan);
        selectMatriksSegitiga.setToggleGroup(MetodeDeterminan);
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
        // ----------------- CLEAR OUTPUT SOLUSI DETERMINAN DI GUI -----------------
        solutionTextFlow.getChildren().clear();

        // ----------------- VALIDASI INPUT -----------------
        String matriksString = inputMatriks.getText().replaceAll("\n", " ");
        int row = Integer.parseInt(barisInput.getText());
        int col = row;
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
        
        alertMsg.setText("");

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

    }

}
