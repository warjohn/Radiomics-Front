package com.example.applicationradiomics.utils;

import javafx.scene.control.ContextMenu;
import javafx.scene.control.MenuItem;
import javafx.scene.control.TreeItem;
import javafx.scene.control.TreeView;
import javafx.scene.input.MouseButton;

import java.io.File;
import java.util.Arrays;
import java.util.Comparator;

import javafx.scene.input.MouseEvent;
import javafx.stage.Stage;

public class TreeController {

    private final MenuItem mbt_folders;
    private final MenuItem mbt_files;
    private final TreeView<String> tree_prj;
    private final Stage primarystage;
    private ContextMenu activeMenu;
    private File selectedFile, selecteddir;

    public TreeController(MenuItem mbt_folders, MenuItem mbt_files, Stage primarystage, TreeView<String> tree_prj) {
        this.mbt_files = mbt_files;
        this.mbt_folders = mbt_folders;
        this.primarystage = primarystage;
        this.tree_prj = tree_prj;
    }

    public void TreeMain() {
        activeMenu = ContextMenuEdit.MenuInstance(tree_prj, primarystage);
        EventbuttonFile();
        EventbuttonFolder();
        EventTree();
    }

    private void EventbuttonFolder() {
        mbt_folders.setOnAction(event -> {
            DirChooserClass dirChooserClass = new DirChooserClass();
            selecteddir = dirChooserClass.DirChooser(primarystage, mbt_folders);
            if (selecteddir != null) {
                if (tree_prj.getRoot() == null || !selecteddir.getAbsolutePath().startsWith(tree_prj.getRoot().getValue())) {
                    TreeItem<String> rootItem = new TreeItem<>(selecteddir.getAbsolutePath());
                    rootItem.setExpanded(true);
                    addDirectoryToTree(selecteddir, rootItem);
                    tree_prj.setRoot(rootItem);
                } else {
                    TreeItem<String> rootItem = tree_prj.getRoot();
                    addDirectoryToTree(selecteddir, rootItem);
                }
            }
        });
    }

    private void EventbuttonFile() {
        mbt_files.setOnAction(event -> {
            FileChooserClass file = new FileChooserClass();
            selectedFile = file.fileChooser(primarystage, mbt_files);
            if (selectedFile != null) {
                if (tree_prj.getRoot() == null || !selectedFile.getAbsolutePath().startsWith(tree_prj.getRoot().getValue()) ) {
                    TreeItem<String> rootItem = new TreeItem<>(selectedFile.getAbsolutePath());
                    rootItem.setExpanded(true);
                    rootItem.getChildren().add(new TreeItem<>(selectedFile.getName()));
                    tree_prj.setRoot(rootItem);
                } else {
                    TreeItem<String> rootItem = tree_prj.getRoot();
                    rootItem.getChildren().add(new TreeItem<>(selectedFile.getAbsolutePath()));
                }
            }
        });
    }

    private void EventTree() {
        tree_prj.setOnContextMenuRequested(event -> {
            tree_prj.setOnMouseClicked(e -> {
                if (activeMenu != null && activeMenu.isShowing()) {
                    activeMenu.hide();
                }
                if (e.getButton() == MouseButton.SECONDARY) {
                    activeMenu.show(tree_prj, e.getScreenX(), e.getScreenY());
                }
            });
        });
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


}
