package model.message;

import java.io.DataInputStream;
import java.io.DataOutputStream;
import java.io.IOException;
import java.net.Socket;

public class Message {
    private Socket socket;
    private String message;

    public Message(Socket socket) {
        this.socket = socket;
    }

    public String getMessage() {
        return message;
    }

    public Message setMessage(String message) {
        this.message = message;
        return this;
    }

    public Socket getSocket() {
        return socket;
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
