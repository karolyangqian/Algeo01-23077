package com.myapp;

import java.io.IOException;
import javafx.fxml.FXML;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;

public class SPLController {
    
    @FXML
    private void switchToMainMenu() throws IOException {
        App.setRoot("mainMenu");
    }

    @FXML

    ImageView imgview;
    Image image = new Image(App.class.getResource("generated_images/gigachad.png").toString());

    public void showImage() {
        if (imgview.getImage() == null) {
            imgview.setImage(image);
        }
        else {
            imgview.setImage(null);
        }
    }
}
