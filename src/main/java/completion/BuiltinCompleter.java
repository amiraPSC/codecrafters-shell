package completion;

import org.jline.reader.Candidate;
import org.jline.reader.Completer;
import org.jline.reader.LineReader;
import org.jline.reader.ParsedLine;

import java.util.ArrayList;
import java.util.List;

public class BuiltinCompleter implements Completer {
    private List<String> completions = new ArrayList<>();

    private static final List<Candidate> BUILTINS = new ArrayList<>();
    {
        BUILTINS.add(new Candidate("echo", "echo", null, null, " ", null, true));
        BUILTINS.add(new Candidate("exit", "exit", null, null, " ", null, true));
        BUILTINS.add(new Candidate("cd", "cd", null, null, " ", null, true));
        BUILTINS.add(new Candidate("pwd", "pwd", null, null, " ", null, true));
        BUILTINS.add(new Candidate("type", "type", null, null, " ", null, true));
    }

    @Override
    public void complete(LineReader reader, ParsedLine line, List<Candidate> candidates) {
        String word = line.word();

        for (Candidate candidate : BUILTINS) {
            if (candidate.value().startsWith(word)) {
                candidates.add(candidate);
                completions.add(candidate.value());
            }
        }
    }

    public List<String> getCompletions() {
        return completions;
    }
}
