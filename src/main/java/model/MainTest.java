package model;

import model.client.Client;
import model.server.Server;

import java.io.File;
import java.io.IOException;
import java.util.concurrent.CountDownLatch;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

public class MainTest {

    private static final int NUMBER_OF_THREADS = 200;

    public static void main(String[] args) throws IOException, InterruptedException {
        CountDownLatch countDownLatch = new CountDownLatch(NUMBER_OF_THREADS);
        ExecutorService executorService = Executors.newFixedThreadPool(2);
        Thread serverThread = new Thread(() -> Server.start(new File("config.xml"), countDownLatch));
        Thread clientThread = new Thread(() -> {
                new Client(new File("config.xml"), "commands.txt").start(NUMBER_OF_THREADS, countDownLatch);
        });

        executorService.submit(serverThread);
        executorService.submit(clientThread);
        executorService.shutdown();
    }
}
