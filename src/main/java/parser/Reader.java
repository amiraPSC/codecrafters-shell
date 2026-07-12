package parser;

import completion.CompleterFactory;
import org.jline.reader.LineReader;
import org.jline.reader.LineReaderBuilder;
import org.jline.terminal.Terminal;

public class Reader {
    private final LineReader reader;

    public Reader(Terminal terminal) {
        this.reader = LineReaderBuilder.builder()
                .terminal(terminal)
                .completer(CompleterFactory.create())
                .option(LineReader.Option.DISABLE_EVENT_EXPANSION, true)
                .build();
    }

    public LineReader getLineReader(){
        return reader;
    }
}
