package model.client;

import model.message.Message;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;

public class ClientTaskRunnable extends ClientTask implements Runnable {

    public ClientTaskRunnable(int portNumber, String serverAddrres) {
        super(portNumber, serverAddrres);
    }

    @Override
    public void run() {
        try {
            ClientTaskRunnable clientTaskRunnable = (ClientTaskRunnable) new ClientTask(portNumber, serverAddrres);
            Message message = new Message();
/*
            System.out.print("Enter your name:");
            message.readClientInput(this.inputStream);
            clientTask.setMessage(message);
            message.send("REQUEST_INFO#" + message.getMessage());
            // process "hi" response
            clientTask.setReplyListener(this.replyListener);
            clientTask.process(message);*/
            BufferedReader bufferedInputStream = new BufferedReader(new InputStreamReader(this.inputStream));
            String line = "";
            while ((line = bufferedInputStream.readLine()) != null) {
                System.out.println("Got message from file:" + line);
                //this.replyListener.onReply(message);
                //Thread.yield();
            }

        } catch (IOException e) {
            e.printStackTrace();
        }
    }
}
