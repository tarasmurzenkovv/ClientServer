package model;

import model.message.Message;

import java.io.IOException;

public interface ReplyListener {
    void onReply(Message message) throws IOException;
    //dsfgsdfg
}
