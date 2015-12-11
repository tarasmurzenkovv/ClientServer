package model;

import model.client.Client;
import model.server.Server;

import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.util.concurrent.CountDownLatch;
import java.util.concurrent.ExecutionException;

public class MainTest {

    private static final int NUMBER_OF_THREADS = 4;

    public static void main(String[] args) throws IOException{
        CountDownLatch countDownLatch = new CountDownLatch(NUMBER_OF_THREADS);

        Thread serverThread = new Thread(() -> Server.start(new File("config.xml"), countDownLatch));
        serverThread.setName("server_thread");

        Thread clientThread = new Thread(() -> {
            try {
                new Client(new File("config.xml"), new FileInputStream("commands.txt")).start(NUMBER_OF_THREADS, countDownLatch);

            } catch (ExecutionException | IOException | InterruptedException e) {
                e.printStackTrace();
            }
        });
        clientThread.setName("Running client threads, " + 10);
        serverThread.start();
        clientThread.start();
    }
}
