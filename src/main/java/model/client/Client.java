package model.client;

import model.message.Message;
import org.apache.log4j.Logger;

import java.io.*;
import java.net.Socket;

public class Client implements Runnable {
    private int portNumber;
    private String serverAddrres;
    private Message message;
    private Logger logger = Logger.getLogger(Client.class);

    public Client(int portNumber, String serverAddress) {
        this.portNumber = portNumber;
        this.serverAddrres = serverAddress;
    }

    public String getStringFromInput(InputStream inputStream) throws IOException {
        String s;
        BufferedReader bufferRead = new BufferedReader(new InputStreamReader(inputStream));
        s = bufferRead.readLine();
        logger.debug("Got from system.in: " + s);
        return s;
    }
    @Override
    public void run() {
        while (true) {
            try {
                String gotMessageFromConsole = this.getStringFromInput(System.in);
                Socket socket = new Socket(serverAddrres, portNumber);
                this.message = new Message(socket);
                this.message.send(socket.getOutputStream(),gotMessageFromConsole);
                this.message.receive(socket.getInputStream());
            } catch (IOException e) {
                logger.error("Exception from client", e);
            }
        }
    }
}
