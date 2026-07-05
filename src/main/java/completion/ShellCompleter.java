package completion;

import org.jline.reader.Candidate;
import org.jline.reader.Completer;
import org.jline.reader.LineReader;
import org.jline.reader.ParsedLine;

import java.util.List;

public class ShellCompleter implements Completer {
    @Override
    public void complete(LineReader reader, ParsedLine line, List<Candidate> candidates) {
        if (line.wordIndex() == 0) {
            Completer builtinCompleter = CompleterFactory.getCompleter(CompleterType.Builtin);
            Completer ExecutableCompleter = CompleterFactory.getCompleter(CompleterType.Executable);
            builtinCompleter.complete(reader, line, candidates);
            ExecutableCompleter.complete(reader, line, candidates);
        }else{
            Completer fileNameCompleter = CompleterFactory.getCompleter(CompleterType.Files);
            fileNameCompleter.complete(reader, line, candidates);
        }
    }
}
