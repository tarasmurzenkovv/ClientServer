package model.utils;

import java.util.LinkedList;
import java.util.List;

/**
 * Generated dummy commands to test the client-server solution.
 * It writes commands to the file "commands.txt"
 */
public class CommandGenerator {
    private int numberOfCommandsToGenerate;

    public CommandGenerator(int numberOfCommandsToGenerate) {
        this.numberOfCommandsToGenerate = numberOfCommandsToGenerate;
    }


    public List<String> generateCommands(){
        List<String> generatedCommands = new LinkedList<>();

        for (int i = 0; i < this.numberOfCommandsToGenerate; i++) {

        }
        return generatedCommands;
    }
}
