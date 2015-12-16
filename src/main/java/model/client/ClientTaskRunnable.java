package model.client;

import model.message.Message;

import java.io.IOException;
import java.net.Socket;

public class ClientTaskRunnable extends ClientTask implements Runnable {
    public ClientTaskRunnable(int portNumber, String serverAddrres) {
        super(portNumber, serverAddrres);
    }

    @Override
    public void run() {
        try {
            Message message = new Message();
            System.out.print("Enter your name:");
            Socket socket = new Socket(this.serverAddress, this.portNumber);
            message.setMessageInputSources(this.inputStream);
            message.setSocket(socket);
            message.send("REQUEST_INFO#@" + message.getMessage());
            this.replyListener.onReply(message);
            // process "hi" response
            while (true) {
                message = new Message();
                socket = new Socket(this.serverAddress, this.portNumber);
                message.setSocket(socket);
                message.setMessageInputSources(this.inputStream);
                this.process(message);
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
}
