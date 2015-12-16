package modeltest;

import model.client.ClientTask;
import model.message.Message;

import java.io.BufferedReader;
import java.io.InputStreamReader;
import java.net.Socket;
import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.Callable;

public class ClientTaskCallable extends ClientTask implements Callable<List<String>> {
    public ClientTaskCallable(int portNumber, String serverAddress) {
        super(portNumber, serverAddress);
    }

    @Override
    public List<String> call() throws Exception {
        List<String> serverReplies = new ArrayList<>();
        String fileLine;
        BufferedReader bufferedReader = new BufferedReader(new InputStreamReader(super.inputStream));
        this.setReplyListener(message -> serverReplies.add(message.receive().getMessage()));

        while ((fileLine = bufferedReader.readLine()) != null) {
            Message message = new Message();
            Socket socket = new Socket(this.serverAddress, this.portNumber);
            message.setSocket(socket);
            message.setStringMessage(fileLine);
            this.process(message);
        }
        return serverReplies;
    }
}
