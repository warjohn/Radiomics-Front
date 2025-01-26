module com.example.applicationradiomics {
    requires javafx.fxml;
    requires javafx.web;

    requires org.controlsfx.controls;
    requires com.dlsc.formsfx;
    requires net.synedra.validatorfx;
    requires org.kordamp.ikonli.javafx;
    requires org.kordamp.bootstrapfx.core;
    requires eu.hansolo.tilesfx;
    requires com.almasb.fxgl.all;
    requires annotations;
    requires com.jfoenix;
    requires org.yaml.snakeyaml;
    requires java.logging;
    requires javax.websocket.api;

    opens com.example.applicationradiomics to javafx.fxml;
    exports com.example.applicationradiomics;
    exports com.example.applicationradiomics.utils;
    opens com.example.applicationradiomics.utils to javafx.fxml;
}