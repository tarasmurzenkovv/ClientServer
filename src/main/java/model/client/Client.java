package model.client;

import org.apache.log4j.Logger;

import java.io.File;
import java.io.InputStream;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

public class Client {
    private static Logger logger = Logger.getLogger(Client.class);
    private ExecutorService executorService = Executors.newFixedThreadPool(50);

    public void start(File file, InputStream inputStream) {
    }
}
