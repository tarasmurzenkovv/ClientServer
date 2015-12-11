package model.server;

import model.message.Message;
import model.utils.ConfigLoader;
import org.apache.log4j.Logger;

import java.io.File;
import java.io.IOException;
import java.net.ServerSocket;
import java.net.Socket;
import java.util.Map;
import java.util.concurrent.Callable;
import java.util.concurrent.CountDownLatch;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

public class Server {
    private int portNumber;
    private String hostAddress;
    private ExecutorService pool;
    private static Socket socket;

    private static final int NUMBER_OF_SPAWNED_THREADS = 50;
    private static Logger logger = Logger.getLogger(Server.class);

    private Server(File file) throws IOException {
        Map<String, Object> configs = ConfigLoader.loadXMLConfigsFromFile(file);
        this.portNumber = (Integer) configs.get("port");
        this.hostAddress = (String) configs.get("ip");
        this.pool = Executors.newFixedThreadPool(NUMBER_OF_SPAWNED_THREADS);
    }

    public static void start(File configFile, CountDownLatch countDownLatch) {
        try {
            Server server = new Server(configFile);
            ServerSocket serverSocket = new ServerSocket(server.portNumber);
            while (true) {
                try {
                    if(countDownLatch.getCount() == 0){
                        server.pool.shutdown();
                    }
                    Server.socket = serverSocket.accept();
                    Message message = new Message();
                    message.setSocket(Server.socket);
                    System.out.println("number of thread: " + countDownLatch.getCount());
                    Callable<Void> serverTask = new ServerTask(message.receive());
                    server.pool.submit(serverTask);
                } catch (IOException e) {
                    logger.error("Unable to process a message from client. Exception: ", e);
                }
            }
        } catch (IOException e) {
            logger.error("Cannot start a server. The sever will exit. Exception stacktrace", e);
            System.exit(0);
        }
    }

    public static void start(File configFile) {
        try {
            Server server = new Server(configFile);
            ServerSocket serverSocket = new ServerSocket(server.portNumber);
            while (true) {
                try {
                    Server.socket = serverSocket.accept();
                    Message message = new Message();
                    message.setSocket(Server.socket);
                    Callable<Void> serverTask = new ServerTask(message.receive());
                    server.pool.submit(serverTask);
                } catch (IOException e) {
                    logger.error("Unable to process a message from client. Exception: ", e);
                }
            }
        } catch (IOException e) {
            logger.error("Cannot start a server. The sever will exit. Exception stacktrace", e);
            System.exit(0);
        }
    }
}
