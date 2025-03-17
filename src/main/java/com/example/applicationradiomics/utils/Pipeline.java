package com.example.applicationradiomics.utils;

import com.jfoenix.controls.JFXTreeView;
import javafx.scene.control.TreeItem;
import org.yaml.snakeyaml.DumperOptions;
import org.yaml.snakeyaml.Yaml;

import java.io.*;
import java.nio.charset.StandardCharsets;
import java.util.*;

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

//    TODO stub
    public void writeToYaml(String filePath) {
        try {
            // Настройка форматирования YAML
            DumperOptions options = new DumperOptions();
            options.setIndent(4);
            options.setDefaultFlowStyle(DumperOptions.FlowStyle.BLOCK); // Человекочитаемый формат
            Yaml yaml = new Yaml(options);

            // Создание структуры данных для YAML
            Map<String, Object> config = new LinkedHashMap<>();

            // Раздел data
            Map<String, Object> dataSection = new LinkedHashMap<>();
            Map<String, Integer> filePaths = new LinkedHashMap<>();
            filePaths.put("testDir/pathan.csv", 1);
            dataSection.put("file-path", filePaths);
            config.put("data", dataSection);

            // Раздел llmModel
            Map<String, String> llmModelSection = new LinkedHashMap<>();
            llmModelSection.put("name", "llama3.1");
            llmModelSection.put("lang", "RU");
            llmModelSection.put("base_url", "http://0.0.0.0:11436");
            config.put("llmModel", llmModelSection);

            // Раздел radiomics
            Map<String, Object> radiomicsSection = new LinkedHashMap<>();
            List<Map<String, Object>> filters = new ArrayList<>();

            Map<String, Object> filter1 = new LinkedHashMap<>();
            filter1.put("name", Arrays.asList("Original", "LoG"));
            Map<String, Object> settings = new LinkedHashMap<>();
            settings.put("binWidth", 25);
            settings.put("wavelet", "db2");
            settings.put("sigma", Arrays.asList(0.5, 2.5));
            filter1.put("settings", settings);
            filters.add(filter1);

            radiomicsSection.put("filters", filters);
            config.put("radiomics", radiomicsSection);

            // Раздел sklearn
            Map<String, Object> sklearnSection = new LinkedHashMap<>();
            List<Map<String, Object>> transformers = new ArrayList<>();

            Map<String, Object> transformer1 = new LinkedHashMap<>();
            transformer1.put("name", "StandardScaler");
            transformer1.put("params", new LinkedHashMap<>());
            transformers.add(transformer1);

            Map<String, Object> transformer2 = new LinkedHashMap<>();
            transformer2.put("name", "OneHotEncoder");
            Map<String, Object> oneHotParams = new LinkedHashMap<>();
            oneHotParams.put("handle_unknown", "ignore");
            transformer2.put("params", oneHotParams);
            transformers.add(transformer2);

            sklearnSection.put("transformers", transformers);

            Map<String, Object> modelSection = new LinkedHashMap<>();
            modelSection.put("name", "KNeighborsClassifier");
            Map<String, Object> modelParams = new LinkedHashMap<>();
            modelParams.put("weights", "distance");
            modelParams.put("leaf_size", 1);
            modelSection.put("params", modelParams);

            sklearnSection.put("model", modelSection);

            Map<String, Object> selectionParamsSection = new LinkedHashMap<>();
            selectionParamsSection.put("enable", true);
            selectionParamsSection.put("name", "GridSearchCV");
            Map<String, Object> gridSearchParams = new LinkedHashMap<>();
            gridSearchParams.put("cv", 5);
            gridSearchParams.put("verbose", 3);
            gridSearchParams.put("n_jobs", 20);
            selectionParamsSection.put("params", gridSearchParams);

            Map<String, Object> paramGrid = new LinkedHashMap<>();
            paramGrid.put("KNeighborsClassifier__leaf_size", Arrays.asList(1, 2, 20));
            selectionParamsSection.put("param_grid", paramGrid);

            sklearnSection.put("selectionParams", selectionParamsSection);

            List<Map<String, Object>> metrics = new ArrayList<>();
            Map<String, Object> metric1 = new LinkedHashMap<>();
            metric1.put("name", "accuracy_score");
            metric1.put("params", new LinkedHashMap<>());
            metrics.add(metric1);

            Map<String, Object> metric2 = new LinkedHashMap<>();
            metric2.put("name", "precision_score");
            Map<String, Object> precisionParams = new LinkedHashMap<>();
            precisionParams.put("average", "weighted");
            precisionParams.put("zero_division", 0);
            metric2.put("params", precisionParams);
            metrics.add(metric2);

            Map<String, Object> metric3 = new LinkedHashMap<>();
            metric3.put("name", "balanced_accuracy_score");
            metric3.put("params", new LinkedHashMap<>());
            metrics.add(metric3);

            sklearnSection.put("metrics", metrics);

            List<Map<String, Object>> pictures = new ArrayList<>();
            Map<String, Object> picture1 = new LinkedHashMap<>();
            picture1.put("name", "#roc_curve");
            pictures.add(picture1);

            Map<String, Object> picture2 = new LinkedHashMap<>();
            picture2.put("name", "#confusion_matrix");
            pictures.add(picture2);

            sklearnSection.put("pictures", pictures);

            config.put("sklearn", sklearnSection);

            // Генерация YAML-содержимого
            String yamlContent = yaml.dump(config);
            System.out.println(yamlContent);

            // Запись в файл
            try (OutputStreamWriter writer = new OutputStreamWriter(new FileOutputStream(filePath), StandardCharsets.UTF_8)) {
                writer.write(yamlContent);
            }
        } catch (IOException e) {
            throw new RuntimeException("Ошибка при записи YAML в файл: " + filePath, e);
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
