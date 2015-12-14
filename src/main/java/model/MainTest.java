package model;

import model.client.Client;
import model.server.Server;

import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.*;

public class MainTest {

    private static final int NUMBER_OF_THREADS = 1;

    public static void main(String[] args) throws IOException, InterruptedException, ExecutionException {
        CountDownLatch countDownLatch = new CountDownLatch(NUMBER_OF_THREADS);
        ExecutorService executorService = Executors.newFixedThreadPool(2);
        InputStream inputStream = new FileInputStream("commands.txt");

        List<String> serverReplies = new ArrayList<>();
        List<String> actualServerReplies;

        ReplyListener collector = m -> serverReplies.add(m.getMessage());

        Thread clientThread = new Thread(() -> new Client(new File("config.xml"), inputStream).start(NUMBER_OF_THREADS, countDownLatch));

        Callable<List<String>> serverThread = () -> Server.start(new File("config.xml"), collector, countDownLatch);


        Future<List<String>> expectedProcessedMessages = executorService.submit(serverThread);
        executorService.submit(clientThread);
        System.out.println("from main thread");
        actualServerReplies = expectedProcessedMessages.get();
        System.out.println(actualServerReplies);
        executorService.shutdownNow();
    }
}
