package model.client;

import model.message.Message;

import java.io.BufferedReader;
import java.io.InputStreamReader;
import java.net.Socket;
import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.Callable;

public class ClientTaskCallable extends ClientTask implements Callable<List<String>> {
    public ClientTaskCallable(int portNumber, String serverAddrres) {
        super(portNumber, serverAddrres);
    }

    @Override
    public List<String> call() throws Exception {
        List<String> serverReplies = new ArrayList<>();
        String fileLine;
        BufferedReader bufferedReader = new BufferedReader(new InputStreamReader(this.inputStream));
        super.setReplyListener(message->serverReplies.add(message.receive().getMessage()));

        while((fileLine = bufferedReader.readLine())!=null){
            System.out.println(fileLine);
            Message message = new Message();
            Socket socket = new Socket(serverAddrres, portNumber);
            message.setSocket(socket);
            message.setMessageInputSources(fileLine);
            super.process(message);
        }
        return  serverReplies;
    }
}
