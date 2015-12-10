package model.client;

import java.io.BufferedReader;
import java.io.InputStreamReader;
import java.util.LinkedList;
import java.util.List;
import java.util.concurrent.Callable;


public class ClientTaskCallable extends ClientTask implements Callable<List<String>> {
    private List<String> collected = new LinkedList<>();

    public ClientTaskCallable(int portNumber, String serverAddrres) {
        super(portNumber, serverAddrres);
    }

    @Override
    public List<String> call() throws Exception {
        String line = "";
        BufferedReader bufferedInputStream = new BufferedReader(new InputStreamReader(this.inputStream));
        while ((line = bufferedInputStream.readLine()) != null) {
            System.out.println("Got message from file:" + line);
            //this.replyListener.onReply(message);
            //Thread.yield();
            collected.add(line);
        }
        return collected;
    }
}
