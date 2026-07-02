import commands.Command;
import commands.CommandFactory;
import completion.BuiltinCompleter;
import completion.CompletionWidget;
import completion.FileNameCompleter;
import org.jline.reader.*;
import org.jline.reader.impl.completer.AggregateCompleter;
import org.jline.terminal.Terminal;
import org.jline.terminal.TerminalBuilder;
import parser.CommandLine;
import completion.ExecutableCompleter;

import java.util.List;


public class Main {
    public static void main(String[] args) throws Exception {
        try (Terminal terminal = TerminalBuilder.builder().system(true).build()) {
            List<Completer> completers = List.of(
                    new BuiltinCompleter(),
                    new ExecutableCompleter(),
                    new FileNameCompleter()
            );

            AggregateCompleter completer = new AggregateCompleter(completers);

            LineReader reader = LineReaderBuilder.builder()
                    .terminal(terminal)
                    .completer(completer)
                    .option(LineReader.Option.DISABLE_EVENT_EXPANSION, true)
                    .build();

            Widget widget = new CompletionWidget(reader, completer,completers).getWidget();

            while (true) {
                String line = reader.readLine("$ ");

                CommandLine commandLine = new CommandLine(line);
                Command cmd = CommandFactory.getCommand(commandLine.getCommand());
                cmd.execute(commandLine);
            }
        }
    }
}
