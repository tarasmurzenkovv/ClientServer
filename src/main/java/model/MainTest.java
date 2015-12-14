package model;

import model.client.Client;
import model.server.Server;

import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.util.List;
import java.util.concurrent.*;

public class MainTest {

    private static final int NUMBER_OF_THREADS = 1;

    public static void main(String[] args) throws IOException, InterruptedException, ExecutionException {
        CountDownLatch countDownLatch = new CountDownLatch(NUMBER_OF_THREADS);
        ExecutorService executorService = Executors.newFixedThreadPool(2);
        InputStream inputStream = new FileInputStream("commands.txt");
        List<String> actualServerReplies;
        List<String> actualClientRecivers;

        ReplyListener replyListener = m -> {

        };

        Callable<List<String>> clientThread = ()-> new Client(new File("config.xml"), inputStream).start(NUMBER_OF_THREADS, countDownLatch);
        Callable<List<String>> serverThread = () -> Server.start(new File("config.xml"), replyListener, countDownLatch);
        
        Future<List<String>> expectedServerMessages = executorService.submit(serverThread);
        Future<List<String>> expectedClientMessages = executorService.submit(clientThread);
        executorService.submit(clientThread);
        System.out.println("from main thread");
        actualServerReplies = expectedServerMessages.get();
        actualClientRecivers = expectedClientMessages.get();
        System.out.println(actualServerReplies);
        System.out.println(actualClientRecivers);
        executorService.shutdownNow();
    }
}
