package com.example.applicationradiomics.utils;

import javafx.application.Platform;
import javafx.scene.control.TextArea;

import java.util.logging.Formatter;
import java.util.logging.Handler;
import java.util.logging.LogRecord;

public class LoggerController extends Handler {

    private TextArea textArea;

    public LoggerController(TextArea textArea) {this.textArea = textArea;}

    @Override
    public void publish(LogRecord record) {
        if (textArea != null) {
            Formatter formatter = getFormatter();
            if (formatter != null) {
                String message = formatter.format(record);
                Platform.runLater(() -> textArea.appendText(message + "\n"));
            } else {
                // Логирование ошибки, если formatter равен null
                Platform.runLater(() -> textArea.appendText("Formatter is null\n"));
            }
        }
    }

    @Override
    public void flush() {}

    @Override
    public void close() throws SecurityException {}
}
