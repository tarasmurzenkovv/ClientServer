package model.client;

import model.message.Message;
import model.processor.ClientSideProtocolProcessor;
import model.processor.ReplyListener;
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

    private static String getStringFromInput(InputStream inputStream) throws IOException {
        String s;
        BufferedReader bufferRead = new BufferedReader(new InputStreamReader(inputStream));
        s = bufferRead.readLine();
        return s;
    }

    @Override
    public void run() {
        try {
            System.out.print("Enter your name:");
            String enteredName = Client.getStringFromInput(System.in);
            Socket socket = new Socket(this.serverAddrres, this.portNumber);
            Message message = new Message(socket);
            message.setMessage("REQUEST_INFO#" + enteredName);
            ClientSideProtocolProcessor clientSideProtocolProcessor = new ClientSideProtocolProcessor();
            clientSideProtocolProcessor.setOnReplyListener(new ReplyListener());
            clientSideProtocolProcessor.process(message);
            while (true) {
                String gotMessageFromConsole = Client.getStringFromInput(System.in);
                socket = new Socket(serverAddrres,portNumber);
                message = new Message(socket);
                message.setMessage(gotMessageFromConsole);
                clientSideProtocolProcessor.process(message);
            }
        } catch (IOException e) {
            logger.error("Exception from client. Exception" + e.getMessage());
        }
    }

    public static void start(File file) {
        try {
            new Thread(new Client(file)).start();
        } catch (IOException e) {
            logger.error("Unable to start a client. Client thread will be terminated. Exception" + e.getMessage());
            System.exit(0);
        }
    }
}
