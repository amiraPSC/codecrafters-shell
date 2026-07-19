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

        }else if (CompleteCommand.hasKey(line.words().get(0))) {
            List<String> output = runCompletionScript(line);

            for (String s : output) {
                candidates.add(new Candidate(s));
            }

        }else{
            Completer fileNameCompleter = CompleterFactory.getCompleter(CompleterType.Files);
            fileNameCompleter.complete(reader, line, candidates);
        }
    }

    private List<String> runCompletionScript(ParsedLine line) {
        List<String> words = line.words();
        String currentWord = line.word();
        String command = words.getFirst();

        List<String> args = new ArrayList<>();
        String value = CompleteCommand.getValue(line.words().get(0));
        String script = value.substring(1, value.length() - 1);
        args.add(script);
        args.add(command);
        args.add(currentWord);

        if (words.size() > 1) {
            args.add(words.get(words.size() - 2));
        }else {
            args.add("");
        }

        ProcessExecutor processExecutor = new ProcessExecutor();
        List<String> output;
        try {
            output = processExecutor.executeProcess(args);
        } catch (Exception e) {
            System.err.println(e.getMessage());
            output = List.of();
        }

        return output;
    }
}
