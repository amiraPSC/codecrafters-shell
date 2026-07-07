package completion;

import org.jline.keymap.KeyMap;
import org.jline.reader.*;
import org.jline.terminal.Terminal;
import org.jline.utils.InfoCmp;

import java.util.*;

public class CompletionWidget {
    private int tabCount = 0;
    private String lastLine = "";
    private LineReader reader;
    private static Widget widget;

    public CompletionWidget(LineReader reader) {
        this.reader = reader;
    }

    private void createWidget(){
        widget = () -> {
            String line = reader.getBuffer().toString();
            Terminal terminal = reader.getTerminal();
            ParsedLine parsedLine = getParse();
            String word = parsedLine.word();

            List<Candidate> candidates = new ArrayList<>();
            CompleterFactory.create().complete(reader, parsedLine, candidates);
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
                    String theLCP = LongestCommonPrefix.findLongestCommonPrefix(word, candidates);

                    if (line.length() == theLCP.length()){
                        reader.callWidget(LineReader.BEEP);
                    }else {
                        updateBufferAndDisplay(reader,word, theLCP);
                    }
                }else if (tabCount > 0){
                    terminal.puts(InfoCmp.Capability.save_cursor);
                    terminal.writer().println();
                    for (Candidate candidate : candidates) {
                        terminal.writer().print(candidate.value() + "  ");
                    }
                    terminal.writer().println();
                    terminal.puts(InfoCmp.Capability.restore_cursor);
                    terminal.writer().flush();

                    reader.callWidget(LineReader.REDRAW_LINE);
                    reader.callWidget(LineReader.REDISPLAY);
                }
            }

            lastLine = line;
            return true;
        };
    }

    private void updateBufferAndDisplay(LineReader reader,String word ,String theLCP){
        Buffer buffer = reader.getBuffer();
        int begin = word.length();
        int end = theLCP.length();
        String sub = theLCP.substring(begin, end);

        System.out.println();
        System.out.println(sub.length());

        buffer.write(sub);
        reader.callWidget(LineReader.REDRAW_LINE);
        reader.callWidget(LineReader.REDISPLAY);
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
