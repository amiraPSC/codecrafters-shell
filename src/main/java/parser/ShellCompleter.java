package parser;

import org.jline.reader.Candidate;
import org.jline.reader.Completer;
import org.jline.reader.LineReader;
import org.jline.reader.ParsedLine;

import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.Arrays;
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

        List<String> paths = listOfPATHs();
        for (String path : paths) {
            if (path.startsWith(word)) {
                Path p = Paths.get(path);
                if (p.toFile().exists()) {
                    candidates.add(new Candidate(path));
                }
            }
        }
    }

    private List<String> listOfPATHs(){
        String path = System.getenv("PATH");
        String separator = System.getProperty("path.separator");
        String[] paths = path.split(separator);

        return Arrays.asList(paths);
    }
}
