package com.example;

import java.io.IOException;
import javafx.fxml.FXML;

public class MainMenuController {
    
    @FXML
    private void switchToSistemPersamaanLinier() throws IOException {
        App.setRoot("sistemPersamaanLinier");
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
}
