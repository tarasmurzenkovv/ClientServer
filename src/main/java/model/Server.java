package model;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.PrintWriter;
import java.net.ServerSocket;
import java.net.Socket;
import java.util.Queue;
import java.util.concurrent.ConcurrentLinkedDeque;

public class Server {
    private int portNumber;
    private String hostAddrress;
    private Socket socket;
    private PrintWriter printWriter;
    private BufferedReader bufferedReader;
    private Queue<String> messages = new ConcurrentLinkedDeque<>();

    Logger logger = LoggerFactory.getLogger(Server.class);

    public Server(int portNumber, String hostAddress) {
        this.portNumber = portNumber;
        this.hostAddrress = hostAddress;

        try (ServerSocket serverSocket = new ServerSocket(portNumber);
             Socket clientSocket = serverSocket.accept();
             PrintWriter out = new PrintWriter(clientSocket.getOutputStream(), true);
             BufferedReader in = new BufferedReader(new InputStreamReader(clientSocket.getInputStream()))) {

            this.printWriter = out;
            this.bufferedReader = in;

        } catch (IOException e) {
            logger.error("Exception occurred while opening a socket connection. Exception: ", e);
        }
    }

    public void start(){
        while (true){

        }
    }
}
