package completion.completers;

import org.jline.reader.Candidate;
import org.jline.reader.Completer;
import org.jline.reader.LineReader;
import org.jline.reader.ParsedLine;

import java.util.ArrayList;
import java.util.List;

public class BuiltinCompleter implements Completer {
    private static final List<Candidate> BUILTINS = new ArrayList<>();
    {
        BUILTINS.add(new Candidate("echo "));
        BUILTINS.add(new Candidate("exit "));
        BUILTINS.add(new Candidate("cd "));
        BUILTINS.add(new Candidate("pwd "));
        BUILTINS.add(new Candidate("type "));
    }

    @Override
    public void complete(LineReader reader, ParsedLine line, List<Candidate> candidates) {
        String word = line.word();

        for (Candidate candidate : BUILTINS) {
            if (candidate.value().startsWith(word)) {
                candidates.add(candidate);
            }
        }
    }
}
