package model.client;

import model.utils.ConfigLoader;
import org.apache.log4j.Logger;

import java.io.File;
import java.io.InputStream;
import java.util.Map;

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
        new Thread(
                new ClientTask(port, ip)
                        .setInputStream(System.in)
                        .setReplyListener(message -> System.out.println(message.receive().toString())))
                .start();

    }

    public void start(int numberOfThreads) {
        for (int i = 0; i < numberOfThreads+1; i++) {
            new Thread(
                    new ClientTask(port, ip)
                            .setInputStream(System.in)
                            .setReplyListener(message -> System.out.println(message.receive().toString())))
                    .start();
        }
    }
}
