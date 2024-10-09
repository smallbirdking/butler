package org.example;

import picocli.CommandLine;
import picocli.CommandLine.Command;
import picocli.CommandLine.Option;

@Command(name = "butler", mixinStandardHelpOptions = true, version = "1.0",
         description = "Command line tool for various tasks.")
public class BulterCommandLine implements Runnable {

//    @Parameters(index = "0", description = "The file whose checksum to calculate.")
//    private File file;
    @Option(names = {"-c", "--command"}, description = "The command to execute (help, record).", required = true)
    private String command;

    public static void main(String[] args) {
        CommandLine.run(new BulterCommandLine(), args);
    }

    @Override
    public void run() {
    }

    private void printHelp() {
        System.out.println("Usage:");
        System.out.println("  help          Display this help message");
        System.out.println("  record        Start and stop audio recording");
    }

    private void start() {
        CommandLine commandLine = new CommandLine(new BulterCommandLine());
        commandLine.execute();
    }
}