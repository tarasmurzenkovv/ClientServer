package model.client;

import model.utils.ConfigLoader;
import org.apache.log4j.Logger;

import java.io.File;
import java.io.InputStream;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.concurrent.ExecutionException;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

public class Client{
    private static Logger logger = Logger.getLogger(Client.class);
    private File configs;
    private InputStream inputStream;
    private int port;
    private String ip;


    public Client(File file, InputStream inputStream) {
        this.configs = file;
        this.inputStream = inputStream;
        Map<String, Object> configs = ConfigLoader.loadXMLConfigsFromFile(file);
        this.port = (Integer) configs.get("port");
        this.ip = (String) configs.get("ip");
    }

    public Client start() {
        ClientTaskRunnable clientTaskRunnable = new ClientTaskRunnable(port, ip);
        clientTaskRunnable.setReplyListener(message -> System.out.println(message.receive().toString()));
        clientTaskRunnable.setInputStream(System.in);

        Thread clientThread = new Thread(clientTaskRunnable);
        clientThread.setName("client_thread");
        clientThread.start();
        return this;
    }

    public Client start(int numberOfThreads) {
        ExecutorService executorService = Executors.newFixedThreadPool(numberOfThreads);
        List<String> collectedServerReplies = new ArrayList<>();

        for (int i = 0; i < numberOfThreads + 1; i++) {
            List<String> serverReplies = new ArrayList<>();
            ClientTaskCallable clientTaskCallable = new ClientTaskCallable(port, ip);
            clientTaskCallable.setReplyListener(message -> serverReplies.add(message.getMessage()));
            clientTaskCallable.setInputStream(this.inputStream);
            try {
                collectedServerReplies.addAll(executorService.submit(clientTaskCallable).get());
            } catch (ExecutionException | InterruptedException e) {
                e.printStackTrace();
            }
        }
        executorService.shutdown();
        System.out.println("After processing a file");
        collectedServerReplies.forEach(System.out::println);
        return this;
    }
}
