package model.processor;

import model.message.Message;

import java.io.IOException;

public class ServerReplyListener implements ReplayListener {
    @Override
    public void onReply(Message message) throws IOException {
        System.out.println("server:> " + message.receive().getMessage());
    }
}
