import commands.Command;
import commands.CommandFactory;
import completion.*;
import org.jline.reader.*;
import org.jline.terminal.Terminal;
import org.jline.terminal.TerminalBuilder;
import parser.CommandLine;
import terminal.DisplayManager;
import terminal.TerminalContext;


public class Main {
    public static void main(String[] args) throws Exception {
        try (Terminal terminal = TerminalBuilder.builder().system(true).build()) {
            TerminalContext terminalContext = new TerminalContext(terminal);

            DisplayManager displayManager = new DisplayManager(terminalContext);
            LineReader lineReader = terminalContext.getReader();

            Widget widget = new CompletionWidget(displayManager, terminalContext).getWidget();

            while (true) {
                CommandLine commandLine = new CommandLine();

                String line = lineReader.readLine("$ ");
                commandLine.parseCommandLine(line);

                Command cmd = CommandFactory.getCommand(commandLine.getCommand());
                cmd.execute(commandLine);
            }
        }
    }
}
