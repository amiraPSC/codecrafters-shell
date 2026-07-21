package terminal;

import org.jline.reader.Buffer;
import org.jline.reader.Candidate;
import org.jline.reader.LineReader;
import org.jline.terminal.Terminal;
import org.jline.utils.InfoCmp;

import java.io.OutputStream;
import java.util.List;

public class DisplayManager {
    private final TerminalContext terminalContext;

    public DisplayManager(TerminalContext terminalContext) {
        this.terminalContext = terminalContext;
    }

    public void updateBufferAndDisplay(String theLCP){
        LineReader reader = terminalContext.getReader();
        Buffer buffer = terminalContext.getBuffer();
        String word = terminalContext.getCurrentWord();
        buffer.write(theLCP.substring(word.length(), theLCP.length()));
        redrawLine(reader);
    }

    public void displayCompletions(List<Candidate> candidates){
        Terminal terminal = terminalContext.getTerminal();
        LineReader reader = terminalContext.getReader();

        terminal.puts(InfoCmp.Capability.save_cursor);
        terminal.writer().println();
        for (Candidate candidate : candidates) {
            terminal.writer().print(candidate.value() + "  ");
        }
        terminal.writer().println();
        terminal.puts(InfoCmp.Capability.restore_cursor);
        terminal.writer().flush();

        redrawLine(reader);
    }

    private void redrawLine(LineReader reader){
        reader.callWidget(LineReader.REDRAW_LINE);
        reader.callWidget(LineReader.REDISPLAY);
    }

    public void beep(){
        LineReader reader = terminalContext.getReader();
        reader.callWidget(LineReader.BEEP);
    }
}
