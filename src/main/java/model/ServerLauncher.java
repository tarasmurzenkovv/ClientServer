package model;

import model.server.Server;

import java.io.File;

public class ServerLauncher implements Runnable {
    private File file;
    private Server server;

    public ServerLauncher(File file) {
        this.file = file;
    }

    @Override
    public void run() {
        Server.start(file);
    }
}
