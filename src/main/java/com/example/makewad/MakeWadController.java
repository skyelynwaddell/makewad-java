package com.example.makewad;

import javafx.event.EventHandler;
import javafx.fxml.FXML;
import javafx.scene.control.Label;

import java.io.File;
import java.io.IOException;
import java.net.URL;
import java.util.ResourceBundle;
import javafx.fxml.FXML;
import javafx.scene.control.Button;
import javafx.scene.control.ListView;
import javafx.scene.control.Tab;
import javafx.scene.control.TextField;
import javafx.scene.input.MouseEvent;
import javafx.stage.DirectoryChooser;

public class MakeWadController {
    @FXML
    private ResourceBundle resources;

    @FXML
    private URL location;

    @FXML
    private Button btnMakeWad;

    @FXML
    private Button btnOutput;

    @FXML
    private Button btnPalette;

    @FXML
    private Button btnTextureFolder;

    @FXML
    private ListView<?> listConsole;

    @FXML
    private Tab tabMakeWad;

    @FXML
    private Tab tabWadViewer;

    @FXML
    private TextField txtOutput;

    @FXML
    private TextField txtPalette;

    @FXML
    private TextField txtTextureFolder;

    private File textureFolder = null;
    private File palette = null;
    private File outputWad = null;

    @FXML
    void initialize() {
        assert btnMakeWad != null : "fx:id=\"btnMakeWad\" was not injected: check your FXML file 'makewad-view.fxml'.";
        assert btnOutput != null : "fx:id=\"btnOutput\" was not injected: check your FXML file 'makewad-view.fxml'.";
        assert btnPalette != null : "fx:id=\"btnPalette\" was not injected: check your FXML file 'makewad-view.fxml'.";
        assert btnTextureFolder != null : "fx:id=\"btnTextureFolder\" was not injected: check your FXML file 'makewad-view.fxml'.";
        assert listConsole != null : "fx:id=\"listConsole\" was not injected: check your FXML file 'makewad-view.fxml'.";
        assert tabMakeWad != null : "fx:id=\"tabMakeWad\" was not injected: check your FXML file 'makewad-view.fxml'.";
        assert tabWadViewer != null : "fx:id=\"tabWadViewer\" was not injected: check your FXML file 'makewad-view.fxml'.";
        assert txtOutput != null : "fx:id=\"txtOutput\" was not injected: check your FXML file 'makewad-view.fxml'.";
        assert txtPalette != null : "fx:id=\"txtPalette\" was not injected: check your FXML file 'makewad-view.fxml'.";
        assert txtTextureFolder != null : "fx:id=\"txtTextureFolder\" was not injected: check your FXML file 'makewad-view.fxml'.";

        //Select Texture Folder
        btnTextureFolder.setOnMouseClicked(new EventHandler<MouseEvent>() {
            @Override
            public void handle(MouseEvent mouseEvent) {
                textureFolder = FileManager.openDirectoryChooser();
                if (textureFolder == null) return;
                txtTextureFolder.setText(textureFolder.getAbsolutePath());
            }
        });

        //Select Palette
        btnPalette.setOnMouseClicked(new EventHandler<MouseEvent>() {
            @Override
            public void handle(MouseEvent mouseEvent) {
                palette = FileManager.openFileChooser("*.lmp");
                if (palette == null) return;
                txtPalette.setText(palette.getAbsolutePath());
            }
        });

        //Select Output WAD File
        btnOutput.setOnMouseClicked(new EventHandler<MouseEvent>() {
            @Override
            public void handle(MouseEvent mouseEvent) {
                outputWad = FileManager.openFileChooser("*.wad");
                if (outputWad == null) return;
                txtOutput.setText(outputWad.getAbsolutePath());
            }
        });

        //Make Wad
        btnMakeWad.setOnMouseClicked(new EventHandler<MouseEvent>() {
            @Override
            public void handle(MouseEvent mouseEvent) {
                try {
                    String output = WAD.MakeWad(textureFolder,palette,outputWad);
                } catch (IOException e) {
                    throw new RuntimeException(e);
                }
            }
        });
    }
}