package model.server;

import model.message.MessageProcessor;
import org.apache.log4j.Logger;

import java.io.IOException;
import java.io.InputStream;
import java.net.ServerSocket;
import java.net.Socket;
import java.util.Properties;
import java.util.concurrent.Callable;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

public class Server {
    private final int NUMBER_OF_SPAWNED_THREADS = 50;
    private int portNumber;
    private String hostAddrress;
    private ExecutorService pool;
    private Logger logger = Logger.getLogger(Server.class);

    private Server() {
        Properties properties = new Properties();
        String propFile = "server.properties";
        InputStream inputStream = getClass().getClassLoader().getResourceAsStream(propFile);
        try {
            properties.load(inputStream);
        } catch (IOException e) {
            this.logger.error("Cannot locate a property file. Server won't start.");
            System.exit(1);
        }
        this.portNumber = Integer.parseInt(properties.getProperty("port"));
        this.hostAddrress = properties.getProperty("host");
        this.pool = Executors.newFixedThreadPool(NUMBER_OF_SPAWNED_THREADS);
    }

    public static void start() {
        Server server = new Server();
        server.logger.debug("Started server with the following params: IP: " + server.hostAddrress + " port: " + server.portNumber);
        try {
            ServerSocket serverSocket = new ServerSocket(server.portNumber);
            while (true) {
                try {
                    Socket socket = serverSocket.accept();
                    Callable<Void> messageProcessor = new MessageProcessor(socket);
                    server.pool.submit(messageProcessor);
                } catch (IOException e) {
                    server.logger.error("Unable to process a message from client. Exception: ", e);
                }
            }
        } catch (IOException e) {
            server.logger.error("Cannot start a server. Exception stacktrace", e);
            System.exit(1);
        }
    }
}
