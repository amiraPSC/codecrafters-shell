package parser;

import org.jline.reader.Candidate;
import org.jline.reader.Completer;
import org.jline.reader.LineReader;
import org.jline.reader.ParsedLine;
import utils.PathScanning;

import java.util.ArrayList;
import java.util.List;

public class ShellCompleter implements Completer {
    private static final List<Candidate> BUILTINS = new ArrayList<>();
    {
        BUILTINS.add(new Candidate("echo", "echo", null, null, " ", null, true));
        BUILTINS.add(new Candidate("exit", "exit", null, null, " ", null, true));
    }

    @Override
    public void complete(LineReader reader, ParsedLine line, List<Candidate> candidates) {
        String word = line.word();

        for (Candidate candidate : BUILTINS) {
            if (candidate.value().startsWith(word)) {
                candidates.add(candidate);
            }
        }

        for (String exe : PathScanning.listOfPATHs()) {
            addCandidateIfMatches(candidates, exe, word);
        }
    }

    private void addCandidateIfMatches(List<Candidate> candidates, String value, String word) {
        if (value.startsWith(word)) {
            candidates.add(new Candidate(value, value, null, null, " ", null, true));
        }
    }

}
