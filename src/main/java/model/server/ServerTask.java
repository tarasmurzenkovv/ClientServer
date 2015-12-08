package model.server;

import model.message.Message;
import org.apache.log4j.Logger;

import java.io.IOException;
import java.util.Date;
import java.util.concurrent.Callable;

public class ServerTask implements Callable<Void> {
    private Message message;
    private static Logger logger = Logger.getLogger(ServerTask.class);

    public ServerTask(Message message) {
        this.message = message;
    }

    private static String getInfoMessage(String clientName) {

        return "server:> Successful connection has been established. \n" +
                "server:> Got your name: " + clientName + "\n" +
                "server:> Basic commands: \n" +
                "server:> - get server time - server_time\n" +
                "server:> - send file to server - -file full_path_to_file\n" +
                "server:> - number of connected clients - client_no\n" +
                "server:> - disconnect - type quit\n";
    }

    public void process(Message message) throws IOException {
        // string pattern is the following: COMMAND_NAME#text or COMMAND_NAME
        String command = message.getCommand();
        logger.debug("Got from client: " + command);
        switch (command) {
            // client has sent a string in the following format REQUEST_INFO#client_name
            case "REQUEST_INFO":
                String clientName = message.getMessage().split("#")[1];
                logger.debug("REQUEST_INFO, send it back");
                message.send(ServerTask.getInfoMessage(clientName));
                break;
            case "SERVER_TIME":
                Date date = new Date();
                message.send("server:>" + date.toString());
                logger.debug("SERVER_TIME send it back");
                break;
            case "QUIT":
                logger.debug("Client has disconnected: " + message.getSocket().getLocalSocketAddress().toString());
                message.getSocket().close();
                break;
            default:
                message.send("server:> " + message.getMessage());
                break;
        }
    }

    @Override
    public Void call() throws Exception {
        this.process(this.message);
        return null;
    }
}
