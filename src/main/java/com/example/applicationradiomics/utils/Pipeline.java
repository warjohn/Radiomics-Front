package com.example.applicationradiomics.utils;
import com.jfoenix.controls.JFXTreeView;
import javafx.scene.control.TreeItem;
import org.yaml.snakeyaml.DumperOptions;
import org.yaml.snakeyaml.Yaml;

import java.io.FileInputStream;
import java.io.FileWriter;
import java.io.IOException;
import java.util.List;
import java.util.Map;

public class Pipeline {


    private Radiomics radiomics;
    private DataReport dataReport;
    private Ml ml;
    private JFXTreeView<String> yamlconfig;

    public Pipeline(DataReport dataReport, Radiomics radiomics, Ml ml, JFXTreeView<String> yamlconfig) {
        this.dataReport = dataReport;
        this.radiomics = radiomics;
        this.ml = ml;
        this.yamlconfig = yamlconfig;
    }

    public void writeToYaml(String filePath) throws IOException {
        DumperOptions options = new DumperOptions();
        options.setIndent(4);
        Yaml yaml = new Yaml(options);

        StringBuilder sb = new StringBuilder();

        sb.append("dataReport:\n");
        sb.append("  ").append(dataReport.getData()).append("\n");

        sb.append("radiomics:\n");
        for (String item : radiomics.getData()) {
            sb.append("  - ").append(item).append("\n");
        }

        sb.append("ml:\n");
        for (String item : ml.getData()) {
            sb.append("  - ").append(item).append("\n");
        }

        try (FileWriter writer = new FileWriter(filePath)) {
            writer.write(sb.toString());
            updateTreeViewWithYaml(filePath);
        }
    }

// TODO доделать дерево
    private void updateTreeViewWithYaml(String filePath) {
        try {
            Yaml yaml = new Yaml();
            FileInputStream fileInputStream = new FileInputStream(filePath);
            Map<String, Object> yamlData = yaml.load(fileInputStream);
            TreeItem<String> rootItem = new TreeItem<>("Root");
            rootItem.setExpanded(true);
            addItemsToTree(rootItem, yamlData);
            yamlconfig.setRoot(rootItem);
            yamlconfig.setShowRoot(false);
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    private void addItemsToTree(TreeItem<String> parent, Object data) {
        if (data == null) {
            return;
        }
        if (data instanceof Map) {
            Map<String, Object> map = (Map<String, Object>) data;
            for (String key : map.keySet()) {
                TreeItem<String> keyItem = new TreeItem<>(key);
                parent.getChildren().add(keyItem);
                addItemsToTree(keyItem, map.get(key));
            }
        } else if (data instanceof List) {
            List<Object> list = (List<Object>) data;
            for (Object item : list) {
                if (item != null) {
                    TreeItem<String> listItem = new TreeItem<>(item.toString());
                    parent.getChildren().add(listItem);
                }
            }
        } else {
            if (data != null) {
                parent.getChildren().add(new TreeItem<>(data.toString()));
            }
        }
    }

}
