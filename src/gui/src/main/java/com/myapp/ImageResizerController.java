package com.myapp;

import algeo.*;
import java.io.IOException;
import javafx.fxml.FXML;
import javafx.scene.control.*;
import javafx.scene.image.*;
import javafx.scene.text.*;
import javafx.stage.*;
import java.io.File;
import java.awt.image.BufferedImage;
import javax.imageio.ImageIO;
import javafx.embed.swing.SwingFXUtils;
import javafx.concurrent.*;

public class ImageResizerController {

    @FXML
    ImageView oldImageView = new ImageView();
    @FXML
    ImageView resizedImageView = new ImageView();
    @FXML
    Text oldSizeText = new Text();
    @FXML
    Text newSizeText = new Text();
    @FXML
    Text resizeAlertMsg = new Text();
    @FXML
    Text exportAlertMsg = new Text();
    @FXML
    TextField scaleWidthInput = new TextField();
    @FXML
    TextField scaleHeightInput = new TextField();
    @FXML
    ProgressBar resizeProgressBar = new ProgressBar();

    private BufferedImage oldImage;
    private BufferedImage resizedImage;
    private boolean imageLoaded;
    private boolean imageResized;

    private final double oldImageViewWidth = 300;
    private final double oldImageViewHeight = 240; 

    @FXML
    public void initialize() {
        resizeAlertMsg.setText("");
        exportAlertMsg.setText("");
        oldSizeText.setText("");
        newSizeText.setText("");
        imageLoaded = false;
        imageResized = false;
        resizeProgressBar.setVisible(false);
    }

    /**
     * Kembali ke main menu
     * @throws IOException
     */
    @FXML
    private void switchToMainMenu() throws IOException {
        App.setRoot("mainMenu");
    }

    @FXML
    private void chooseImage() {
        FileChooser fileChooser = new FileChooser();
        File initialDirectory = new File("./../../test");
        if (initialDirectory.exists()) {
            fileChooser.setInitialDirectory(initialDirectory);
        }
        fileChooser.setTitle("Open Image File");
        fileChooser.getExtensionFilters().addAll(
            new FileChooser.ExtensionFilter("All Image Files", "*.png", "*.jpg", "*.jpeg")
            );

        File file = fileChooser.showOpenDialog(new Stage());
        
        if (file != null) {
            try {
                oldImage = ImageIO.read(file);
                displayOldImage(SwingFXUtils.toFXImage(oldImage, null));
                oldSizeText.setText("Ukuran lama: " + oldImage.getWidth() + " x " + oldImage.getHeight());
                newSizeText.setText("");
                imageLoaded = true;
                imageResized = false;
                resizeAlertMsg.setText("");
                exportAlertMsg.setText("");
            } catch (IOException e) {
                System.out.println(e.getStackTrace());
                return;
            }
        }
    }

    @FXML
    private void resize() {
        if (!imageLoaded) {
            resizeAlertMsg.setText("*Muat gambar terlebih dahulu");
            return;
        }

        if (scaleWidthInput.getText().isEmpty() || scaleHeightInput.getText().isEmpty()) {
            resizeAlertMsg.setText("*Masukkan skala lebar dan tinggi");
            return;
        }

        resizeAlertMsg.setText("");
        exportAlertMsg.setText("");
        resizedImageView.setImage(null);
        resizedImageView.setFitWidth(0);
        resizedImageView.setFitHeight(0);
        
        BicubicSplineInterpolation bsi = new BicubicSplineInterpolation();

        double scale_x;
        double scale_y;
        try {
            scale_x = Double.parseDouble(scaleWidthInput.getText());
            scale_y = Double.parseDouble(scaleHeightInput.getText());
        } catch (NumberFormatException e) {
            resizeAlertMsg.setText("*Skala harus berupa angka");
            return;
        }

        Task<Void> resizeTask = new Task<Void>() {
            @Override
            protected Void call() throws Exception {
                BufferedImage newImage = bsi.resizeImage(oldImage, scale_x, scale_y);
        
                displayResizedImage(SwingFXUtils.toFXImage(newImage, null), scale_x*oldImageView.getFitWidth(), scale_y*oldImageView.getFitHeight());
                newSizeText.setText("Ukuran baru: " + newImage.getWidth() + " x " + newImage.getHeight());
                resizeProgressBar.setVisible(false);
                resizedImage = newImage;
                imageResized = true;
                return null;
            }
        };

        Task<Void> updateProgressTask = new Task<Void>() {
            @Override
            protected Void call() throws Exception {
                while (!resizeTask.isDone()) {
                    // updateProgress(BicubicSplineInterpolation.getResizeProgress(), 1);
                    resizeProgressBar.setProgress(BicubicSplineInterpolation.getResizeProgress());
                }
                return null;
            }
        };

        resizeProgressBar.setProgress(0);
        resizeProgressBar.setVisible(true);
        // resizeProgressBar.progressProperty().bind(resizeTask.progressProperty());

        new Thread(resizeTask).start();
        new Thread(updateProgressTask).start();


        // imageResized = true;
    }

    @FXML
    private void export() {
        if (!imageLoaded) {
            exportAlertMsg.setText("*Muat gambar terlebih dahulu");
            return;
        }

        if (!imageResized) {
            exportAlertMsg.setText("*Lakukan perbesaran gambar terlebih dahulu");
            return;
        }

        if (oldImage == null || resizedImage == null) {
            exportAlertMsg.setText("*Gambar tidak ditemukan");
            return;
        }

        
        FileChooser fileChooser = new FileChooser();
        File initialDirectory = new File("./../../test");
        if (initialDirectory.exists()) {
            fileChooser.setInitialDirectory(initialDirectory);
        }

        fileChooser.setTitle("Save Image File");
        fileChooser.getExtensionFilters().addAll(
            new FileChooser.ExtensionFilter("PNG Files", "*.png")
            );

            fileChooser.setInitialFileName("resized_image");
            
            File file = fileChooser.showSaveDialog(new Stage());
            
        if (file != null) {
            try {
                ImageIO.write(resizedImage, "png", file);
                exportAlertMsg.setText("");
            } catch (IOException e) {
                System.out.println(e.getStackTrace());
                return;
            }
        }
    }

    private void displayOldImage(Image image) {
        oldImageView.setPreserveRatio(true);

        oldImageView.setFitWidth(oldImageViewWidth);
        oldImageView.setFitHeight(oldImageViewHeight);

        oldImageView.setSmooth(true);
        oldImageView.setImage(image);
    }

    private void displayResizedImage(Image image, double frameWidth, double frameHeight) {
        resizedImageView.setPreserveRatio(true);

        double minWidth = 100;
        double minHeight = 80;
        double maxWidth = 600;
        double maxHeight = 480;

        resizedImageView.setFitWidth(Math.min(Math.max(frameWidth, minWidth), maxWidth));
        resizedImageView.setFitHeight(Math.min(Math.max(frameHeight, minHeight), maxHeight));

        resizedImageView.setSmooth(true);
        resizedImageView.setImage(image);
    }

}