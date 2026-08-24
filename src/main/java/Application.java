import completion.CompletionWidget;
import listeners.HistoryListener;
import listeners.JobListeners;
import listeners.Listener;
import org.jline.reader.Widget;
import parser.nodes.Node;
import terminal.DisplayManager;
import terminal.TerminalContext;

import java.util.ArrayList;
import java.util.List;

public class Application {
    private List<Listener> listeners = new ArrayList<>();

    public void start(TerminalContext terminalContext){
        DisplayManager displayManager = new DisplayManager(terminalContext);
        Widget widget = new CompletionWidget(displayManager, terminalContext).getWidget();

        addListeners();
    }

    public void notify(String line){
        for (Listener listener : listeners){
            listener.event(line);
        }
    }

    private void addListeners(){
        listeners.add(new JobListeners());
        listeners.add(new HistoryListener());
    }
}
