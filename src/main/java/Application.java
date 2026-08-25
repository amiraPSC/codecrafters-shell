import commands.CommandFactory;
import commands.impl.HistoryCommand;
import completion.CompletionWidget;
import listeners.JobListeners;
import listeners.Listener;
import org.jline.reader.LineReader;
import org.jline.reader.Widget;
import terminal.DisplayManager;
import terminal.TerminalContext;

import java.util.ArrayList;
import java.util.List;

public class Application {
    private List<Listener> listeners = new ArrayList<>();

    public void start(TerminalContext terminalContext, LineReader reader) {
        DisplayManager displayManager = new DisplayManager(terminalContext);
        Widget widget = new CompletionWidget(displayManager, terminalContext).getWidget();
        CommandFactory.installationHistory(new HistoryCommand(reader.getHistory()));

        addListeners();
    }

    public void notify(String line){
        for (Listener listener : listeners){
            listener.event();
        }
    }

    private void addListeners(){
        listeners.add(new JobListeners());
    }
}
