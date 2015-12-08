package model.client;

import model.message.Message;
import model.utils.ConfigLoader;
import org.apache.log4j.Logger;

import java.io.File;
import java.io.IOException;
import java.net.Socket;
import java.util.Map;

public class Client implements Runnable {
    private static int portNumber;
    private static String serverAddrres;
    private ServerReplyListener replyListener = new ServerReplyListener();
    private static Logger logger = Logger.getLogger(Client.class);
    private static Message message;

    private Client(File configFile) {
        Map<String, Object> configs = ConfigLoader.loadXMLConfigsFromFile(configFile);
        Client.portNumber = (Integer) configs.get("port");
        Client.serverAddrres = (String) configs.get("ip");
    }

    public void setMessage(Message message) {
        Client.message = message;
        try {
            Client.message.setSocket(new Socket(Client.serverAddrres, Client.portNumber));
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    private void process(Message message) throws IOException {
        String command = message.getCommand();
        switch (command) {
            case "REQUEST_INFO":
                message.send(message.getMessage());
                break;
            case "SERVER_TIME":
                message.send(command);
                break;
            case "QUIT":
                message.send(command);
                try {
                    System.out.println("You have been disconnected");
                    Thread.sleep(40);
                    System.exit(0);
                } catch (InterruptedException e) {
                    e.printStackTrace();
                }
            default:
                message.send(message.getMessage());
                break;
        }
        this.replyListener.onReply(message);
    }

    @Override
    public void run() {
        try {
            this.process(Client.message);
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    public static void start(File file) {
        try {
            // send a "hi" request
            System.out.print("Enter your name:");
            Client client = new Client(file);
            Message message = new Message();
            message.setMessageFromInputStream(System.in);
            client.setMessage(message);
            message.send("REQUEST_INFO#" + message.getMessage());
            // process "hi" response
            client.replyListener.onReply(message);
            // start a processing the given message in a separate thread.
            while (true) {
                message = new Message();
                message.setMessageFromInputStream(System.in);
                client.setMessage(message);
                Thread thread = new Thread(client);
                thread.start();
            }
        } catch (IOException e) {
            logger.error("Client thread will be terminated. Exception " + e.getStackTrace().toString());
            System.exit(0);
        }
    }
}
