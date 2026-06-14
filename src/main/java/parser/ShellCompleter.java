package parser;

import org.jline.reader.Candidate;
import org.jline.reader.Completer;
import org.jline.reader.LineReader;
import org.jline.reader.ParsedLine;
import org.jline.reader.impl.completer.AggregateCompleter;
import org.jline.reader.impl.completer.StringsCompleter;

import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.Arrays;
import java.util.List;

public class ShellCompleter implements Completer {
    @Override
    public void complete(LineReader reader, ParsedLine line, List<Candidate> candidates) {
        String word = line.word();

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

    public Completer getCompleter() {
        return new AggregateCompleter(getStringCompleter(), new ShellCompleter());
    }

    private static Completer getStringCompleter() {
        return new StringsCompleter("echo", "exit");
    }

    private List<String> listOfPATHs(){
        String path = System.getenv("PATH");
        String separator = System.getProperty("path.separator");
        String[] paths = path.split(separator);

        return Arrays.asList(paths);
    }
}
