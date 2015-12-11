package model;

import model.client.Client;
import model.server.Server;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.IOException;

public class MainTest {
    public static void main(String[] args) throws IOException, FileNotFoundException {
        Thread serverThread = new Thread(() -> Server.start(new File("config.xml")));
        serverThread.setName("server_thread");
        serverThread.start();

        FileInputStream fileInputStream = new FileInputStream("commands.txt");

        Thread clientThread = new Thread(() -> new Client(new File("config.xml"), fileInputStream).start(2));
        clientThread.setName("Running client threads, " + 10);
        clientThread.start();
    }
}
