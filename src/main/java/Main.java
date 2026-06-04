import commands.Command;
import commands.CommandFactory;
import parser.CommandLine;

import java.util.Scanner;

public class Main {
    public static void main(String[] args) throws Exception {
        Scanner sc = new Scanner(System.in);

        while (true){
            System.out.print("$ ");
            String input = sc.nextLine();

            CommandLine commandLine = new CommandLine(input);
            Command cmd = CommandFactory.getCommand(commandLine.getCommand());
            cmd.execute(commandLine);
        }
    }
}
