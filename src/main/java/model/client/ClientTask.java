package model.client;

import model.message.Message;
import org.apache.log4j.Logger;

import java.io.IOException;
import java.io.InputStream;

public class ClientTask {
    protected int portNumber;
    protected String serverAddress;
    protected ReplyListener replyListener;
    protected InputStream inputStream;
    private static Logger logger = Logger.getLogger(ClientTask.class);

    public ClientTask(int portNumber, String serverAddress) {
        this.portNumber = portNumber;
        this.serverAddress = serverAddress;
    }

    public void setReplyListener(ReplyListener replyListener) {
        this.replyListener = replyListener;
    }

    public void setInputStream(InputStream inputStream) {
        this.inputStream = inputStream;
    }

    protected void process(Message message) {
        String command = message.getCommand();
        try {
            switch (command) {
                case "REQUEST_INFO":
                    message.send(message.getMessage());
                    break;
                case "SERVER_TIME":
                    message.send(message.getMessage());
                    break;
                case "QUIT":
                    message.send(message.getMessage());
                    try {
                        System.out.println("Buy! ");
                        Thread.sleep(100);
                    } catch (InterruptedException e) {
                        logger.error("Exception occurred from 'process' method. Exception ", e);
                    }
                    break;
                default:
                    message.send(message.getMessage());
                    break;
            }
            logger.debug("Sent to server a string: " + command);
            this.replyListener.onReply(message);
        } catch (IOException e) {
            logger.error("Exception occurred from 'process' method. Exception ", e);
        }
    }
}
