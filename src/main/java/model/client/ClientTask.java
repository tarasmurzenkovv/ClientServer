package model.client;

import model.message.Message;

import java.io.IOException;
import java.io.InputStream;
import java.net.Socket;

public class ClientTask implements Runnable {
    private int portNumber;
    private String serverAddrres;
    private ReplyListener replyListener;
    private InputStream inputStream;
    private static Message message;
    private Socket socket;

    public ClientTask(int portNumber, String serverAddrres) {
        this.portNumber = portNumber;
        this.serverAddrres = serverAddrres;
    }

    public void setMessage(Message message) throws IOException {
        ClientTask.message = message;
    }

    public ClientTask setReplyListener(ReplyListener replyListener) {
        this.replyListener = replyListener;
        return this;
    }
    public ClientTask setInputStream(InputStream inputStream){
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

    @Override
    public void run() {
        try {
            Message message = new Message();

            System.out.print("Enter your name:");
            message.setMessage(this.inputStream);
            message.setSocket(new Socket(this.serverAddrres, this.portNumber));
            message.send("REQUEST_INFO#" + message.getMessage());
            this.replyListener.onReply(message);
            // process "hi" response
            while (true) {
                message = new Message();
                message.setSocket(new Socket(this.serverAddrres, this.portNumber));
                message.setMessage(this.inputStream);
                this.process(message);
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
}
