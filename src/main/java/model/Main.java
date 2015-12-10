package model;

import model.client.Client;
import model.server.Server;
import org.apache.commons.lang3.StringUtils;
import org.apache.log4j.Logger;

import java.io.File;

public class Main {
    private static Logger logger = Logger.getLogger(Main.class);
    private static String[] processInputParams(String[] args) {
        String type = null;
        String fileName = null;
        String[] processedInputParams = new String[2];
        try {
            type = args[0];//either client or server mode
            fileName = args[2]; //path to xml file

            if (StringUtils.isEmpty(type) || StringUtils.isEmpty(fileName)) {
                throw new RuntimeException();
            }
        } catch (RuntimeException e) {
            logger.error("How to use this jar: ");
            logger.error("To start a server: java –jar app.jar –server –config config.xml");
            logger.error("To start a client: java –jar app.jar –client –config config.xml");
            logger.error("Program will be terminated");
            System.exit(0);
            /*asdfasd*/
        }
        processedInputParams[0] = type;
        processedInputParams[1] = fileName;

        return processedInputParams;
    }

    public static void main(String[] args) {
        String type = Main.processInputParams(args)[0];
        String fileName = Main.processInputParams(args)[1];
        File properties = new File(fileName);// load properties file
        switch (type) {
            case "-server":
                logger.debug("Started in a server mode. ");
                Server.start(properties);
                break;
            case "-client":
                logger.debug("Started in a client mode. ");
                new Client(properties,System.in).start();
                break;
        }
    }
}
