package parser;

import org.jline.reader.Candidate;
import org.jline.reader.Completer;
import org.jline.reader.LineReader;
import org.jline.reader.ParsedLine;

import java.util.List;

public class ShellCompleter implements Completer {
    private static final List<String> commands = List.of("echo", "exit");
    @Override
    public void complete(LineReader reader, ParsedLine line, List<Candidate> candidates) {
        String word = line.word();
        for (String command : commands) {
            if (command.startsWith(word)) {
                candidates.add(new Candidate(command));
            }
        }
    }
}
