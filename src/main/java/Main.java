import commands.Command;
import commands.CommandFactory;
import completion.*;
import org.jline.reader.*;
import org.jline.terminal.Terminal;
import org.jline.terminal.TerminalBuilder;
import parser.CommandLine;
import parser.Reader;


public class Main {
    public static void main(String[] args) throws Exception {
        try (Terminal terminal = TerminalBuilder.builder().system(true).build()) {

            Reader reader = new Reader(terminal);
            LineReader lineReader = reader.getLineReader();

            Widget widget = new CompletionWidget(reader).getWidget();

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
