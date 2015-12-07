package model.client;

import model.message.Message;
import model.utils.ConfigLoader;
import org.apache.log4j.Logger;
import org.xml.sax.SAXException;

import javax.xml.parsers.ParserConfigurationException;
import java.io.*;
import java.net.Socket;
import java.util.Map;

public class Client implements Runnable {
    private int portNumber;
    private String serverAddrres;
    private static ServerReplyListener replyListener = new ServerReplyListener();
    private static Logger logger = Logger.getLogger(Client.class);

    private Client(File configFile) throws IOException {
        try {
            Map<String, Object> configs = ConfigLoader.loadXMLConfigsFromFile(configFile);
            this.portNumber = (Integer) configs.get("port");
            this.serverAddrres = (String) configs.get("ip");

        } catch (SAXException | ParserConfigurationException e) {
            logger.error("Cannot read a config file. The client will exit. Exception " + e.getMessage());
            System.exit(0);
        }
    }

    @Override
    public void run() {
        try {
            Socket socket = new Socket(serverAddrres, portNumber);
            Message message = new Message(socket);
            message.setMessage(System.in);
            logger.debug("Got the following for processing: " + message.getCommand());
            switch (message.getCommand()) {
                // client has sent a string in the following format REQUEST_INFO#client_name
                case "REQUEST_INFO":
                    message.send(message.getMessage());
                    break;
                case "SERVER_TIME":
                    message.send("SERVER_TIME");
                    break;
                case "QUIT":
                    message.send("QUIT");
                    try {
                        System.out.println("You have been disconnected");
                        Thread.sleep(40);
                    } catch (InterruptedException e) {
                        e.printStackTrace();
                    } finally {
                        System.exit(0);
                    }
                default:
                    message.send(message.getMessage());
                    break;
            }

        } catch (IOException e) {
            logger.error("Exception from client. Exception" + e.getMessage());
        }
    }

    public void start(File file) {
        try {
            System.out.print("Enter your name:");
            Client client = new Client(file);
            Message message = new Message(new Socket(serverAddrres,portNumber));
            message.setMessage(System.in);
            replyListener.onReply(message);
            while (true) {
                new Thread(client).start();
                replyListener.onReply(message   );
            }
        } catch (IOException e) {
            logger.error("Client thread will be terminated. Exception" + e.getMessage());
            System.exit(0);
        }
    }
}
