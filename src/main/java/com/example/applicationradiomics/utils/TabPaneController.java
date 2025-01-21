package com.example.applicationradiomics.utils;

import com.jfoenix.controls.JFXCheckBox;
import com.jfoenix.controls.JFXTreeView;
import javafx.scene.control.*;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.layout.HBox;
import javafx.scene.layout.VBox;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileReader;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Collections;
import java.nio.file.Paths;
import java.util.List;

public class TabPaneController {

    private TreeView<String> tree_prj;
    private TabPane tab_pane;
    private VBox radiomics;
    private VBox datareport;
    private VBox ml;
    private JFXTreeView<String> yamlConfig;
    private MenuItem apply;

    private Radiomics radiomicsdata = new Radiomics();
    private DataReport dataReport = new DataReport();
    private Ml mldata = new Ml();

    public TabPaneController(TreeView<String> tree_prj, TabPane tab_pane, VBox radiomics, VBox datareport, VBox ml, JFXTreeView<String> yamlConfig, MenuItem apply) {
        this.tree_prj = tree_prj;
        this.tab_pane = tab_pane;
        this.radiomics = radiomics;
        this.datareport = datareport;
        this.ml = ml;
        this.yamlConfig = yamlConfig;
        this.apply = apply;
    }

    public void initPanes() {
        getAllCheckBoxesRadiomics();
        getAllCheckBoxesData();
        getAllCheckBoxesML();
        inithandlerApply();
    }



//    TODO зменить промт текст на обычный текст textfield пока туда ничего не вписывается - заглушка

    private void getAllCheckBoxesRadiomics() {
        radiomics.getChildren()
                .stream()
                .filter(node -> node instanceof HBox) // Оставляем только HBox
                .map(hbox -> (HBox) hbox) // Преобразуем в HBox
                .forEach(hbox -> {
                    JFXCheckBox checkbox = (JFXCheckBox) hbox.getChildren()
                            .stream()
                            .filter(node -> node instanceof JFXCheckBox)
                            .findFirst()
                            .orElse(null);

                    TextField textField = (TextField) hbox.getChildren()
                            .stream()
                            .filter(node -> node instanceof TextField)
                            .findFirst()
                            .orElse(null);

                    if (checkbox != null && textField != null) {
                        textField.setVisible(false); // Скрываем поле ввода по умолчанию

                        checkbox.setOnAction(event -> {
                            boolean isChecked = checkbox.isSelected();
                            textField.setVisible(isChecked);
                            System.out.println("Чекбокс '" + checkbox.getText() + "' -> TextField visible: " + isChecked);
                            String data = checkbox.getText() + ": " + textField.getPromptText();
                            radiomicsdata.addData(data);
                        });

                    }
                });
    }

    private void getAllCheckBoxesData() {
        datareport.getChildren()
                .stream()
                .filter(node -> node instanceof HBox)
                .map(hbox -> (HBox) hbox)
                .forEach(hbox -> {
                    JFXCheckBox checkbox = (JFXCheckBox) hbox.getChildren()
                            .stream()
                            .filter(node -> node instanceof JFXCheckBox)
                            .findFirst()
                            .orElse(null);

                    checkbox.setOnAction(event -> {
                        boolean isChecked = checkbox.isSelected();
                        System.out.println("Чекбокс '" + checkbox.getText() + "' -> TextField visible: " + isChecked);
                        dataReport.setData(checkbox.getText());});
                });
    }

    private void getAllCheckBoxesML() {
        ml.getChildren()
                .stream()
                .filter(node -> node instanceof HBox) // Оставляем только HBox
                .map(hbox -> (HBox) hbox) // Преобразуем в HBox
                .forEach(hbox -> {
                    JFXCheckBox checkbox = (JFXCheckBox) hbox.getChildren()
                            .stream()
                            .filter(node -> node instanceof JFXCheckBox)
                            .findFirst()
                            .orElse(null);

                    TextField textField = (TextField) hbox.getChildren()
                            .stream()
                            .filter(node -> node instanceof TextField)
                            .findFirst()
                            .orElse(null);

                    if (checkbox != null && textField != null) {
                        textField.setVisible(false); // Скрываем поле ввода по умолчанию

                        checkbox.setOnAction(event -> {
                            boolean isChecked = checkbox.isSelected();
                            textField.setVisible(isChecked);
                            System.out.println("Чекбокс '" + checkbox.getText() + "' -> TextField visible: " + isChecked);
                            mldata.addData(checkbox.getText() + ": " + textField.getPromptText());
                        });
                    }
                });
    }

    private void inithandlerApply() {
        apply.setOnAction(event -> {
            Pipeline pipeline = new Pipeline(dataReport, radiomicsdata, mldata, yamlConfig);
            try {
                pipeline.writeToYaml("cofig.yaml");
            } catch (IOException e) {
                e.printStackTrace();
            }
        });
    }


    public void handlerDoubleClick() {
        String folderpath = tree_prj.getRoot().getValue();
        String filename = tree_prj.getSelectionModel().getSelectedItem().getValue();
        String fullPath = Paths.get(folderpath, filename).toString();
        System.out.println(fullPath);
        Tab newTab = new Tab(filename);
        newTab.setClosable(true);
        if (fullPath.endsWith(".csv")) {
            TableView<List<String>> tableview = new TableView<>();
            List<List<String>> data = readCSVfile(fullPath);
            System.out.println(data);
            if (!data.isEmpty()) {
                List<String> header = data.get(0);
                for (String columns : header) {
                    TableColumn<List<String>, String> tablecolumn = new TableColumn<>(columns);
                    int clIndex = header.indexOf(columns);
                    tablecolumn.setCellValueFactory(cellData -> new javafx.beans.property.SimpleStringProperty(cellData.getValue().get(clIndex)));
                    tableview.getColumns().add(tablecolumn);
                }
                tableview.getItems().addAll(data.subList(1, data.size()));
            }
            newTab.setContent(tableview);
        } else {
            Image image = new Image(new File(fullPath).toURI().toString());
            ImageView imageView = new ImageView(image);
            imageView.setPreserveRatio(true);
            imageView.setFitWidth(tab_pane.getWidth());
            newTab.setContent(imageView);
        }
        tab_pane.getTabs().add(newTab);
    }

    private List<List<String>> readCSVfile(String filename) {
        List<List<String>> data = new ArrayList<>();
        try (BufferedReader bt = new BufferedReader(new FileReader(filename))) {
            String line;
            while ((line = bt.readLine()) != null) {
                String[] values = line.split(",");
                List<String> row = new ArrayList<>();
                Collections.addAll(row, values);
                data.add(row);
             }
        } catch (IOException e) {
            e.printStackTrace();
        }
        return data;
    }

}
