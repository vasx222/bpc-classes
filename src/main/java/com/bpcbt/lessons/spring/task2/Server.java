package com.bpcbt.lessons.spring.task2;

import com.bpcbt.lessons.spring.task1.SimpleController;

import java.io.DataInputStream;
import java.io.IOException;
import java.net.ServerSocket;
import java.net.Socket;

public class Server extends Thread {
    private final int port;
    private Socket client;
    private final SimpleController controller;
    private DataInputStream dis;

    public Server(final int port, final SimpleController controller) {
        this.port = port;
        this.controller = controller;
        System.out.println("Server created.");
    }

    @Override
    public void run() {
        try (ServerSocket server = new ServerSocket(port)) {
            System.out.println("Waiting for client...");
            client = server.accept();
            System.out.println("Client connected.");
            dis = new DataInputStream(client.getInputStream());

            String msg;
            while (!client.isClosed()) {
                msg = dis.readUTF();
                if (msg.equalsIgnoreCase("stop")) {
                    break;
                }
                controller.insertAccount(Generator.getAccountFromJSON(msg));
            }
        } catch (IOException e) {
            e.printStackTrace();
        } finally {
            close();
        }
    }

    @Override
    public void interrupt() {
        super.interrupt();
        close();
    }

    public void close() {
        try {
            dis.close();
            client.close();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
}
