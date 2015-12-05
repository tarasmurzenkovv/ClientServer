package model.client;

public class MainClient {

    public static void main(String[] args) {

        if (args.length != 2) {
            System.err.println("Usage: java EchoClient <host name> <port number>");
            System.exit(1);
        }
        String host = args[0];
        int port = Integer.parseInt(args[1]);
        Client client = new Client(port, host);
        client.run();
    }
}
