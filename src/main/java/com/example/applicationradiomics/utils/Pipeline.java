package com.example.applicationradiomics.utils;

import com.jfoenix.controls.JFXTreeView;
import javafx.scene.control.TreeItem;
import org.yaml.snakeyaml.DumperOptions;
import org.yaml.snakeyaml.Yaml;

import java.io.*;
import java.nio.charset.StandardCharsets;
import java.util.List;
import java.util.Map;

public class Pipeline {

    private final Radiomics radiomics;
    private final DataReport dataReport;
    private final Ml ml;
    private final JFXTreeView<String> yamlconfig;

    public Pipeline(DataReport dataReport, Radiomics radiomics, Ml ml, JFXTreeView<String> yamlconfig) {
        this.dataReport = dataReport;
        this.radiomics = radiomics;
        this.ml = ml;
        this.yamlconfig = yamlconfig;
    }

    public void writeToYaml(String filePath) {
        try {
            DumperOptions options = new DumperOptions();
            options.setIndent(4);
            Yaml yaml = new Yaml(options);

            StringBuilder sb = buildYamlContent();
            System.out.println(sb);

            try (OutputStreamWriter writer = new OutputStreamWriter(new FileOutputStream(filePath), StandardCharsets.UTF_8)) {
                writer.write(sb.toString());
            }

            updateTreeViewWithYaml(sb);
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    private StringBuilder buildYamlContent() {
        StringBuilder sb = new StringBuilder()
                .append("dataReport:\n  ").append(dataReport.getData()).append("\n")
                .append("radiomics:\n");

        radiomics.getData().forEach(item -> sb.append("  - ").append(item).append("\n"));
        sb.append("ml:\n");
        ml.getData().forEach(item -> sb.append("  - ").append(item).append("\n"));

        return sb;
    }

    private void updateTreeViewWithYaml(StringBuilder sb) {
        try {
            Yaml yaml = new Yaml();
            Map<String, Object> yamlData = yaml.load(sb.toString());
            TreeItem<String> rootItem = new TreeItem<>("Root");
            addItemsToTree(rootItem, yamlData);
            rootItem.setExpanded(true);
            rootItem.getChildren().forEach(item -> item.setExpanded(true));
            yamlconfig.setRoot(rootItem);
            yamlconfig.setShowRoot(false);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    private void addItemsToTree(TreeItem<String> parent, Object data) {
        if (data instanceof Map) {
            ((Map<String, Object>) data).forEach((key, value) -> {
                TreeItem<String> keyItem = new TreeItem<>(key);
                parent.getChildren().add(keyItem);
                addItemsToTree(keyItem, value);
            });
        } else if (data instanceof List) {
            ((List<?>) data).forEach(item -> {
                if (item != null) {
                    parent.getChildren().add(new TreeItem<>(item.toString()));
                }
            });
        } else if (data != null) {
            parent.getChildren().add(new TreeItem<>(data.toString()));
        }
    }
}
