package model.processor;

import model.message.Message;
import model.message.ServerTask;
import org.apache.commons.lang3.StringUtils;
import org.apache.log4j.Logger;

import java.io.IOException;
import java.util.Date;

public class ServerProtocolProcessor implements Processable<Message> {
    private static Logger logger = Logger.getLogger(ServerTask.class);

    private static String getInfoMessage(String clientName){

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
        String command = StringUtils.upperCase(message.getMessage().split("#")[0]);

        switch (command) {
            // client has sent a string in the following format REQUEST_INFO#client_name
            case "REQUEST_INFO":
                String clientName = message.getMessage().split("#")[1];
                message.send(ServerProtocolProcessor.getInfoMessage(clientName));
                break;
            case "SERVER_TIME":
                Date date = new Date();
                message.send(date.toString());
                break;
            case "QUIT":
                logger.debug("Client has disconnected: " + message.getSocket().getLocalSocketAddress().toString());
                message.getSocket().close();
            default:
                message.send("server:> " + message.getMessage());
                break;

        }
    }
}
