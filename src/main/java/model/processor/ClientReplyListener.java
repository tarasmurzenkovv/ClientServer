package model.processor;

import model.message.Message;

import java.io.IOException;

public class ClientReplyListener implements ReplayListener {
    @Override
    public void onReply(Message message) throws IOException {
        System.out.println("client:> " + message.getMessage());
    }
}
