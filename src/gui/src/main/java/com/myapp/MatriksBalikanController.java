package com.myapp;

import algeo.*;
import java.io.IOException;
import javafx.fxml.FXML;
import javafx.scene.control.*;
import javafx.scene.text.*;

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


    @FXML
    public void initialize() {
        selectOBE.setToggleGroup(MetodeBalikan);
        selectMatriksAdjoin.setToggleGroup(MetodeBalikan);
    }
    
    @FXML
    private void switchToMainMenu() throws IOException {
        App.setRoot("mainMenu");
    }

    @FXML
    private void findBalikan() {
        // ----------------- CLEAR OUTPUT SOLUSI BALIKAN DI GUI -----------------
        solutionTextFlow.getChildren().clear();

        // ----------------- VALIDASI INPUT -----------------
        String matriksString = inputMatriks.getText().replaceAll("\n", " ");
        int row = Integer.parseInt(barisInput.getText());
        int col = row;
        String[] elements = matriksString.split(" ");

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

        balikan.printMatriks();
    }
}
