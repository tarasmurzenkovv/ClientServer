package model.message;

import org.apache.commons.lang3.StringUtils;

import java.io.*;
import java.net.Socket;


/**
 * Message has the following string format command_name # some_plain_text @ name_of_sender
 */
public class Message {
    private Socket socket;
    private String message;

    public void setSocket(Socket socket) {
        this.socket = socket;
    }

    public String getMessage() {
        return message;
    }

    public Socket getSocket() {
        return socket;
    }

    public void setStringMessage(String message) {
        this.message = message;
    }

    public void setMessageInputSources(InputStream inputStream) throws IOException {
        String s;
        BufferedReader bufferRead = new BufferedReader(new InputStreamReader(inputStream));
        s = bufferRead.readLine();
        this.message = s;
    }

    public String getCommand() {
        return (message.indexOf('#') == -1) ? "" : StringUtils.upperCase(message.split("#")[0]);
    }

    public String getName() {
        return message.split("@")[1];
    }

    public String getText() {
        int positionOfAtSign = (message.indexOf('@') == -1) ? 0 : message.indexOf('@');
        int positionOfSharp = (message.indexOf('#') == -1) ? 0 : message.indexOf('#');

        if(positionOfAtSign == positionOfSharp){
            return "";
        }
        return message.substring(positionOfSharp + 1, positionOfAtSign);
    }

    public void send(String message) throws IOException {
        DataOutputStream dataOutputStream = new DataOutputStream(this.socket.getOutputStream());
        dataOutputStream.writeUTF(message);
        dataOutputStream.flush();
        this.setStringMessage(message);
    }

    public Message receive() throws IOException {
        String data;
        DataInputStream dataInputStream = new DataInputStream(this.socket.getInputStream());
        data = dataInputStream.readUTF();
        this.setStringMessage(data);
        return this;
    }

    @Override
    public String toString() {
        return this.message;
    }
}
