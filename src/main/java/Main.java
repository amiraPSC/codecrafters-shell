import commands.Command;
import commands.CommandFactory;
import completion.*;
import jobs.JobManager;
import org.jline.reader.*;
import org.jline.terminal.Terminal;
import org.jline.terminal.TerminalBuilder;
import parser.Parser;
import terminal.DisplayManager;
import terminal.TerminalContext;


public class Main {
    public static void main(String[] args) throws Exception {
        try (Terminal terminal = TerminalBuilder.builder().system(true).build()) {
            TerminalContext terminalContext = new TerminalContext(terminal);

            DisplayManager displayManager = new DisplayManager(terminalContext);
            LineReader lineReader = terminalContext.getReader();

            Widget widget = new CompletionWidget(displayManager, terminalContext).getWidget();

            JobManager jobManager = new JobManager();

            while (true) {
                Parser parser = new Parser();
                jobManager.reapCompletedJobs();

                String line = lineReader.readLine("$ ");
                parser.parse(line);

                Command cmd = CommandFactory.getCommand(parser);
                cmd.execute(parser);
            }
        }
    }
}
