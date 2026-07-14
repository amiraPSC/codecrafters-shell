package completion;

import org.jline.reader.Candidate;
import org.jline.reader.LineReader;
import org.jline.reader.ParsedLine;
import parser.LongestCommonPrefix;
import terminal.DisplayManager;
import terminal.TerminalContext;

import java.util.*;

public class CompletionController {
    private TerminalContext terminalContext;
    private DisplayManager displayManager;
    private int tabCount = 0;
    private String lastLine = "";

    public CompletionController(TerminalContext terminalContext, DisplayManager displayManager) {
        this.terminalContext = terminalContext;
        this.displayManager = displayManager;
    }

    void handleCompletion() {
        LineReader lineReader = terminalContext.getReader();
        String line = terminalContext.getLine();

        List<Candidate> candidates = getCompletionCandidates();
        Set<Candidate> candidateSet = new HashSet<>(candidates);

        if (candidateSet.isEmpty()) {
            displayManager.beep();

        }else if (candidateSet.size() == 1) {
            lineReader.callWidget("expand-or-complete");

        }else if (candidateSet.size() > 1) {
            handleTabPress(candidates,line);
        }
        lastLine = line;
    }

    private void applyCommonPrefix(List<Candidate> candidates){
        String word = terminalContext.getCurrentWord();
        String theLCP = LongestCommonPrefix.findLongestCommonPrefix(word, candidates);

        if (word.length() == theLCP.length()){
            displayManager.beep();
        }else {
            displayManager.updateBufferAndDisplay(theLCP);
        }
    }

    private List<Candidate> getCompletionCandidates(){
        LineReader lineReader = terminalContext.getReader();
        ParsedLine parsedLine = terminalContext.getParsedLine();

        List<Candidate> candidates = new ArrayList<>();
        CompleterFactory.create().complete(lineReader, parsedLine, candidates);

        candidates.sort(
                Comparator.comparing(Candidate::value)
        );
        return candidates;
    }

    private void handleTabPress(List<Candidate> candidates, String line){
        calculateTabCount(line);
        if (tabCount == 0){
            applyCommonPrefix(candidates);
            return;
        }
        displayManager.displayCompletions(candidates);
    }

    private void calculateTabCount(String line){
        if (!line.equals(lastLine)){
            tabCount = 0;
        }else{
            tabCount++;
        }
    }
}
