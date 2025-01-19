package com.example.applicationradiomics;

import com.jfoenix.controls.JFXSlider;
import javafx.fxml.FXML;
import javafx.scene.control.SplitPane;
import javafx.scene.layout.Pane;
import javafx.stage.Stage;

public class ThemesController {

    @FXML private SplitPane main_split;
    @FXML private JFXSlider red;
    @FXML private JFXSlider green;
    @FXML private JFXSlider blue;
    @FXML private JFXSlider transparency;
    @FXML private Pane example_pane;

    private Stage stage;

    public void setThemesStage(Stage stage) {
        this.stage = stage;
        this.stage.setTitle("Themes");
        this.stage.setResizable(false);
        this.stage.setFullScreen(false);
        this.stage.show();
        main_split.lookupAll(".split-pane-divider").forEach(divider -> {divider.setMouseTransparent(true);});
    }

    @FXML
    public void initialize() {
        setupSliders();
    }

    private void setupSliders() {
        red.valueProperty().addListener((observable, oldValue, newValue) -> updateColor());
        green.valueProperty().addListener((observable, oldValue, newValue) -> updateColor());
        blue.valueProperty().addListener((observable, oldValue, newValue) -> updateColor());
        transparency.valueProperty().addListener((observable, oldValue, newValue) -> updateColor());
    }

    private void updateColor() {
        int r = (int) red.getValue();
        int g = (int) green.getValue();
        int b = (int) blue.getValue();
        int tr = (int) transparency.getValue();

        System.out.println("Colors " + r + ", " + g + ", " + b + ", " + tr);

        String colorStyle = String.format("-fx-background-color: rgba(%d, %d, %d, %d);", r, g, b, tr);
        example_pane.setStyle(colorStyle);
    }
}
