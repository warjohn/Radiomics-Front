package com.example.applicationradiomics;

import javafx.application.Application;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.stage.Stage;
import org.jetbrains.annotations.Contract;
import org.jetbrains.annotations.NotNull;

import java.io.IOException;

public class HelloApplication extends Application {

    //TODO доделать меню изменения цвета и прикрутить функицонал основной
    @NotNull
    @Contract("_, _ -> param1")
    private Stage initStage(Stage stage, Scene scene) {
        stage.setMinHeight(800.0);
        stage.setMinWidth(800.0);
        stage.setTitle("MLRad");
        stage.setScene(scene);
        return stage;
    }

    @Override
    public void start(Stage stage) throws IOException {
        FXMLLoader fxmlLoader = new FXMLLoader(HelloApplication.class.getResource("hello-view.fxml"));
        Parent root = fxmlLoader.load();

        //Пердача объекта stage в контроллер для пикрутки доп функций
        HelloController helloController = fxmlLoader.getController();
        helloController.setPrimaryStage(stage);

        Scene scene = new Scene(root);
        stage = initStage(stage, scene);
        stage.show();
    }

    public static void main(String[] args) {
        launch();
    }
}