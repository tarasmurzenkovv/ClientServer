package model.message;

import org.apache.commons.lang3.StringUtils;
import org.apache.log4j.Logger;

import java.io.*;
import java.net.Socket;

public class Message {
    private Socket socket;
    private String message;
    private static Logger logger = Logger.getLogger(Message.class);

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

    public synchronized String getCommand() {
        String stringMessage = this.getMessage();
        return StringUtils.upperCase(stringMessage.split("#")[0]);
    }

    public void send(String message) {
        try (DataOutputStream dataOutputStream = new DataOutputStream(this.socket.getOutputStream())) {
            dataOutputStream.writeUTF(message);
            dataOutputStream.flush();
        } catch (IOException e) {
            logger.error("Exception occurred while sending a message. Exception: ", e);
        }
        this.setStringMessage(message);
    }

    public Message receive() {
        String data;
        try (DataInputStream dataInputStream = new DataInputStream(this.socket.getInputStream())) {
            data = dataInputStream.readUTF();
            this.setStringMessage(data);
        } catch (IOException e) {
            logger.error("Exception occurred while receiving a message. Exception: ", e);
        }
        return this;
    }

    @Override
    public String toString() {
        return this.message;
    }
}
