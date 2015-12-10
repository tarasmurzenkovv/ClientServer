package model.client;

import model.utils.ConfigLoader;
import org.apache.log4j.Logger;

import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.Map;
import java.util.concurrent.ExecutionException;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

public class Client implements Runnable {
    private static Logger logger = Logger.getLogger(Client.class);
    private File configs;
    private InputStream inputStream;
    private int port;
    private String ip;
    private int numberOfThreads;

    public Client(File file, InputStream inputStream) {
        this.configs = file;
        this.inputStream = inputStream;
        Map<String, Object> configs = ConfigLoader.loadXMLConfigsFromFile(file);
        this.port = (Integer) configs.get("port");
        this.ip = (String) configs.get("ip");
    }

    public void start() {
        try {
            ClientTaskRunnable c =
                    (ClientTaskRunnable) new ClientTaskRunnable(port, ip)
                    .setInputStream(System.in)
                    .setReplyListener(message -> System.out.println(message.receive().toString()));

            new Thread(c).start();
        } catch (IOException e) {
            logger.error("Exception occurred while reading from System.in. Exception: ", e);
        }
    }

    public void start(int numberOfThreads) throws IOException, ExecutionException, InterruptedException {
        this.numberOfThreads = numberOfThreads;
        ExecutorService executorService = Executors.newFixedThreadPool(numberOfThreads);
        List<String> collectedReplies = Collections.synchronizedList(new ArrayList<>());
        for (int i = 0; i < numberOfThreads; i++) {
            ClientTaskCallable c  = new ClientTaskCallable(port, ip);
            c.setInputStream(new FileInputStream(new File("commands.txt")));
            c.setReplyListener(System.out::println);
            // Collect future results
            collectedReplies.addAll(executorService.submit(c).get());
        }
        // Finished processing commands from a file, call shutdown()
        executorService.shutdown();
        System.out.println("debug");
        collectedReplies.forEach(System.out::println);
    }

    @Override
    public void run() {
        if (this.numberOfThreads != 0) {
            try {
                this.start(this.numberOfThreads);
            } catch (InterruptedException|ExecutionException |IOException e) {
                e.printStackTrace();
            }
        } else {
            this.start();
        }
    }
}
