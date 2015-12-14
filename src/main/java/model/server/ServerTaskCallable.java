package model.server;

import model.ReplyListener;
import model.message.Message;

import java.util.concurrent.Callable;

public class ServerTaskCallable extends ServerTask implements Callable<Void> {
    public ServerTaskCallable(Message message) {
        super(message);
    }

    public ServerTaskCallable setReplyListener(ReplyListener replyListener) {
        super.setReplyListener(replyListener);
        return this;
    }

    @Override
    public Void call() throws Exception {
        this.process(this.message);
        return null;
    }
}
