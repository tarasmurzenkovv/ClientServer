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

    protected void process(Message message) throws IOException {
        String m = message.getMessage();
        switch (m) {
            case "REQUEST_INFO":
                message.send(message.getMessage());
                break;
            case "SERVER_TIME":
                message.send(message.getMessage());
                break;
            case "QUIT":
                message.send(message.getMessage());
                try {
                    System.out.println("You have been disconnected");
                    Thread.sleep(40);
                } catch (InterruptedException e) {
                    e.printStackTrace();
                }
            default:
                message.send(message.getMessage());
                break;
        }
        logger.debug("Sent to server a string: " + m);
        this.replyListener.onReply(message);
    }
}
