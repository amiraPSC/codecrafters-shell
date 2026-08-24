package listeners;

import commands.impl.HistoryCommand;

public class HistoryListener implements Listener{

    @Override
    public void event(String line) {
        HistoryCommand.addCommand(line);
    }
}
