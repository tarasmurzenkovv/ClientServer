package model.client;

import model.message.Message;

import java.io.IOException;
import java.io.InputStream;


public class ClientTask {
    protected int portNumber;
    protected String serverAddrres;
    protected ReplyListener replyListener;
    protected InputStream inputStream;

    public ClientTask(int portNumber, String serverAddrres) {
        this.portNumber = portNumber;
        this.serverAddrres = serverAddrres;
    }

    public void setReplyListener(ReplyListener replyListener) {
        this.replyListener = replyListener;
    }
    public void setInputStream(InputStream inputStream){
        this.inputStream = inputStream;
    }


    protected void process(Message message) throws IOException {
        String command = message.getCommand();
        switch (command) {
            case "REQUEST_INFO":
                message.send(message.getMessage());
                break;
            case "SERVER_TIME":
                message.send(command);
                break;
            case "QUIT":
                message.send(command);
                try {
                    System.out.println("You have been disconnected");
                    Thread.sleep(40);
                    System.exit(0);
                } catch (InterruptedException e) {
                    e.printStackTrace();
                }
            default:
                message.send(message.getMessage());
                break;
        }
        this.replyListener.onReply(message);
    }
}
