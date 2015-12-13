package model.server;

import model.client.ReplyListener;
import model.message.Message;
import model.utils.ConfigLoader;
import org.apache.log4j.Logger;

import java.io.File;
import java.io.IOException;
import java.net.ServerSocket;
import java.net.Socket;
import java.net.SocketTimeoutException;
import java.util.Map;
import java.util.concurrent.Callable;
import java.util.concurrent.CountDownLatch;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

public class Server {
    private static int portNumber;
    private static String hostAddress;
    private static ExecutorService pool;
    private static ServerSocket serverSocket;
    private static Socket socket;

    private static final int NUMBER_OF_SPAWNED_THREADS = 50;
    private static Logger logger = Logger.getLogger(Server.class);

    private static void init(File file) throws IOException {
        Map<String, Object> configs = ConfigLoader.loadXMLConfigsFromFile(file);
        Server.portNumber = (Integer) configs.get("port");
        Server.hostAddress = (String) configs.get("ip");
        Server.serverSocket = new ServerSocket(Server.portNumber);
        Server.serverSocket.setSoTimeout(200);
        Server.pool = Executors.newFixedThreadPool(NUMBER_OF_SPAWNED_THREADS);
    }

    public static void start(File configFile, ReplyListener replyListener, CountDownLatch countDownLatch) {
        try {
            Server.init(configFile);
            while (countDownLatch.getCount() != 0) {
                try {
                    Server.socket = serverSocket.accept();
                    Message message = new Message();
                    message.setSocket(Server.socket);
                    Callable<Void> serverTask = new ServerTask(message.receive()).setReplyListener(replyListener);
                    Server.pool.submit(serverTask);
                } catch (SocketTimeoutException e){
                    System.out.println("Finished testing");
                }
            }
            Server.socket.close();
            serverSocket.close();
            Server.pool.shutdown();
        } catch (IOException e) {
            logger.error("Cannot start a server. The server thread will be terminated. Exception: ", e);
            System.exit(0);
        }
    }

    public static void start(File configFile, ReplyListener replyListener) {
        try {
            Server.init(configFile);
            while (true) {
                try {
                    Server.socket = Server.serverSocket.accept();
                    Message message = new Message();
                    message.setSocket(Server.socket);
                    Callable<Void> serverTask = new ServerTask(message.receive()).setReplyListener(replyListener);
                    Server.pool.submit(serverTask);
                } catch (IOException e) {
                    logger.error("Unable to process a message from client. Exception: ", e);
                }
            }
        } catch (IOException e) {
            logger.error("Cannot start a server. The sever will exit. Exception: ", e);
            System.exit(0);
        }
    }
}
