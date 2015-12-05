package model.message;

import java.io.*;
import java.net.Socket;

public class Message {
    private Socket socket;

    public Message(Socket socket) {
        this.socket = socket;
    }

    public void send(OutputStream outputStream, String message) throws IOException {
        DataOutputStream dataOutputStream = new DataOutputStream(outputStream);
        dataOutputStream.writeUTF(message);
        dataOutputStream.flush();
    }

    public String receive(InputStream inputStream) throws IOException {
        String data;
        DataInputStream dataInputStream = new DataInputStream(inputStream);
        data = dataInputStream.readUTF();
        return data;
    }
}
