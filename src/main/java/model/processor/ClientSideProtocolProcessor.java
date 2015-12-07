package model.processor;

import model.message.Message;
import model.message.ServerTask;
import org.apache.commons.lang3.StringUtils;
import org.apache.log4j.Logger;

import java.io.IOException;

public class ClientSideProtocolProcessor implements Processable<Message> {
    private static Logger logger = Logger.getLogger(ServerTask.class);

    @Override
    public void process(Message message) throws IOException {
        String stringMessage = message.getMessage();
        // string pattern is the following: COMMAND_NAME#text or COMMAND_NAME
        String command = StringUtils.upperCase(stringMessage.split("#")[0]);
        logger.debug("Got the following for processing: " + command);
        switch (command) {
            // client has sent a string in the following format REQUEST_INFO#client_name
            case "REQUEST_INFO":
                message.send(stringMessage);
                message.receive();
                break;
            case "SERVER_TIME":
                message.send(command);
                break;
            case "QUIT":
                message.send(command);
                System.out.println("You have been disconnected");
                try {
                    Thread.sleep(40);
                } catch (InterruptedException e) {
                    e.printStackTrace();
                } finally {
                    System.exit(0);
                }
            default:
                message.send(stringMessage);
                message.receive();
                break;
        }
    }
}
