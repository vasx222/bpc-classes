package com.bpcbt.lessons.spring.task2;

import java.io.DataOutputStream;
import java.io.IOException;
import java.net.Socket;

public class Client {
    private final String host;
    private final int port;
    private Socket socket;
    private DataOutputStream dos;

    public Client(final String host, final int port) {
        this.host = host;
        this.port = port;
        System.out.println("Client created.");
        System.out.println("Connecting to server...");
        try {
            socket = new Socket(host, port);
            System.out.println("Connected to server.");
            dos = new DataOutputStream(socket.getOutputStream());
        } catch (IOException e) {
            e.printStackTrace();
            close();
        }
    }

    public void sendMessage(String message) {
        try {
            if (!socket.isOutputShutdown()) {
                dos.writeUTF(message);
                dos.flush();
                if (message.equalsIgnoreCase("stop")) {
                    close();
                }
            }
        } catch (IOException e) {
            e.printStackTrace();
            close();
        }

    }

    public void close() {
        try {
            dos.close();
            socket.close();
        } catch (IOException e) {
            e.printStackTrace();
        }

    }
}
