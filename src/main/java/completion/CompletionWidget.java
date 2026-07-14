package completion;

import org.jline.keymap.KeyMap;
import org.jline.reader.*;
import terminal.DisplayManager;
import terminal.TerminalContext;

public class CompletionWidget {
    private final CompletionController controller;
    private TerminalContext terminalContext;
    private static Widget widget;

    public CompletionWidget(DisplayManager displayManager, TerminalContext terminalContext) {
        this.terminalContext = terminalContext;
        this.controller  = new CompletionController(terminalContext, displayManager);
    }

    private void createWidget() {
        widget = () -> {
            controller.handleCompletion();
            return true;
        };
    }

    private void registerWidget() {
        LineReader lineReader = terminalContext.getReader();
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
