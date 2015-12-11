package model;

import model.client.Client;
import model.server.Server;

import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.util.concurrent.ExecutionException;

public class MainTest {
    public static void main(String[] args) throws IOException{
        Thread serverThread = new Thread(() -> Server.start(new File("config.xml")));
        serverThread.setName("server_thread");

        Thread clientThread = new Thread(() -> {
            try {
                new Client(new File("config.xml"), new FileInputStream("commands.txt")).start(10);
            } catch (ExecutionException | IOException | InterruptedException e) {
                e.printStackTrace();
            }
        });
        clientThread.setName("Running client threads, " + 10);


        serverThread.start();
        clientThread.start();
    }
}
