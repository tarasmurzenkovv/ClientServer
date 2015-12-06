package model.message;

import java.io.*;
import java.net.Socket;

public class Message {
    private Socket socket;

    public Message(Socket socket) {
        this.socket = socket;
    }

    public void send(String message) throws IOException {
        DataOutputStream dataOutputStream = new DataOutputStream(this.socket.getOutputStream());
        dataOutputStream.writeUTF(message);
        dataOutputStream.flush();
    }

    public String receive() throws IOException {
        String data;
        DataInputStream dataInputStream = new DataInputStream(this.socket.getInputStream());
        data = dataInputStream.readUTF();
        return data;
    }
}
