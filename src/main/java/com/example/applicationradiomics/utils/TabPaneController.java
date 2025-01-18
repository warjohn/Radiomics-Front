package com.example.applicationradiomics.utils;

import javafx.geometry.Side;
import javafx.scene.control.*;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.layout.StackPane;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileReader;
import java.io.IOException;
import java.nio.file.Path;
import java.util.ArrayList;
import java.util.Collections;
import java.nio.file.Paths;
import java.util.List;

public class TabPaneController {

    private TreeView<String> tree_prj;
    private TabPane tab_pane;

    public TabPaneController(TreeView<String> tree_prj, TabPane tab_pane) {
        this.tree_prj = tree_prj;
        this.tab_pane = tab_pane;
    }

    public void TabMain() {
        handlerDoubleClick();
    }

    private void handlerDoubleClick() {
        tree_prj.setOnMouseClicked(event -> {
            if (event.getClickCount() == 2) {
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
        });
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
