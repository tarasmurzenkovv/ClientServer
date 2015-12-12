package model.client;

import model.utils.ConfigLoader;
import org.apache.log4j.Logger;

import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.concurrent.CountDownLatch;
import java.util.concurrent.ExecutionException;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

public class Client {
    private static Logger logger = Logger.getLogger(Client.class);
    private InputStream inputStream;
    private int port;
    private String ip;
    private String pathToFileWithCommands;
    
    public Client(File file, InputStream inputStream) {
        this.inputStream = inputStream;
        Map<String, Object> configs = ConfigLoader.loadXMLConfigsFromFile(file);
        this.port = (Integer) configs.get("port");
        this.ip = (String) configs.get("ip");
    }

    public Client(File file, String pathToFileWithCommands) {
        Map<String, Object> configs = ConfigLoader.loadXMLConfigsFromFile(file);
        this.port = (Integer) configs.get("port");
        this.ip = (String) configs.get("ip");
        this.pathToFileWithCommands = pathToFileWithCommands;
    }

    public void start() {
        ClientTaskRunnable clientTaskRunnable = new ClientTaskRunnable(port, ip);
        clientTaskRunnable.setReplyListener(message -> System.out.println(message.receive().toString()));
        clientTaskRunnable.setInputStream(this.inputStream);

        Thread clientThread = new Thread(clientTaskRunnable);
        clientThread.setName("client_thread");
        clientThread.start();
    }

    public void start(int numberOfThreads, CountDownLatch countDownLatch) {
        ExecutorService executorService = Executors.newFixedThreadPool(numberOfThreads);
        List<String> collectedServerReplies = new ArrayList<>();

        for (int i = 0; i < numberOfThreads; i++) {
            try {
                ClientTaskCallable clientTaskCallable = new ClientTaskCallable(port, ip);
                clientTaskCallable.setInputStream(new FileInputStream(pathToFileWithCommands));
                collectedServerReplies.addAll(executorService.submit(clientTaskCallable).get());
                countDownLatch.countDown();
            } catch (ExecutionException | InterruptedException | IOException e) {
                logger.error("Exception occurred while processing a file with commands. Exception: ", e);
            }
        }
        System.out.println("After processing a file");
        collectedServerReplies.forEach(System.out::println);
        executorService.shutdown();
    }
}
