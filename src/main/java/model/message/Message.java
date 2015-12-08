package model.message;

import org.apache.commons.lang3.StringUtils;

import java.io.*;
import java.net.Socket;

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

    public void setMessage(String message) {
        this.message = message;
    }

    public void setMessage(InputStream inputStream) throws IOException {
        String s;
        BufferedReader bufferRead = new BufferedReader(new InputStreamReader(inputStream));
        s = bufferRead.readLine();
        this.message = s;
    }

    public String getCommand(){
        String stringMessage = this.getMessage();
        return StringUtils.upperCase(stringMessage.split("#")[0]);
    }

    public void send(String message) throws IOException {
        DataOutputStream dataOutputStream = new DataOutputStream(this.socket.getOutputStream());
        dataOutputStream.writeUTF(message);
        this.setMessage(message);
        dataOutputStream.flush();
    }

    public Message receive() throws IOException {
        String data;
        DataInputStream dataInputStream = new DataInputStream(this.socket.getInputStream());
        data = dataInputStream.readUTF();
        this.setMessage(data);
        return this;
    }

    @Override
    public String toString() {
        return this.message;
    }
}
