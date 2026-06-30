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
        Path dir = getDir(word);
        String prefix = getPrefix(word);

        for (String file : PathScanning.getFilesInDir(dir)) {
            if (file.startsWith(prefix)) {
                candidates.add(new Candidate(buildCompletion(word, file), buildCompletion(word, file), null, null, " ", null, true));
            }
        }
    }

    private String buildCompletion(String word, String file) {
        if (word.contains("/")) {
            int lastSlash = word.lastIndexOf("/");
            return word.substring(0, lastSlash + 1) + file;
        }
        return file;
    }

    private Path getDir(String word){
        Path dir;

        if (word.contains("/")){
            int lastSlash = word.lastIndexOf("/");
            String name = word.substring(0, lastSlash + 1);
            Path path = Path.of(name);

            dir = path;
        }else {
            dir = PathScanning.getCurrentDir();
        }
        return dir;
    }

    private String getPrefix(String word){
        String prefix;
        if (word.contains("/")){
            int lastSlash = word.lastIndexOf("/");
            prefix = word.substring(lastSlash + 1);
        }else  {
            prefix = word;
        }
        return prefix;
    }
}
