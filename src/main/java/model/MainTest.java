package model;

import model.client.Client;
import org.apache.log4j.Logger;

import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.util.concurrent.ExecutionException;

public class MainTest {
    private static Logger logger = Logger.getLogger(MainTest.class);
    public static void main(String[] args) throws IOException, ExecutionException, InterruptedException {

        // make sure that current solution works with System.in
      /*  Server server = new Server(new File("config.xml"));
        Thread serverThread = new Thread(server);
        serverThread.start();*/
        new Client(new File("config.xml"), new FileInputStream("commands.txt")).start(4);
    }
}
