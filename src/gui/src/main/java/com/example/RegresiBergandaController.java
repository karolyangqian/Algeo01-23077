package com.example;

import java.io.IOException;
import javafx.fxml.FXML;

public class RegresiBergandaController {
    
    @FXML
    private void switchToMainMenu() throws IOException {
        App.setRoot("mainMenu");
    }
}
