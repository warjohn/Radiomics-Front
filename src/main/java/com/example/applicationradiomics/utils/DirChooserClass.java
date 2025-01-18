package com.example.applicationradiomics.utils;

import javafx.fxml.FXML;
import javafx.scene.Scene;
import javafx.scene.control.MenuItem;
import javafx.stage.DirectoryChooser;
import javafx.stage.Stage;

import java.io.File;

public class DirChooserClass {
    @FXML
    public File DirChooser(Stage stage, MenuItem openFileButton){
        Scene scene = openFileButton.getParentPopup().getScene().getWindow().getScene();
        DirectoryChooser directoryChooser = new DirectoryChooser();
        File selectedDir = directoryChooser.showDialog(stage);
        if (selectedDir != null) {
            System.out.println("Dir selected^ " + selectedDir.getAbsolutePath());
        }
        return selectedDir;
    }
}
