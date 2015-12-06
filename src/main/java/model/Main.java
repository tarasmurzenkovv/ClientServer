package model;

import model.client.Client;
import model.server.Server;

import java.io.File;

public class Main {

    public static void main(String[] args) {
        System.out.println("How to use this jar: ");
        System.out.println("To start a server: java –jar app.jar –server –config config.xml");
        System.out.println("To start a client: java –jar app.jar –client –config config.xml");

        String type = args[0];//either client or server mode
        String fileName = args[2]; //path to xml file

        File properties = new File(fileName);// load properties file
        switch (type){
            case "-server":
                Server.start(properties);
                break;
            case "-client":
                Client.start(properties);
                break;
        }
    }
}
