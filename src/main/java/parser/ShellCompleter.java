package parser;

import org.jline.reader.Candidate;
import org.jline.reader.Completer;
import org.jline.reader.LineReader;
import org.jline.reader.ParsedLine;
import org.jline.reader.impl.completer.AggregateCompleter;
import org.jline.reader.impl.completer.StringsCompleter;
import utils.PathScanning;

import java.util.List;
import java.util.Set;

public class ShellCompleter implements Completer {
    @Override
    public void complete(LineReader reader, ParsedLine line, List<Candidate> candidates) {
        String word = line.word();

        Set<String> exeNames = PathScanning.listOfPATHs();
        for (String exe : exeNames) {
            if (exe.startsWith(word)) {
                System.out.println(word);
                System.out.println(exe);
                //candidates.add(new Candidate(exe));
            }
        }
    }

    public Completer getCompleter() {
        return new AggregateCompleter(getStringCompleter(), new ShellCompleter());
    }

    private static Completer getStringCompleter() {
        return new StringsCompleter("echo", "exit");
    }


}
