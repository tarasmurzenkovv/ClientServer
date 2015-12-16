package model.message;

import org.apache.commons.lang3.StringUtils;

import java.io.*;
import java.net.Socket;


/**
 * Message has the following string format command_name # some_plain_text @ name_of_sender
 */
public class Message {
    private Socket socket;
    private String stringText;

    public String getStringText() {
        return stringText;
    }

    public Socket getSocket() {
        return socket;
    }

    public void setSocket(Socket socket) {
        this.socket = socket;
    }

    public Message setStringMessage(String message) {
        this.stringText = message;
        return this;
    }

    public void setMessageInputSources(InputStream inputStream) throws IOException {
        String s;
        BufferedReader bufferRead = new BufferedReader(new InputStreamReader(inputStream));
        s = bufferRead.readLine();
        this.stringText = s;
    }

    public String getCommand() {
        if (stringText.length() == 1) {
            return stringText;
        }
        return (stringText.indexOf('#') == -1) ? "" : StringUtils.upperCase(stringText.split("#")[0]);
    }

    public String getName() {
        if (stringText.length() == 1) {
            return "";
        }
        return stringText.split("@")[1];
    }

    public String getText() {
        int positionOfAtSign = (stringText.indexOf('@') == -1) ? 0 : stringText.indexOf('@');
        int positionOfSharp = (stringText.indexOf('#') == -1) ? 0 : stringText.indexOf('#');

        if (positionOfAtSign - 1 == positionOfSharp) {
            return "";
        }
        return stringText.substring(positionOfSharp, positionOfAtSign);
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
        return "entered text:" + this.getText() + " entered command: " + this.getCommand() + " entered name: " + this.getName();
    }
}
