package completion;

import org.jline.keymap.KeyMap;
import org.jline.reader.*;
import org.jline.terminal.Terminal;
import org.jline.utils.InfoCmp;
import parser.Reader;

import java.util.*;

public class CompletionWidget {
    private int tabCount = 0;
    private String lastLine = "";
    private Reader reader;
    private static Widget widget;

    public CompletionWidget(Reader reader) {
        this.reader = reader;
    }

    private void createWidget(){
        widget = () -> {
            LineReader lineReader = reader.getLineReader();
            String line = lineReader.getBuffer().toString();
            Terminal terminal = reader.getTerminal();
            ParsedLine parsedLine = reader.getParse();
            String word = parsedLine.word();

            List<Candidate> candidates = new ArrayList<>();
            CompleterFactory.create().complete(lineReader, parsedLine, candidates);
            Set<Candidate> candidateSet = new HashSet<>(candidates);

            candidates.sort(
                    Comparator.comparing(Candidate::value)
            );


            if (candidateSet.isEmpty()) {
                lineReader.callWidget(LineReader.BEEP);
            }else if (candidateSet.size() == 1) {
                lineReader.callWidget("expand-or-complete");
            }else if (candidateSet.size() > 1) {
                if (!line.equals(lastLine)){
                    tabCount = 0;
                }else{
                    tabCount++;
                }

                if (tabCount == 0){
                    String theLCP = LongestCommonPrefix.findLongestCommonPrefix(word, candidates);

                    if (word.length() == theLCP.length()){
                        lineReader.callWidget(LineReader.BEEP);
                    }else {
                        updateBufferAndDisplay(lineReader,word, theLCP);
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

                    lineReader.callWidget(LineReader.REDRAW_LINE);
                    lineReader.callWidget(LineReader.REDISPLAY);
                }
            }

            lastLine = line;
            return true;
        };
    }

    private void updateBufferAndDisplay(LineReader reader,String word ,String theLCP){
        Buffer buffer = reader.getBuffer();
        buffer.write(theLCP.substring(word.length(), theLCP.length()));
        reader.callWidget(LineReader.REDRAW_LINE);
        reader.callWidget(LineReader.REDISPLAY);
    }

    private void registerWidget(){
        LineReader lineReader = reader.getLineReader();
        KeyMap<Binding> keyMap = lineReader.getKeyMaps().get(LineReader.MAIN);
        lineReader.getWidgets().put("my-completion", widget);

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
