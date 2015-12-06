package model.server;

import model.message.MessageProcessor;
import org.apache.log4j.Logger;
import org.w3c.dom.Document;
import org.xml.sax.SAXException;

import javax.xml.parsers.DocumentBuilder;
import javax.xml.parsers.DocumentBuilderFactory;
import javax.xml.parsers.ParserConfigurationException;
import java.io.File;
import java.io.IOException;
import java.net.ServerSocket;
import java.net.Socket;
import java.util.concurrent.Callable;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

public class Server {
    private static final String SERVER_HOST = "ServerHost";
    private static final String SERVER = "server";
    private static final String PORT = "port";
    private final int NUMBER_OF_SPAWNED_THREADS = 50;
    private int portNumber;
    private String hostAddress;
    private ExecutorService pool;
    private static Logger logger = Logger.getLogger(Server.class);

    private Server(File file) throws IOException {
        initServerParametersFromConfigFile(file);
    }

    private void initServerParametersFromConfigFile(File configFile) throws IOException {
        try {
            DocumentBuilderFactory documentBuilderFactory = DocumentBuilderFactory.newInstance();
            DocumentBuilder documentBuilder = documentBuilderFactory.newDocumentBuilder();
            Document document = documentBuilder.parse(configFile);
            String stringPortNumber = document.getElementsByTagName(SERVER).item(0).getAttributes().getNamedItem(PORT).getNodeValue();
            this.portNumber = Integer.parseInt(stringPortNumber);
            this.hostAddress = document.getElementsByTagName(SERVER_HOST).item(0).getChildNodes().item(0).getNodeValue();
            this.pool = Executors.newFixedThreadPool(NUMBER_OF_SPAWNED_THREADS);
        } catch (SAXException | ParserConfigurationException e) {
            logger.error("Cannot read a config configFile. The sever will exit. Exception stacktrace", e);
            System.exit(1);
        }
    }

    public static void start(File configFile) {
        try {
            Server server = new Server(configFile);
            logger.debug("Server started with the following params: IP: " + server.hostAddress + " port: " + server.portNumber);
            ServerSocket serverSocket = new ServerSocket(server.portNumber);
            while (true) {
                try {
                    Socket socket = serverSocket.accept();
                    logger.debug("Client has connected: " + socket.getLocalSocketAddress().toString());
                    Callable<Void> messageProcessor = new MessageProcessor(socket);
                    server.pool.submit(messageProcessor);
                } catch (IOException e) {
                    logger.error("Unable to process a message from client. Exception: ", e);
                }
            }
        } catch (IOException e) {
            logger.error("Cannot start a server. The sever will exit. Exception stacktrace", e);
            System.exit(1);
        }
    }
}
