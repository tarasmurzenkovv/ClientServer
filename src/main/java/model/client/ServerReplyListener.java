package model.client;

import model.message.Message;

import java.io.IOException;

public class ServerReplyListener {
    public void onReply(Message message) throws IOException {
        System.out.println(message.receive().getMessage());
    }
}
