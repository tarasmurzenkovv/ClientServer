package model.message;

import java.net.Socket;
import java.util.concurrent.Callable;

public class MessageProcessor implements Callable<Void> {
    private Socket socket;
    private Message message;

    public MessageProcessor(Socket socket){
        this.socket = socket;
        this.message = new Message(socket);
    }

    @Override
    public Void call() throws Exception {
        String message = this.message.receive(socket.getInputStream());
        this.message.send(socket.getOutputStream(), message);
        return null;
    }
}
