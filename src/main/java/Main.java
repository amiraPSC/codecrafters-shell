import executors.Executor;
import jobs.JobManager;
import org.jline.reader.*;
import org.jline.terminal.Terminal;
import org.jline.terminal.TerminalBuilder;
import parser.Parser;
import parser.nodes.Node;
import terminal.TerminalContext;


public class Main {
    public static void main(String[] args) throws Exception {
        try (Terminal terminal = TerminalBuilder.builder().system(true).build()) {
            TerminalContext terminalContext = new TerminalContext(terminal);

            LineReader lineReader = terminalContext.getReader();
            Application application = new Application();
            application.start(terminalContext, lineReader);

            while (true) {
                application.notifyListeners();

                Parser parser = new Parser();
                Executor executor = new Executor();

                String line = lineReader.readLine("$ ");

                Node node = parser.parse(line);
                executor.execute(node);
                application.notifyListeners();
            }
        }
    }
}
