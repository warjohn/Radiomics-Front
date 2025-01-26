package com.example.applicationradiomics.utils;

import javafx.scene.control.*;
import javafx.scene.input.Clipboard;
import javafx.scene.input.ClipboardContent;
import javafx.scene.input.KeyCode;
import javafx.stage.Stage;

import java.io.File;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.Arrays;
import java.util.Comparator;

public class ContextMenuEdit {

    private final ContextMenu menu = new ContextMenu();
    private final MenuItem removebt = new MenuItem("Delete");
    private final MenuItem addbtfile = new MenuItem("Add file");
    private final MenuItem addbtfolder = new MenuItem("Add folder");
    private final MenuItem copyPath = new MenuItem("Copy Path");
    private TextField textfield;
    private File selectedFile, selectedDir;
    private TreeView<String> tree_prj;
    private Clipboard clipboard = Clipboard.getSystemClipboard();
    private ClipboardContent clipboardContent = new ClipboardContent();


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
                    tree_prj.setRoot(null);
                } else {
                    if (!qwe.getParent().getChildren().remove(qwe)) {
                            System.out.println("Error");
                    }
                }
            }
        });

        copyPath.setOnAction(event -> {
            if (tree_prj.getSelectionModel().getSelectedItem() != null) {
                TreeItem<String> qwe = tree_prj.getSelectionModel().getSelectedItem();
                if (qwe.isLeaf()) {clipboardContent.putString(buildFullPath(qwe).toString()); clipboard.setContent(clipboardContent);} else {clipboardContent.putString(buildFullPath(qwe).toString());clipboard.setContent(clipboardContent);}
            }
        });

        addbtfile.setOnAction(event -> {
            FileChooserClass file = new FileChooserClass();
            selectedFile = file.fileChooser(primaryStage, addbtfile);
            if (selectedFile != null) {
                if (tree_prj.getRoot() == null) {
                    TreeItem<String> rootItem = new TreeItem<>(selectedFile.getParent());
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

        addbtfolder.setOnAction(event -> {
            DirChooserClass dir = new DirChooserClass();
            selectedDir = dir.DirChooser(primaryStage, addbtfolder);
            if (selectedDir != null) {
                if (tree_prj.getRoot() == null || !selectedDir.getAbsolutePath().startsWith(tree_prj.getRoot().getValue())) {
                    TreeItem<String> rootItem = new TreeItem<>(selectedDir.getAbsolutePath());
                    rootItem.setExpanded(true);
                    addDirectoryToTree(selectedDir, rootItem);
                    tree_prj.setRoot(rootItem);
                } else {
                    TreeItem<String> rootItem = tree_prj.getRoot();
                    addDirectoryToTree(selectedDir, rootItem);
                }
            }
        });
    }

    private Path buildFullPath(TreeItem<String> item) {
        StringBuilder fullPath = new StringBuilder();

        while (item != null) {
            if (fullPath.length() == 0) {
                fullPath.insert(0, item.getValue());
            } else {
                fullPath.insert(0, item.getValue() + "/"); // Для Windows можно "\\" заменить
            }
            item = item.getParent();
        }

        return Paths.get(fullPath.toString());
    }

    private void addDirectoryToTree(File dir, TreeItem<String> parentItem) {
        File[] files = dir.listFiles();
        if (files != null) {
            Arrays.sort(files, new Comparator<File>() {
                @Override
                public int compare(File f1, File f2) {
                    if (f1.isDirectory() && !f2.isDirectory()) {
                        return -1; // f1 - папка, f2 - файл
                    } else if (!f1.isDirectory() && f2.isDirectory()) {
                        return 1; // f1 - файл, f2 - папка
                    } else {
                        return f1.getName().compareTo(f2.getName()); // если оба одинаковые, сортируем по имени
                    }
                }
            });
            for (File file : files) {
                TreeItem<String> fileItem = new TreeItem<>(file.getName());
                parentItem.getChildren().add(fileItem);

                // Если это папка, рекурсивно добавляем её содержимое в дерево
                if (file.isDirectory()) {
                    addDirectoryToTree(file, fileItem);
                }
            }
        }
    }

    private void settingsMenu() {
        menu.setPrefWidth(150.0);
        menu.setPrefHeight(200.0);
        menu.getItems().addAll(removebt, addbtfile, addbtfolder, copyPath);
    }

    private ContextMenu getMenu() {return menu;}
    public MenuItem get_removebt() {return removebt;}
    public MenuItem get_addbt() {return addbtfile;}
    public MenuItem get_addbtfolder() {return addbtfolder;}
    public MenuItem get_cppath() {return copyPath;}


}
