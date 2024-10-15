package com.myapp;

import java.io.IOException;
import javafx.fxml.FXML;

public class MatriksBalikanController {
    
    @FXML
    private void switchToMainMenu() throws IOException {
        App.setRoot("mainMenu");
    }
}
