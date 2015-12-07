package model.processor;

import model.message.Message;

import java.io.IOException;

/**
 * Created by tmurzenkov on 12/7/2015.
 */
public interface ReplayListener {
    void onReply(Message message) throws IOException;
}
