package modeltest;

import model.ReplyListener;
import model.client.Client;
import model.server.Server;
import model.utils.CommandGenerator;

import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.util.List;
import java.util.concurrent.*;
import java.util.stream.IntStream;

public class MainTest {

    private static final int NUMBER_OF_THREADS = 2;
    private static final String FILE_NAME_WITH_COMMANDS = "commands.txt";
    private static final int NUMBER_OF_COMMANDS_TO_GENERATE = 1000;

    public static void main(String[] args) throws IOException, InterruptedException, ExecutionException {
        CountDownLatch countDownLatch = new CountDownLatch(NUMBER_OF_THREADS);
        InputStream inputStream = new FileInputStream(FILE_NAME_WITH_COMMANDS);
        CommandGenerator commandGenerator = new CommandGenerator(NUMBER_OF_COMMANDS_TO_GENERATE, FILE_NAME_WITH_COMMANDS);

        ExecutorService executorService = Executors.newFixedThreadPool(2);
        List<String> actualServerReplies;
        List<String> actualClientRecivers;

        ReplyListener replyListener = m -> {};

        Callable<List<String>> clientThread = () -> new Client(new File("config.xml"), inputStream).start(NUMBER_OF_THREADS, countDownLatch);
        Callable<List<String>> serverThread = () -> Server.start(new File("config.xml"), replyListener, countDownLatch);

        boolean hasFinishedWritingCommands = commandGenerator.writeCommandsToFile();

        if (hasFinishedWritingCommands) {
            Future<List<String>> expectedServerMessages = executorService.submit(serverThread);
            Future<List<String>> expectedClientMessages = executorService.submit(clientThread);
            executorService.submit(clientThread);
            actualServerReplies = expectedServerMessages.get();
            actualClientRecivers = expectedClientMessages.get();
            executorService.shutdownNow();

            IntStream lengthOfReplies = IntStream.range(0, actualServerReplies.size() - 2);

            lengthOfReplies.forEach(index -> {
                if (!actualClientRecivers.get(index).equals(actualServerReplies.get(index))) {
                    System.out.println("Got mismatch in: ");
                    System.out.println("Server reply: " + actualServerReplies.get(index));
                    System.out.println("Client reply: " + actualClientRecivers.get(index));
                }
            });
            System.out.println("Tes has been passed. ");
        } else {
            throw new RuntimeException("Failed to generate file with commands");
        }
    }
}
