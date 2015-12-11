package model.client;

import model.utils.ConfigLoader;
import org.apache.log4j.Logger;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.InputStream;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.concurrent.ExecutionException;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

public class Client {
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

    public void start() {
        ClientTaskRunnable clientTaskRunnable = new ClientTaskRunnable(port, ip);
        clientTaskRunnable.setReplyListener(message -> System.out.println(message.receive().toString()));
        clientTaskRunnable.setInputStream(System.in);

        Thread clientThread = new Thread(clientTaskRunnable);
        clientThread.setName("client_thread");
        clientThread.start();
    }

    public void start(int numberOfThreads) throws ExecutionException, InterruptedException, FileNotFoundException {
        ExecutorService executorService = Executors.newFixedThreadPool(numberOfThreads);
        List<String> collectedServerReplies = new ArrayList<>();

        for (int i = 0; i < numberOfThreads; i++) {
            ClientTaskCallable clientTaskCallable = new ClientTaskCallable(port, ip);
            FileInputStream fileInputStream = new FileInputStream("commands.txt");
            clientTaskCallable.setInputStream(fileInputStream);
            collectedServerReplies.addAll(executorService.submit(clientTaskCallable).get());
        }
        System.out.println("After processing a file");
        collectedServerReplies.forEach(System.out::println);
        executorService.shutdown();
    }
}
