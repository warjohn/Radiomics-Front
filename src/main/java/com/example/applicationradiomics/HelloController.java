package com.example.applicationradiomics;

import com.example.applicationradiomics.utils.TabPaneController;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.scene.control.*;
import javafx.scene.layout.AnchorPane;

import java.util.Objects;
import javafx.stage.Stage;

import com.example.applicationradiomics.utils.TreeController;

public class HelloController {
    @FXML private MenuItem mbt_files;
    @FXML private SplitPane rigth_split;
    @FXML private AnchorPane bottom_panel;
    @FXML private MenuItem mbt_folders;
    @FXML private TreeView<String> tree_prj;
    @FXML private TabPane tab_panel;

    private String position = "CENTER";
    private boolean flagClose = false;

    private Stage primarystage;


    @FXML
    public void initialize() {
        EventMainTree();
        EventTab();
    }

    public void setPrimaryStage(Stage stage) {
        this.primarystage = stage;
    }

    public void EventMainTree() {
        TreeController treeController = new TreeController(mbt_folders, mbt_files, primarystage, tree_prj);
        treeController.TreeMain();
    }

    public void EventTab() {
        TabPaneController tabPaneController = new TabPaneController(tree_prj, tab_panel);
        tabPaneController.TabMain();
    }


    public void collapseTextArea(ActionEvent actionEvent) {
        if (!Objects.equals(position, "CENTER") && !Objects.equals(position, "TOP")) {
            // Разворачиваем нижнюю панель
            rigth_split.setDividerPositions(0.8); // Устанавливаем делитель на 80%
            position = "CENTER";
        } else {
            // Сворачиваем нижнюю панель
            rigth_split.setDividerPositions(1.0); // Устанавливаем делитель на 100% (сильно сжимаем панель)
            position = "BOTTOM";
        }
        System.out.println(position);
    }

    public void fullscreen(ActionEvent event) {
        if (!Objects.equals(position, "BOTTOM") && !Objects.equals(position, "CENTER")) {
            rigth_split.setDividerPositions(1.0); // Убираем нижнюю панель, 0% высоты
            position = "BOTTOM";
        } else {
            rigth_split.setDividerPositions(0.0); // Возвращаем разделитель на 80%
            position = "TOP";
        }
        System.out.println(position);
    }

    public void Close() {
        if (!flagClose) {
            bottom_panel.setVisible(false);
            rigth_split.setDividerPosition(0, 1);
            rigth_split.setMinHeight(0.0);
        }
    }


}