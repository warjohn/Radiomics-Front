package com.example.applicationradiomics.utils;
import java.io.IOException;
import java.net.URI;
import java.util.concurrent.CountDownLatch;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import javax.websocket.*;

@ClientEndpoint
public class WebSocketClient {

    private static CountDownLatch latch;
    private String sendingText;

    private void setText(String text) {this.sendingText = text;}

    @OnOpen
    public void onOpen(Session session) {
        System.out.println("Connected to server");
        try {
            session.getBasicRemote().sendText(sendingText);
            System.out.println("Sent text: " + sendingText);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    @OnMessage
    public void onMessage(String message) {
        System.out.println("Received from server: " + message);
    }

    @OnClose
    public void onClose(Session session, CloseReason reason) {
        System.out.println("Session closed: " + reason);
    }
    public void send(String string) {
        setText(string);
        latch = new CountDownLatch(1);
        ExecutorService executorService = Executors.newSingleThreadExecutor();
        executorService.submit(() -> {
            try {
                WebSocketContainer container = ContainerProvider.getWebSocketContainer();
                System.out.println(container.toString());
                container.connectToServer(WebSocketClient.class, URI.create("ws://localhost:8000/ws"));
                latch.await();
            } catch (Exception e) {
                e.printStackTrace();
            }
        });
    }

}
