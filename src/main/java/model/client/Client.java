package model.client;

import model.message.Message;
import org.apache.log4j.Logger;
import org.w3c.dom.Document;
import org.xml.sax.SAXException;

import javax.xml.parsers.DocumentBuilder;
import javax.xml.parsers.DocumentBuilderFactory;
import javax.xml.parsers.ParserConfigurationException;
import java.io.*;
import java.net.Socket;

public class Client implements Runnable {
    private static final String SERVER_HOST = "ServerHost";
    private static final String SERVER = "server";
    private static final String PORT = "port";
    private int portNumber;
    private String serverAddrres;
    private Message message;
    private static Logger logger = Logger.getLogger(Client.class);

    private Client(File configFile) throws IOException {
        try {
            DocumentBuilderFactory documentBuilderFactory = DocumentBuilderFactory.newInstance();
            DocumentBuilder documentBuilder = documentBuilderFactory.newDocumentBuilder();
            Document document = documentBuilder.parse(configFile);
            String stringPortNumber = document.getElementsByTagName(SERVER).item(0).getAttributes().getNamedItem(PORT).getNodeValue();
            this.portNumber = Integer.parseInt(stringPortNumber);
            this.serverAddrres = document.getElementsByTagName(SERVER_HOST).item(0).getChildNodes().item(0).getNodeValue();
        } catch (SAXException | ParserConfigurationException e) {
            logger.error("Cannot read a config configFile. The client will exit. Exception stacktrace", e);
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
            while (true){
                String gotMessageFromConsole = this.getStringFromInput(System.in);
                Socket socket = new Socket(serverAddrres, portNumber);
                this.message = new Message(socket);
                this.message.send(socket.getOutputStream(), gotMessageFromConsole);
                this.message.receive(socket.getInputStream());
            }
        } catch (IOException e) {
            logger.error("Exception from client", e);
        }
    }

    public static void start(File file){
        try {
            Client client = new Client(file);
            new Thread(client).start();
        } catch (IOException e) {
            logger.error("Unable to start a client. Client thread will be terminated. Exception: ", e);
            System.exit(1);
        }
    }
}
