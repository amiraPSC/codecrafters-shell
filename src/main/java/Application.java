import commands.CommandFactory;
import commands.impl.HistoryCommand;
import completion.CompletionWidget;
import listeners.JobListeners;
import listeners.Listener;
import org.jline.reader.History;
import org.jline.reader.LineReader;
import org.jline.reader.Widget;
import terminal.DisplayManager;
import terminal.TerminalContext;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

public class Application {
    private List<Listener> listeners = new ArrayList<>();

    public void start(TerminalContext terminalContext, LineReader reader) throws IOException {
        DisplayManager displayManager = new DisplayManager(terminalContext);
        Widget widget = new CompletionWidget(displayManager, terminalContext).getWidget();

        HistoryCommand historyCommand = new HistoryCommand(reader.getHistory());
        CommandFactory.installationHistory(historyCommand);
        historyCommand.loadFromHistFile();

        addListeners();
    }

    public void notifyListeners(){
        for (Listener listener : listeners){
            listener.event();
        }
    }

    private void addListeners(){
        listeners.add(new JobListeners());
    }
}
