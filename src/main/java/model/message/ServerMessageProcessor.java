package model.message;

import org.apache.log4j.Logger;

import java.net.Socket;
import java.util.Date;
import java.util.concurrent.Callable;

public class ServerMessageProcessor implements Callable<Void> {
    private Socket socket;
    private Message message;
    private static Logger logger = Logger.getLogger(ServerMessageProcessor.class);

    public ServerMessageProcessor(Socket socket) {
        this.socket = socket;
        this.message = new Message(socket);
    }

    @Override
    public Void call() throws Exception {
        String receivedString = this.message.receive();
        String command = receivedString.split("#")[0];
        Message welcomeMessage = new Message(socket);

        switch (command) {
            case "INFO":
                String name = receivedString.split("#")[1];
                welcomeMessage.send("server:> Successful connection has been established. ");
                welcomeMessage.send("server:> Got your name: " + name);
                welcomeMessage.send("server:> Basic commands: ");
                welcomeMessage.send("server:> - get server time - server_time");
                welcomeMessage.send("server:> - send file to server - -file full_path_to_file");
                welcomeMessage.send("server:> - number of connected clients - client_no");
                welcomeMessage.send("server:> - disconnect - type quit");
            case "server_time":
                Date date = new Date();
                welcomeMessage.send("server:> " + date.toString());
            case "quit":
                logger.debug("Client has disconnected: " + socket.getLocalSocketAddress().toString());
            default:
                welcomeMessage.send("server:> " + receivedString);

        }

        return null;
    }
}
