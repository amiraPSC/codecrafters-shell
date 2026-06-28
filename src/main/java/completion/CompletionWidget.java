package completion;

import org.jline.keymap.KeyMap;
import org.jline.reader.*;
import org.jline.reader.impl.completer.AggregateCompleter;
import org.jline.terminal.Terminal;

import java.util.*;

public class CompletionWidget {
    private int tabCount = 0;
    private String lastLine = "";
    private LineReader reader;
    private static Widget widget;
    AggregateCompleter completer;

    public CompletionWidget(LineReader reader, AggregateCompleter completer) {
        this.reader = reader;
        this.completer = completer;
    }

    private void createWidget(){
        widget = () -> {
            String line = reader.getBuffer().toString();

            ParsedLine parsedLine = getParse();
            Terminal terminal = reader.getTerminal();

            List<Candidate> candidates = new ArrayList<>();
            completer.complete(reader, parsedLine, candidates);
            Set<Candidate> candidateSet = new HashSet<>(candidates);

            candidates.sort(
                    Comparator.comparing(Candidate::value)
            );


            if (candidateSet.isEmpty()) {
                reader.callWidget(LineReader.BEEP);
            }else if (candidateSet.size() == 1) {
                reader.callWidget("expand-or-complete");
            }else if (candidateSet.size() > 1) {
                if (!line.equals(lastLine)){
                    tabCount = 0;
                }else{
                    tabCount++;
                }

                if (tabCount == 0){
                    String theLCP = findLongestCommonPrefix(line, candidates);

                    if (line.length() == theLCP.length()){
                        reader.callWidget(LineReader.BEEP);
                    }else {
                        updateBufferAndDisplay(reader, theLCP);
                    }
                }else if (tabCount > 0){
                    terminal.writer().println();
                    for (Candidate candidate : candidates) {
                        terminal.writer().print(candidate.value() + "  ");
                    }
                    terminal.writer().println();
                    terminal.writer().println("$ " + line);
                    terminal.writer().flush();
                }
            }

            lastLine = line;
            return true;
        };
    }

    private String findLongestCommonPrefix(String line, List<Candidate> candidates){
        StringBuilder prefixBuilder = new StringBuilder();
        prefixBuilder.append(line);

        int len = line.length();
        String shortest = findShortest(candidates);

        boolean found = true;
        for (int i = len; i < shortest.length(); i++) {
            String sub = shortest.substring(0, i + 1);
            for (Candidate candidate : candidates) {
                if (!candidate.value().startsWith(sub)) {
                    found = false;
                    break;
                }
            }
            if (found){
                prefixBuilder.append(shortest.charAt(i));
            }else {
                break;
            }
        }
        return prefixBuilder.toString();
    }

    private void updateBufferAndDisplay(LineReader reader, String theLCP){
        Buffer buffer = reader.getBuffer();

        buffer.write(theLCP.substring(buffer.length(), theLCP.length()));
        reader.callWidget(LineReader.REDRAW_LINE);
        reader.callWidget(LineReader.REDISPLAY);
    }

    private String findShortest(List<Candidate> candidates){
        List<String> list = new ArrayList<>();
        for (Candidate candidate : candidates) {
            list.add(candidate.value());
        }

        String shortest = list.stream()
                .min(Comparator.comparingInt(String::length))
                .get();

        return shortest;
    }

    private ParsedLine getParse(){
        ParsedLine parsedLine =
                reader.getParser().parse(
                        reader.getBuffer().toString(),
                        reader.getBuffer().cursor()
                );
        return parsedLine;
    }

    private void registerWidget(){
        KeyMap<Binding> keyMap = reader.getKeyMaps().get(LineReader.MAIN);
        reader.getWidgets().put("my-completion", widget);

        keyMap.bind(
                new Reference("my-completion"),
                "\t"
        );
    }

    public Widget getWidget() {
        createWidget();
        registerWidget();
        return widget;
    }
}
