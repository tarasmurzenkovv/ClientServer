package model.utils;

import java.io.BufferedWriter;
import java.io.FileWriter;
import java.io.IOException;
import java.io.PrintWriter;
import java.util.Random;

/**
 * Generated dummy commands to test the client-server solution.
 * It writes commands to the file "commands.txt"
 */
public class CommandGenerator {
    private int numberOfCommandsToGenerate;
    private String fileNameWithCommands;

    public CommandGenerator(int numberOfCommandsToGenerate, String fileNameWithCommands) {
        this.numberOfCommandsToGenerate = numberOfCommandsToGenerate;
        this.fileNameWithCommands = fileNameWithCommands;
    }

    private String getRandomCommand() {
        Random random = new Random();
        int randomNumber = random.nextInt(3);

        switch (randomNumber) {
            case 0:
                return "request_info#@client_name";
            case 1:
                return "server_time$@client_name";
            default:
                return "some really dummy text that was sent by a client@client_name";
        }
    }

    public boolean writeCommandsToFile() throws IOException {
        boolean hasFinished = true;
        try (PrintWriter out = new PrintWriter(new BufferedWriter(new FileWriter("commands.txt", true)))) {
            out.println("request_info#@some really awesome text sent by me@client_name");
            for (int i = 0; i < numberOfCommandsToGenerate; i++) {
                String command = getRandomCommand();
                out.println(command);
            }
            out.println("quit#@client_name");
        } catch (IOException e) {
            hasFinished = false;
        } finally {
            return hasFinished;
        }
    }
}
