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

    public void setMessage(String message) {
        this.message = message;
    }

    public Socket getSocket() {
        return socket;
    }

    public void readClientInput(String message) {
        this.message = message;
    }

    public synchronized void readClientInput(InputStream inputStream) throws IOException {

        String s;
        BufferedReader bufferRead = new BufferedReader(new InputStreamReader(inputStream));
        s = bufferRead.readLine();
        System.out.println("got message from file: " + s);
        this.message = s;
    }

    public String getCommand() {
        return StringUtils.upperCase(message.split("#")[0]);
    }

    public synchronized void send(String message) {
        try (DataOutputStream dataOutputStream = new DataOutputStream(this.socket.getOutputStream())){
            dataOutputStream.writeUTF(message);
            dataOutputStream.flush();
        }
        catch(IOException e){
        }
    }

    public synchronized Message receive() {
        try (DataInputStream dataInputStream = new DataInputStream(this.socket.getInputStream())) {
            String data = dataInputStream.readUTF();
            this.setMessage(data);
        } catch (IOException e) {

        }
        return this;
    }

    @Override
    public String toString() {
        return this.message;
    }
}
