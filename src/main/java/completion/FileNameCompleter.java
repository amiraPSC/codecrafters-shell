package completion;

import org.jline.reader.Candidate;
import org.jline.reader.Completer;
import org.jline.reader.LineReader;
import org.jline.reader.ParsedLine;
import utils.PathScanning;

import java.nio.file.Path;
import java.util.List;

public class FileNameCompleter implements Completer {
    @Override
    public void complete(LineReader reader, ParsedLine line, List<Candidate> candidates) {
        String word = line.word();
        String prefix;
        Path dir;

        if (word.contains("/")){
            int lastSlash = word.lastIndexOf("/");
            String name = word.substring(0, lastSlash + 1);
            String dirName = (PathScanning.getCurrentDir().getFileName().toString()) + "/" + name;
            prefix = word.substring(lastSlash + 1);


            dir = Path.of(dirName);
        }else {
            prefix = word;
            dir = PathScanning.getCurrentDir();
        }

        for (String file : PathScanning.getFilesInCurrentDirectory(dir)) {
            if (file.startsWith(prefix)) {
                candidates.add(new Candidate(file));
            }
        }
    }
}
