package terminal;

import completion.CompleterFactory;
import org.jline.reader.Buffer;
import org.jline.reader.LineReader;
import org.jline.reader.LineReaderBuilder;
import org.jline.reader.ParsedLine;
import org.jline.terminal.Terminal;

public class TerminalContext {
    private static LineReader reader;
    private static Terminal terminal;


    public TerminalContext(Terminal terminal) {
        this.terminal = terminal;
        this.reader = LineReaderBuilder.builder()
                .terminal(terminal)
                .completer(CompleterFactory.create())
                .option(LineReader.Option.DISABLE_EVENT_EXPANSION, true)
                .build();
    }

    public ParsedLine getParsedLine(){
        ParsedLine parsedLine =
                reader.getParser().parse(
                        reader.getBuffer().toString(),
                        reader.getBuffer().cursor()
                );
        return parsedLine;
    }

    public Buffer getBuffer(){
        return reader.getBuffer();
    }

    public String getCurrentWord(){
        return getParsedLine().word();
    }

    public String getLine(){
        return getBuffer().toString();
    }

    public LineReader getReader() {
        return reader;
    }

    public Terminal getTerminal() {
        return terminal;
    }
}
