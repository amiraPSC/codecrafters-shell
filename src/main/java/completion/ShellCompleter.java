package completion;

import commands.impl.CompleteCommand;
import org.jline.reader.Candidate;
import org.jline.reader.Completer;
import org.jline.reader.LineReader;
import org.jline.reader.ParsedLine;
import utils.ProcessExecutor;

import java.util.ArrayList;
import java.util.List;

public class ShellCompleter implements Completer {
    @Override
    public void complete(LineReader reader, ParsedLine line, List<Candidate> candidates) {
        int wordIndex = line.wordIndex();

        if (wordIndex == 0) {
            Completer builtinCompleter = CompleterFactory.getCompleter(CompleterType.Builtin);
            Completer ExecutableCompleter = CompleterFactory.getCompleter(CompleterType.Executable);
            builtinCompleter.complete(reader, line, candidates);
            ExecutableCompleter.complete(reader, line, candidates);

        }else if (wordIndex == 1 && CompleteCommand.hasKey(line.words().get(0))) {
            List<String> args = new ArrayList<>();
            args.add(CompleteCommand.getValue(line.words().get(0)));
            args.add(line.words().get(0));

            ProcessExecutor processExecutor = new ProcessExecutor();
            List<String> output;
            try {
                output = processExecutor.executeProcess(args);
            } catch (Exception e) {
                System.err.println(e.getMessage());
                output = List.of();
            }

            for (String s : output) {
                candidates.add(new Candidate(s));
            }

        }else{
            Completer fileNameCompleter = CompleterFactory.getCompleter(CompleterType.Files);
            fileNameCompleter.complete(reader, line, candidates);
        }
    }
}
