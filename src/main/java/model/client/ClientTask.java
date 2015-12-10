package model.client;

import model.message.Message;

import java.io.IOException;
import java.io.InputStream;
import java.net.Socket;
import java.util.concurrent.CountDownLatch;

public class ClientTask {

    protected int portNumber;
    protected String serverAddrres;
    private ReplyListener replyListener;
    protected InputStream inputStream;
    private static Message message;

    public ClientTask(int portNumber, String serverAddrres) {
        this.portNumber = portNumber;
        this.serverAddrres = serverAddrres;
    }

    public void setMessage(Message message) throws IOException {
        ClientTask.message = message;
        ClientTask.message.setSocket(new Socket(this.serverAddrres, this.portNumber));
    }

    public ClientTask setReplyListener(ReplyListener replyListener) {
        this.replyListener = replyListener;
        return this;
    }

    public ClientTask setInputStream(InputStream inputStream) throws IOException {
        this.inputStream = inputStream;
        return this;
    }

    private void process(Message message) throws IOException {
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
