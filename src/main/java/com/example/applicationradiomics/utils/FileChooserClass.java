package com.example.applicationradiomics.utils;


import javafx.fxml.FXML;
import javafx.scene.Scene;
import javafx.scene.control.MenuItem;
import javafx.stage.FileChooser;
import javafx.stage.Stage;

import java.io.File;

public class FileChooserClass {

    private FileChooser.ExtensionFilter extensionFilter = new FileChooser.ExtensionFilter("Table&Image files", "*.csv", "*.xlsx", "*.parquet", "*.xls", "*.jpg", "*.jpeg", "*.png");


    @FXML
    public File fileChooser(Stage stage, MenuItem openFileButton){
        Scene scene = openFileButton.getParentPopup().getScene().getWindow().getScene();
        FileChooser fileChooser = new FileChooser();
        fileChooser.getExtensionFilters().add(extensionFilter);
        File selectedFile = fileChooser.showOpenDialog(stage);
        if (selectedFile != null) {
            System.out.println("File selected^ " + selectedFile.getAbsolutePath());
        } else {
            System.out.println("File Dialog is closed");
        }
        return selectedFile;
    }

}
