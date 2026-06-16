package completion;

import org.jline.keymap.KeyMap;
import org.jline.reader.Binding;
import org.jline.reader.LineReader;
import org.jline.reader.Widget;

public class CompletionWidget {
    private int tabCount = 0;
    private String lastLine = "";
    private LineReader reader;
    private Widget widget;

    public CompletionWidget(LineReader reader) {
        this.reader = reader;
    }

    public Widget getWidget() {
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
                reader.callWidget(LineReader.COMPLETION_STYLE_LIST_GROUP);
                reader.callWidget(LineReader.REDRAW_LINE);
            }
            return true;
        };

        reader.getWidgets().put("completion", widget);
        KeyMap<Binding> keyMap = reader.getKeyMaps().get(LineReader.MAIN);
        keyMap.bind(widget, KeyMap.ctrl('i'));
        return widget;
    }
}
