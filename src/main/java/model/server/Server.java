package model.server;

import model.ReplyListener;
import model.message.Message;
import model.utils.ConfigLoader;
import org.apache.log4j.Logger;

import java.io.File;
import java.io.IOException;
import java.net.ServerSocket;
import java.net.Socket;
import java.net.SocketTimeoutException;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.concurrent.*;

public class Server {
    private static final int NUMBER_OF_SPAWNED_THREADS = 50;
    private static int portNumber;
    private static String hostAddress;
    private static ExecutorService pool;
    private static ServerSocket serverSocket;
    private static Socket socket;
    private static Logger logger = Logger.getLogger(Server.class);

    private static void init(File file) throws IOException {
        Map<String, Object> configs = ConfigLoader.loadXMLConfigsFromFile(file);
        Server.portNumber = (Integer) configs.get("port");
        Server.hostAddress = (String) configs.get("ip");
        Server.serverSocket = new ServerSocket(Server.portNumber);
        Server.pool = Executors.newFixedThreadPool(NUMBER_OF_SPAWNED_THREADS);
    }

    public static void start(File configFile, ReplyListener replyListener) {
        try {
            Server.init(configFile);
            while (true) {
                try {
                    Server.socket = Server.serverSocket.accept();
                    Message message = new Message();
                    message.setSocket(Server.socket);
                    Callable<Void> serverTask = new ServerTaskCallable(message.receive()).setReplyListener(replyListener);
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

    public static List<String> start(File configFile, ReplyListener replyListener, CountDownLatch countDownLatch) {
        List<Future<String>> expectedProcessedMessages = new ArrayList<>();
        List<String> actualServerReplies = new ArrayList<>();
        try {
            Server.init(configFile);
            Server.serverSocket.setSoTimeout(200);
            while (countDownLatch.getCount() != 0) {
                Server.socket = serverSocket.accept();
                Message message = new Message();
                message.setSocket(Server.socket);
                Callable<String> serverTask = new ServerTaskCallableAndTestable(message.receive()).setReplyListener(replyListener);
                Future<String> expectedServerReplyInString = Server.pool.submit(serverTask);
                expectedProcessedMessages.add(expectedServerReplyInString);
            }
        } catch (SocketTimeoutException e) {
            System.out.println("Finished testing");
            Server.pool.shutdown();
        } catch (IOException e) {
            logger.error("Cannot start a server. The server thread will be terminated. Exception: ", e);
            System.exit(0);
        } finally {
            expectedProcessedMessages.forEach(expectedServerReply -> {
                try {
                    String actualServerReply = expectedServerReply.get();
                    actualServerReplies.add(actualServerReply);
                } catch (ExecutionException | InterruptedException e) {
                    e.printStackTrace();
                }
            });
            return actualServerReplies;
        }
    }
}
