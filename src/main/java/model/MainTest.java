package model;

import model.client.Client;
import org.apache.log4j.Logger;

import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;

public class MainTest {
    private static Logger logger = Logger.getLogger(MainTest.class);
    public static void main(String[] args) throws IOException{

        // make sure that current solution works with System.in
        Thread serverThread = new Thread(new ServerLauncher(new File("config.xml")));
        serverThread.setName("server_thread");
        serverThread.start();
        new Client(new File("config.xml"), new FileInputStream("commands.txt")).start();
    }
}
