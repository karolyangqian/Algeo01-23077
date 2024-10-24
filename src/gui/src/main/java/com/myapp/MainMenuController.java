package com.myapp;

import java.io.IOException;
import javafx.fxml.FXML;

public class MainMenuController {
    
    @FXML
    private void switchToSistemPersamaanLinier() throws IOException {
        App.setRoot("sistemPersamaanLinier");
    }

    @FXML
    private void switchToDeterminan() throws IOException {
        App.setRoot("determinan");
    }

    @FXML
    private void switchToMatriksBalikan() throws IOException {
        App.setRoot("matriksBalikan");
    }

    @FXML
    private void switchToInterpolasiPolinomial() throws IOException {
        App.setRoot("interpolasiPolinomial");
    }
    
    @FXML
    private void switchToRegresiBerganda() throws IOException {
        App.setRoot("regresiBerganda");
    }

    @FXML
    private void switchToBicubicSpline() throws IOException {
        App.setRoot("bicubicSpline");
    }

    @FXML
    private void switchToImageResizer() throws IOException {
        App.setRoot("imageResizer");
    }

    @FXML
    private void keluar() {
        System.exit(0);
    }
}
