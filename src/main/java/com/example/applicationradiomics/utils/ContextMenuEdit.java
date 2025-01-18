package com.example.applicationradiomics.utils;

import javafx.scene.control.*;
import javafx.scene.input.KeyCode;
import javafx.stage.Stage;

import java.io.File;

public class ContextMenuEdit {

    private ContextMenu menu = new ContextMenu();
    private MenuItem removebt = new MenuItem("Delete");
    private MenuItem addbt = new MenuItem("Add");
    //private MenuItem makebt = new MenuItem("Make file");
    private TextField textfield;
    private File selectedFile;
    private TreeView<String> tree_prj;


    private Stage primaryStage;

    private ContextMenuEdit(TreeView<String> tree_prj, Stage primaryStage) {
        this.primaryStage = primaryStage;
        this.tree_prj = tree_prj;
        settingsMenu();
        BtnHanler();
    }

    public static ContextMenu MenuInstance(TreeView<String> tree_prj, Stage primaryStage) {
        ContextMenuEdit contextMenuEdit = new ContextMenuEdit(tree_prj, primaryStage);
        return contextMenuEdit.getMenu();
    }

    public void BtnHanler() {

        removebt.setOnAction(event -> {
            if (tree_prj.getSelectionModel().getSelectedItem() != null) {
                TreeItem<String> qwe = tree_prj.getSelectionModel().getSelectedItem();
                if (qwe == tree_prj.getRoot() && qwe != null) {
                    tree_prj.getRoot().getChildren().clear();
                    tree_prj.getRoot().setValue("");
                } else {
                    if (!qwe.getParent().getChildren().remove(qwe)) {
                            System.out.println("Error");
                    }
                }
            }
        });

        addbt.setOnAction(event -> {
            FileChooserClass file = new FileChooserClass();
            selectedFile = file.fileChooser(primaryStage, addbt);
            if (selectedFile != null) {
                if (tree_prj.getRoot() == null) {
                    TreeItem<String> rootItem = new TreeItem<>("Root File");
                    rootItem.setExpanded(true);
                    rootItem.getChildren().add(new TreeItem<>(selectedFile.getName()));
                    tree_prj.setRoot(rootItem);
                } else {
                    TreeItem<String> rootItem = tree_prj.getRoot();
                    rootItem.getChildren().add(new TreeItem<>(selectedFile.getName()));
                    tree_prj.setRoot(rootItem);
                }
            }
        });
//        makebt.setOnAction(event -> {
//            if (tree_prj.getRoot() == null) {
//                TreeItem<String> rootItem = new TreeItem<>("Root Item");
//                rootItem.setExpanded(true);
//                tree_prj.setRoot(rootItem);
//                textfield = new TextField();
//                textfield.setPromptText("write file name");
//                textfield.setOnAction(textevent -> {
//                    if (!textfield.getText().isEmpty()) {
//                        TreeItem<String> newItem = new TreeItem<>(textfield.getText());
//                        rootItem.getChildren().add(newItem);
//                        // create file on computer
//                        createFile();
//                        textfield.clear();
//                    }
//                });
//            }
//        });
    }


    private void createFile() {

    }

    private void settingsMenu() {
        menu.setPrefWidth(150.0);
        menu.setPrefHeight(200.0);
        menu.getItems().addAll(removebt, addbt);
    }

    private ContextMenu getMenu() {
        return menu;
    }
    public MenuItem get_removebt() {
        return removebt;
    }
    public MenuItem get_addbt() {
        return addbt;
    }
//    public MenuItem get_makebt() {
//        return makebt;
//    }

}
