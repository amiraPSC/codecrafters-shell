package completion;

import org.jline.keymap.KeyMap;
import org.jline.reader.*;
import org.jline.reader.impl.completer.AggregateCompleter;
import org.jline.terminal.Terminal;

import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.List;

public class CompletionWidget {
    private int tabCount = 0;
    private String lastLine = "";
    private LineReader reader;
    private Widget widget;
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

            candidates.sort(
                    Comparator.comparing(Candidate::value)
            );


            if (candidates.isEmpty()) {
                reader.callWidget(LineReader.BEEP);
            }else if (candidates.size() == 1) {
                reader.callWidget("expand-or-complete");
            }else if (candidates.size() > 1) {
                if (!line.equals(lastLine)){
                    tabCount = 0;
                }else{
                    tabCount++;
                }

                if (tabCount == 0){
                    reader.callWidget(LineReader.BEEP);
                }else if (tabCount > 0){
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
