import commands.impl.Command;
import commands.CommandFactory;
import completion.*;
import org.jline.reader.*;
import org.jline.terminal.Terminal;
import org.jline.terminal.TerminalBuilder;
import parser.CommandLine;


public class Main {
    public static void main(String[] args) throws Exception {
        try (Terminal terminal = TerminalBuilder.builder().system(true).build()) {
            LineReader reader = LineReaderBuilder.builder()
                    .terminal(terminal)
                    .completer(CompleterFactory.create())
                    .option(LineReader.Option.DISABLE_EVENT_EXPANSION, true)
                    .build();

            Widget widget = new CompletionWidget(reader).getWidget();

            while (true) {
                String line = reader.readLine("$ ");

                CommandLine commandLine = new CommandLine(line);
                Command cmd = CommandFactory.getCommand(commandLine.getCommand());
                cmd.execute(commandLine);
            }
        }
    }
}
