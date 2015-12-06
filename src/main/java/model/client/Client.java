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
    private Message message;
    private static Logger logger = Logger.getLogger(Client.class);

    private Client(File configFile) throws IOException {
        try {
            Map<String, Object> configs = ConfigLoader.loadXMLConfigsFromFile(configFile);
            this.portNumber = (Integer) configs.get("port");
            this.serverAddrres = (String) configs.get("ip");

        } catch (SAXException | ParserConfigurationException e) {
            logger.error("Cannot read a config file. The client will exit. Exception " + e.getMessage().toString());
            System.exit(1);
        }
    }

    private static String getStringFromInput(InputStream inputStream) throws IOException {
        String s;
        BufferedReader bufferRead = new BufferedReader(new InputStreamReader(inputStream));
        s = bufferRead.readLine();
        return s;
    }

    @Override
    public void run() {
        try {
            Socket socket = new Socket(serverAddrres, portNumber);
            System.out.print("Enter your name:");
            String enteredName = Client.getStringFromInput(System.in);
            Message message = new Message(socket);
            message.send("INFO#" + enteredName);
            for (int i = 0; i < 6; i++) {
                System.out.println(message.receive());
            }
            while (true) {
                String gotMessageFromConsole = Client.getStringFromInput(System.in);
                if ("quit".equals(gotMessageFromConsole)) {
                    System.out.println("Buy!");
                    break;
                }
                this.message = new Message(socket);
                this.message.send(gotMessageFromConsole);
                System.out.println(this.message.receive());
            }
        } catch (IOException e) {
            logger.error("Exception from client. Exception" + e.getMessage().toString());
        }
    }

    public static void start(File file) {
        try {
            new Thread(new Client(file)).start();
        } catch (IOException e) {
            logger.error("Unable to start a client. Client thread will be terminated. Exception" + e.getMessage().toString());
            System.exit(1);
        }
    }
}
