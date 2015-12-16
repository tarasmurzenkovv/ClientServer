package model.client;

import model.utils.ConfigLoader;
import modeltest.ClientTaskCallable;
import org.apache.log4j.Logger;

import java.io.*;
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

    public Client(File file, InputStream inputStream) {
        this.inputStream = inputStream;
        Map<String, Object> configs = ConfigLoader.loadXMLConfigsFromFile(file);
        this.port = (Integer) configs.get("port");
        this.ip = (String) configs.get("ip");
    }

    public void start() {
        ClientTaskRunnable clientTaskRunnable = new ClientTaskRunnable(port, ip);
        clientTaskRunnable.setReplyListener(message -> System.out.println(message.receive().toString()));
        clientTaskRunnable.setInputStream(this.inputStream);

        Thread clientThread = new Thread(clientTaskRunnable);
        clientThread.setName("client_thread");
        clientThread.start();
    }

    private InputStream[] forkAGivenStream(InputStream inputStream, int numberOfThreads) throws IOException {
        int read;
        byte[] bytes = new byte[1024];
        InputStream[] streams = new InputStream[numberOfThreads];
        ByteArrayOutputStream bos = new ByteArrayOutputStream();

        while ((read = inputStream.read(bytes)) != -1) {
            bos.write(bytes, 0, read);
        }
        byte[] ba = bos.toByteArray();

        for (int i = 0; i < numberOfThreads; i++) {
            streams[i] = new ByteArrayInputStream(ba);
        }
        return streams;
    }

    public List<String> start(int numberOfThreads, CountDownLatch countDownLatch) {
        ExecutorService executorService = Executors.newFixedThreadPool(numberOfThreads);
        List<String> collectedServerReplies = new ArrayList<>();
        try {
            InputStream[] streams = this.forkAGivenStream(this.inputStream, numberOfThreads);
            for (int i = 0; i < numberOfThreads; i++) {
                ClientTaskCallable clientTaskCallable = new ClientTaskCallable(port, ip);
                clientTaskCallable.setInputStream(streams[i]);
                collectedServerReplies.addAll(executorService.submit(clientTaskCallable).get());
                countDownLatch.countDown();
            }
        } catch (ExecutionException | IOException e) {
            logger.error("Exception occurred while processing a file with commands. Exception: ", e);
        } catch (InterruptedException e) {
            Thread.currentThread().interrupt();
        }
        collectedServerReplies.forEach(System.out::println);
        executorService.shutdown();
        return collectedServerReplies;
    }
}
