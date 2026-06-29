package completion;

import org.jline.reader.Candidate;
import org.jline.reader.Completer;
import org.jline.reader.LineReader;
import org.jline.reader.ParsedLine;
import utils.PathScanning;

import java.util.List;

public class FileNameCompleter implements Completer {
    @Override
    public void complete(LineReader reader, ParsedLine line, List<Candidate> candidates) {
        String word = line.word();
        for (String file : PathScanning.getFilesInCurrentDirectory()) {
            if (file.startsWith(word)) {
                candidates.add(new Candidate(file));
            }
        }
    }
}
