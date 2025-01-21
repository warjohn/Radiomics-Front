package com.example.applicationradiomics;

import com.jfoenix.controls.JFXButton;
import com.jfoenix.controls.JFXCheckBox;
import com.jfoenix.controls.JFXSlider;
import javafx.fxml.FXML;
import javafx.geometry.Insets;
import javafx.scene.control.SplitPane;
import javafx.scene.layout.Background;
import javafx.scene.layout.BackgroundFill;
import javafx.scene.layout.CornerRadii;
import javafx.scene.layout.Pane;
import javafx.scene.paint.Color;
import javafx.scene.paint.CycleMethod;
import javafx.scene.paint.LinearGradient;
import javafx.scene.paint.Stop;
import javafx.stage.Stage;

import java.io.FileWriter;
import java.io.IOException;

public class ThemesController {

    @FXML private SplitPane main_split;
    @FXML private JFXSlider red;
    @FXML private JFXSlider green;
    @FXML private JFXSlider blue;
    @FXML private JFXSlider transparency;
    @FXML private JFXCheckBox checkbox;
    @FXML private Pane example_pane;
    @FXML private JFXButton done;

    private static final String CSS_FILE = "styles.css";
    private Stage stage;
    private boolean useGradient;
    private Color newColor;

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
        checkbox.selectedProperty().addListener((observable, oldValue, newValue) -> updateColor());
        done.setOnAction(event -> donefun());
    }

    private void donefun() {
        saveStylesToCSS(newColor, useGradient);
    }

    private void updateColor() {
        double r = red.getValue() / 255.0;  // RGB переводим в [0.0 - 1.0]
        double g = green.getValue() / 255.0;
        double b = blue.getValue() / 255.0;
        double tr = transparency.getValue(); // Прозрачность уже от 0.0 до 1.0

        System.out.println("Colors: " + r + ", " + g + ", " + b + ", Transparency: " + tr);

        newColor = new Color(r, g, b, 1.0);
        useGradient = checkbox.isSelected();
        CornerRadii cornerRadii = new CornerRadii(15);
        if (useGradient) {
            // Градиентный фон с учетом закругленных краев
            LinearGradient gradient = new LinearGradient(
                    0.0, 0.0, 1.0, 0.0, true, CycleMethod.NO_CYCLE,
                    new Stop(0.0, newColor),
                    new Stop(1.0, new Color(1.0 - r, 1.0 - g, 1.0 - b, tr))
            );
            example_pane.setBackground(new Background(new BackgroundFill(gradient, cornerRadii, Insets.EMPTY)));
        } else {
            // Обычный цвет с учетом закругления
            BackgroundFill fill = new BackgroundFill(newColor, cornerRadii, Insets.EMPTY);
            example_pane.setBackground(new Background(fill));
        }
    }


    private void saveStylesToCSS(Color bgColor, boolean useGradient) {
        try {
            FileWriter writer = new FileWriter(CSS_FILE);
            if (useGradient) {
                writer.write(String.format("* { -fx-background-color: linear-gradient(to right, rgba(%d, %d, %d, 1.0), rgba(%d, %d, %d, 1.0)); }",
                        (int) (bgColor.getRed() * 255), (int) (bgColor.getGreen() * 255), (int) (bgColor.getBlue() * 255),
                        255 - (int) (bgColor.getRed() * 255), 255 - (int) (bgColor.getGreen() * 255), 255 - (int) (bgColor.getBlue() * 255)
                ));
            } else {
                writer.write(String.format("* { -fx-background-color: rgba(%d, %d, %d, 1.0); }",
                        (int) (bgColor.getRed() * 255), (int) (bgColor.getGreen() * 255), (int) (bgColor.getBlue() * 255)
                ));
            }
            writer.close();
            System.out.println("CSS сохранен!");
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
}
