package model.message;

import model.processor.Processable;

import java.util.concurrent.Callable;

public class ServerTask implements Callable<Void> {
    private Message message;
    private Processable processable;


    public ServerTask(Message message) {
        this.message = message;
    }
    public ServerTask registerAProtocolProcessor(Processable processable){
        this.processable = processable;
        return this;
    }

    @Override
    public Void call() throws Exception {
        processable.process(this.message);
        return null;
    }
}
