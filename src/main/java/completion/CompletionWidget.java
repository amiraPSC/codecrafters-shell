package completion;

import org.jline.keymap.KeyMap;
import org.jline.reader.Binding;
import org.jline.reader.LineReader;
import org.jline.reader.Reference;
import org.jline.reader.Widget;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

public class CompletionWidget {
    private int tabCount = 0;
    private String lastLine = "";
    private LineReader reader;
    private Widget widget;

    public CompletionWidget(LineReader reader) {
        this.reader = reader;
    }

    private void createWidget(){
        widget = () -> {
            String buffer = reader.getBuffer().toString();

            if (!buffer.equals(lastLine)){
                tabCount = 0;
                lastLine = buffer;
            }

            tabCount++;

            if (tabCount == 1) {
                reader.callWidget(LineReader.BEEP);
            }else if (tabCount == 2) {
                for (String word : getCompletions()){
                    System.out.print(word + "  ");
                }
                System.out.println();
                System.out.println("$ " + buffer);
                tabCount = 0;
            }
            return true;
        };
    }

    private List<String> getCompletions(){
        BuiltinCompleter builtinCompleter = new BuiltinCompleter();
        ExecutableCompleter executableCompleter = new ExecutableCompleter();

        List<String> completions = new ArrayList<>();
        completions.addAll(builtinCompleter.getCompletions());
        completions.addAll(executableCompleter.getCompletions());

        Collections.sort(completions);
        return completions;
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
