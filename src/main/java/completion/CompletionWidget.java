package completion;

import org.jline.keymap.KeyMap;
import org.jline.reader.Binding;
import org.jline.reader.LineReader;
import org.jline.reader.Reference;
import org.jline.reader.Widget;

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
            String buffer = reader.getBuffer().toString(); //لا تنسي تجيبي النص

            if (!buffer.equals(lastLine)){
                tabCount = 0;
                lastLine = buffer;
            }

            tabCount++;

            if (tabCount == 1) {
                reader.callWidget(LineReader.BEEP);
            }else if (tabCount == 2) {
                reader.callWidget(LineReader.LIST_CHOICES);
                reader.printAbove(buffer);
            }
            return true;
        };
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
