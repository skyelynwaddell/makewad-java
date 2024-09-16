package com.example.makewad;

import javafx.stage.DirectoryChooser;
import javafx.stage.FileChooser;
import javafx.stage.Stage;

import javax.imageio.ImageIO;
import java.awt.image.BufferedImage;
import java.io.File;
import java.io.FilenameFilter;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

public class FileManager {

    public static File openFileChooser(String type) {
        FileChooser fileChooser = new FileChooser();

        fileChooser.getExtensionFilters().addAll(
                new FileChooser.ExtensionFilter("Supported Files", type)
        );
        File selectedFile = fileChooser.showOpenDialog(new Stage());
        if (selectedFile != null) {
            return selectedFile;
        }
        return null;
    }

    public static File openDirectoryChooser() {
        DirectoryChooser directoryChooser = new DirectoryChooser();
        File selectedDirectory = directoryChooser.showDialog(new Stage());
        if (selectedDirectory != null) {
            return selectedDirectory;
        }
        return null;
    }

    public static List<String> LoadAllImageNamesFromDirectory(File directory) {

        List<String> imageNames = new ArrayList<>();

        FilenameFilter imageFilter = (dir, name) -> {
            String lowercaseName = name.toLowerCase();
            return lowercaseName.endsWith(".png") || lowercaseName.endsWith(".jpg") || lowercaseName.endsWith(".jpeg");
        };

        File[] imageFiles = directory.listFiles(imageFilter);

        if (imageFiles == null) {
            System.out.println("No image files found in the directory");
            return null;
        }

        for (File imageFile : imageFiles) {
            try {
                BufferedImage image = ImageIO.read(imageFile);
                if (image != null){
                    imageNames.add(imageFile.getName());
                    System.out.println("Loaded Image: " + imageFile.getName());
                } else {
                    System.out.println("Failed to load image: " + imageFile.getName());
                }
            } catch(IOException e){
                System.out.println("Error reading image file: " + imageFile);
            }
        }
        return imageNames;
    }

    public static List<BufferedImage> LoadAllImagesFromDirectory(File directory) {
        List<BufferedImage> images = new ArrayList<>();

        FilenameFilter imageFilter = (dir, name) -> {
            String lowercaseName = name.toLowerCase();
            return lowercaseName.endsWith(".png") || lowercaseName.endsWith(".jpg") || lowercaseName.endsWith(".jpeg");
        };

        File[] imageFiles = directory.listFiles(imageFilter);

        if (imageFiles == null) {
            System.out.println("No image files found in the directory");
            return images;
        }

        for (File imageFile : imageFiles) {
            try {
                BufferedImage image = ImageIO.read(imageFile);
                if (image != null){
                    images.add(image);
                    System.out.println("Loaded Image: " + imageFile.getName());
                } else {
                    System.out.println("Failed to load image: " + imageFile.getName());
                }
            } catch(IOException e){
                System.out.println("Error reading image file: " + imageFile);
            }
        }
        return images;
    }
}
