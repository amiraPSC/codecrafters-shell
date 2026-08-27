package commands.impl;

import commands.Command;
import executors.ExecutionContext;
import org.jline.reader.History;
import parser.nodes.impl.CommandNode;
import utils.PathScanning;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.nio.file.StandardOpenOption;
import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;

public class HistoryCommand implements Command {
    private final History history;
    private int lastWrittenIndex = 0;

    public HistoryCommand(History history) {
        this.history = history;
    }

    @Override
    public void execute(CommandNode node, ExecutionContext context) throws IOException {
        List<String> args = node.getArgs();

        switch (args.size()){
            case 0:
                printAllHistory(context);
                break;

            case 1:
                int n = Integer.parseInt(args.getFirst());
                printNHistory(n, context);
                break;

            default:
                handleHistoryOption(args);
        }
    }

    private void handleHistoryOption(List<String> args) throws IOException {
        String option = args.getFirst();
        Path path = Paths.get(args.get(1));

        switch (option){
            case "-r":
                loadHistoryFromFile(path);
                break;
            case "-w":
                writeHistoryToFile(path, false);
                break;
            case "-a":
                writeHistoryToFile(path, true);
                break;
        }
    }

    public void loadFromHistFile() throws IOException {
        Path path = PathScanning.getHistFilePath();
        if (path != null) {
            loadHistoryFromFile(path);
        }
    }

    public void saveToHistFile() throws IOException {
        Path path = PathScanning.getHistFilePath();
        if (path != null) {
            writeHistoryToFile(path, false);
        }
    }

    private void loadHistoryFromFile(Path path) throws IOException {
        List<String> lines = Files.readAllLines(path);

        for (String line : lines) {
            if (!line.isEmpty()){
                history.add(line);
            }
        }
    }

    private void writeHistoryToFile(Path path, boolean isAppend) throws IOException {
        List<String> lines = new ArrayList<>();
        int start = isAppend ? lastWrittenIndex : 0;

        for (int i = start; i < history.size(); i++) {
            String line = history.get(i);

            if (!line.isEmpty()) {
                lines.add(line);
            }
        }

        if (isAppend){
            Files.write(path, lines, StandardOpenOption.APPEND, StandardOpenOption.CREATE);
        }else {
            Files.write(path, lines);
        }

        lastWrittenIndex = history.size();
    }

    private void printAllHistory(ExecutionContext context) {
        int i = 1;
        for (History.Entry entry : history) {
            formatPrint(entry, i++, context);
        }
    }

    private void printNHistory(int n, ExecutionContext context) {
        int i = 0;
        int limit = history.size() - n;

        for (History.Entry entry : history) {
            if (limit <= i){
                formatPrint(entry, (i+1), context);
            }
            i++;
        }
    }

    private void formatPrint(History.Entry entry, int i, ExecutionContext context) {
        context.getWriter().println( "    " + i + "  " + entry.line());
    }
}
