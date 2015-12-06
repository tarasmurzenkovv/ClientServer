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

    private String getStringFromInput(InputStream inputStream) throws IOException {
        String s;
        BufferedReader bufferRead = new BufferedReader(new InputStreamReader(inputStream));
        s = bufferRead.readLine();
        logger.debug("Got from system.in: " + s);
        return s;
    }

    @Override
    public void run() {
        try {
            while (true) {
                String gotMessageFromConsole = this.getStringFromInput(System.in);
                Socket socket = new Socket(serverAddrres, portNumber);
                this.message = new Message(socket);
                this.message.send(socket.getOutputStream(), gotMessageFromConsole);
                this.message.receive(socket.getInputStream());
            }
        } catch (IOException e) {
            logger.error("Exception from client. Exception" + e.getMessage().toString());
        }
    }

    public static void start(File file) {
        try {
            Client client = new Client(file);
            new Thread(client).start();
        } catch (IOException e) {
            logger.error("Unable to start a client. Client thread will be terminated. Exception" + e.getMessage().toString());
            System.exit(1);
        }
    }
}
