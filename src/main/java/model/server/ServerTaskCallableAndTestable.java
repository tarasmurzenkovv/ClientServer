package model.server;

import model.ReplyListener;
import model.message.Message;

import java.util.concurrent.Callable;

public class ServerTaskCallableAndTestable extends ServerTask implements Callable<String> {

    public ServerTaskCallableAndTestable(Message message) {
        super(message);
    }
    public ServerTaskCallableAndTestable setReplyListener(ReplyListener replyListener) {
        super.setReplyListener(replyListener);
        return this;
    }

    @Override
    public String call() throws Exception {
        this.process(this.message);
        return this.message.getMessage();
    }
}
