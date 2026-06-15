package parser;

import org.jline.reader.Candidate;
import org.jline.reader.Completer;
import org.jline.reader.LineReader;
import org.jline.reader.ParsedLine;
import utils.PathScanning;

import java.util.List;

public class ShellCompleter implements Completer {
    @Override
    public void complete(LineReader reader, ParsedLine line, List<Candidate> candidates) {
        String word = line.word();

        addCandidateIfMatches(candidates, "echo", word);
        addCandidateIfMatches(candidates, "exit", word);

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
