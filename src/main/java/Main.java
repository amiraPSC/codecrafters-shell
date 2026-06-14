import commands.Command;
import commands.CommandFactory;
import org.jline.reader.LineReader;
import org.jline.reader.LineReaderBuilder;
import org.jline.terminal.Terminal;
import org.jline.terminal.TerminalBuilder;
import parser.CommandLine;
import parser.ShellCompleter;

import java.util.Scanner;

public class Main {
    public static void main(String[] args) throws Exception {
        try (Terminal terminal = TerminalBuilder.builder().build()) {
            ShellCompleter completer = new ShellCompleter();
            LineReader reader = LineReaderBuilder.builder()
                    .terminal(terminal)
                    .completer(completer.getCompleter())
                    .option(LineReader.Option.DISABLE_EVENT_EXPANSION, true)
                    .build();

            while (true) {
                String line = reader.readLine("$ ");

                CommandLine commandLine = new CommandLine(line);
                Command cmd = CommandFactory.getCommand(commandLine.getCommand());
                cmd.execute(commandLine);
            }
        }
    }
}
