package model.server;

import model.ReplyListener;
import model.message.Message;
import org.apache.commons.lang3.StringUtils;
import org.apache.log4j.Logger;

import java.io.IOException;
import java.util.Date;

public class ServerTask {

    protected Message message;
    protected ReplyListener replyListener;
    private static Logger logger = Logger.getLogger(ServerTaskCallable.class);

    public ServerTask(Message message) {
        this.message = message;
    }

    private static String getInfoMessage(String clientName) {

        return "server:> Successful connection has been established. \n" +
                "server:> Got your name: " + clientName + "\n" +
                "server:> Basic commands: \n" +
                "server:> - get server time - server_time\n" +
                "server:> - disconnect - type quit\n";
    }

    private static String getInfoMessage() {

        return "server:> Basic commands: \n" +
                "server:> - get server time - server_time\n" +
                "server:> - disconnect - type quit\n";
    }

    public ServerTask setReplyListener(ReplyListener replyListener) {
        this.replyListener = replyListener;
        return this;
    }

    public void process(Message message) throws IOException {
        String command = message.getCommand();
        //logger.debug("Got command from client: " + command);
        switch (command) {
            case "REQUEST_INFO":

                if ("REQUEST_INFO".equals(StringUtils.upperCase(message.getCommand()))) {
                    //logger.debug("Sent back to client: " + ServerTask.getInfoMessage());
                    message.send(ServerTask.getInfoMessage(message.getName()));
                } else {
                    message.send(ServerTask.getInfoMessage());
                }
                this.replyListener.onReply(message);
                break;
            case "SERVER_TIME":
                Date date = new Date();
                message.send("server:>" + date.toString());
                this.replyListener.onReply(message);
                break;
            case "QUIT":
                //logger.debug("Client has disconnected: " + message.getSocket().getLocalSocketAddress().toString());
                message.send("You have been disconnected.");
                this.replyListener.onReply(message);
                message.getSocket().close();
                break;
            default:
                //logger.debug("Sending back to a client " + message.getText());
                message.send("server:> " + message.getMessage());
                this.replyListener.onReply(message);
                break;
        }
    }
}
