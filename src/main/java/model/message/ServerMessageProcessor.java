package model.message;

import java.net.Socket;
import java.util.Date;
import java.util.concurrent.Callable;

public class ServerMessageProcessor implements Callable<Void> {
    private Socket socket;
    private Message message;

    public ServerMessageProcessor(Socket socket) {
        this.socket = socket;
        this.message = new Message(socket);
    }

    @Override
    public Void call() throws Exception {
        String message = this.message.receive();
        String command = message.split("#")[0];
        Message welcomeMessage = new Message(socket);

        switch (command) {
            case "INFO":
                String name = message.split("#")[1];
                welcomeMessage.send("server:> Successful connection has been established. ");
                welcomeMessage.send("server:> Got your name: " + name);
                welcomeMessage.send("server:> Basic commands: ");
                welcomeMessage.send("server:> - get server time - server_time");
                welcomeMessage.send("server:> - send file to server - -file full_path_to_file");
                welcomeMessage.send("server:> - disconnect - type quit");
            case "server_time":
                Date date = new Date();
                welcomeMessage.send(date.toString());
            default:

        }

        return null;
    }
}
