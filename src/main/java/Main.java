import commands.Command;
import commands.CommandFactory;
import completion.BuiltinCompleter;
import completion.CompletionWidget;
import org.jline.reader.*;
import org.jline.reader.impl.completer.AggregateCompleter;
import org.jline.terminal.Terminal;
import org.jline.terminal.TerminalBuilder;
import parser.CommandLine;
import completion.ExecutableCompleter;


public class Main {
    public static void main(String[] args) throws Exception {
        try (Terminal terminal = TerminalBuilder.builder().system(true).build()) {
            AggregateCompleter completer = new AggregateCompleter(new BuiltinCompleter(), new ExecutableCompleter());
            LineReader reader = LineReaderBuilder.builder()
                    .terminal(terminal)
                    .completer(completer)
                    .option(LineReader.Option.DISABLE_EVENT_EXPANSION, true)
                    .build();

            Widget widget = new CompletionWidget(reader, completer).getWidget();

            while (true) {
                String line = reader.readLine("$ ");

                CommandLine commandLine = new CommandLine(line);
                Command cmd = CommandFactory.getCommand(commandLine.getCommand());
                cmd.execute(commandLine);
            }
        }
    }
}
