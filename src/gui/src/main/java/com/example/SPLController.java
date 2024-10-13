package com.example;

import java.io.IOException;
import javafx.fxml.FXML;

public class SPLController {
    
    @FXML
    private void switchToMainMenu() throws IOException {
        App.setRoot("mainMenu");
    }
}
