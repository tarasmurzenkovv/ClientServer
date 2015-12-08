package model.client;

import model.message.Message;

import java.io.IOException;
import java.net.Socket;

public class ClientTask implements Runnable {
    private int portNumber;
    private String serverAddrres;
    private ReplyListener replyListener;
    private static Message message;

    public ClientTask(int portNumber, String serverAddrres) {
        this.portNumber = portNumber;
        this.serverAddrres = serverAddrres;
    }

    public void setMessage(Message message) throws IOException {
        ClientTask.message = message;
        ClientTask.message.setSocket(new Socket(this.serverAddrres, this.portNumber));
    }

    public void setReplyListener(ReplyListener replyListener) {
        this.replyListener = replyListener;
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
            System.out.print("Enter your name:");
            ClientTask clientTask = new ClientTask(portNumber, serverAddrres);
            Message message = new Message();
            message.setMessage(System.in);
            clientTask.setMessage(message);
            message.send("REQUEST_INFO#" + message.getMessage());
            // process "hi" response
            clientTask.setReplyListener(m-> System.out.println(m.receive().toString()));
            // start a processing the given message in a separate thread.
            while (true) {
                message = new Message();
                message.setMessage(System.in);
                this.process(message);
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
}
