package com.example.applicationradiomics.utils;

import javafx.application.Platform;
import javafx.scene.control.TextArea;
import org.java_websocket.client.WebSocketClient;
import org.java_websocket.handshake.ServerHandshake;

import java.net.URI;
import java.net.URISyntaxException;

public class socket extends WebSocketClient {

    private TextArea textArea;

    public socket(String serverUrl) throws URISyntaxException {
        super(new URI(serverUrl));
    }

    public void setTextArea(TextArea textArea) {
        this.textArea = textArea;
    }

    @Override
    public void onOpen(ServerHandshake handshakedata) {
        System.out.println("Connected to WebSocket server");
    }

    @Override
    public void onMessage(String message) {
        if (message.startsWith("LOG: ")) {
            appendToTextArea("[SERVER LOG] " + message.substring(5) + "\n", "red");
        }
//        } else {
//            appendToTextArea("Received from server: " + message + "\n", "black");
//        }
    }

    @Override
    public void onClose(int code, String reason, boolean remote) {
        System.out.println("Connection closed: " + reason);
    }

    @Override
    public void onError(Exception ex) {
        System.err.println("Error: " + ex.getMessage());
    }

    private void appendToTextArea(String text, String color) {
        Platform.runLater(() -> {
            textArea.appendText(text); // Добавляем текст с новой строки
            textArea.setStyle("-fx-text-fill: " + color + ";"); // Красим текст
        });
    }
}
