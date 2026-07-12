package parser;

import completion.CompleterFactory;
import org.jline.reader.LineReader;
import org.jline.reader.LineReaderBuilder;
import org.jline.terminal.Terminal;

public class Reader {
    private static final LineReader reader;
    private static Terminal terminal;
    static {
        reader = LineReaderBuilder.builder()
                .terminal(terminal)
                .completer(CompleterFactory.create())
                .option(LineReader.Option.DISABLE_EVENT_EXPANSION, true)
                .build();
    }

    public Reader(Terminal terminal) {
        this.terminal = terminal;
    }

    public LineReader getLineReader(){
        return reader;
    }
}
