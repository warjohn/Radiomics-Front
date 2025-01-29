package com.example.applicationradiomics;



import com.example.applicationradiomics.utils.TabPaneController;
import com.jfoenix.controls.JFXTreeView;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.*;
import javafx.scene.layout.AnchorPane;

import java.io.IOException;
import java.util.Objects;

import javafx.scene.layout.VBox;
import javafx.stage.Stage;

import com.example.applicationradiomics.utils.TreeController;

public class HelloController {
    @FXML private MenuItem mbt_files;
    @FXML private SplitPane rigth_split;
    @FXML private AnchorPane bottom_panel;
    @FXML private MenuItem mbt_folders;
    @FXML private TreeView<String> tree_prj;
    @FXML private TabPane tab_panel;
    @FXML private VBox radiomics;
    @FXML private VBox datareport;
    @FXML private VBox ml;
    @FXML private JFXTreeView<String> yamlConfig;
    @FXML private MenuItem apply;
    @FXML private TextArea text_area;

    private String position = "CENTER";
    private boolean flagClose = false;
    private Stage primarystage;



    @FXML
    public void initialize() {
        //initLogs();
        EventMainTree();
    }

    public void setPrimaryStage(Stage stage) {
        this.primarystage = stage;
    }

    public void EventMainTree() {
        TabPaneController tabPaneController = new TabPaneController(tree_prj, tab_panel, radiomics, datareport, ml, yamlConfig, apply, text_area);
        TreeController treeController = new TreeController(mbt_folders, mbt_files, primarystage, tree_prj, tabPaneController);
        treeController.TreeMain();
        tabPaneController.initPanes();
    }

    public void changeThemeColor() throws IOException {
        FXMLLoader loader = new FXMLLoader(getClass().getResource("themes.fxml"));
        Parent root = loader.load();
        ThemesController themesController = loader.getController();
        Stage newStage = new Stage();
        newStage.setScene(new Scene(root));
        themesController.setThemesStage(newStage);
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