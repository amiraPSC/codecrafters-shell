package parser;

import java.io.File;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.util.List;

public class OperatorParser {
    private OperatorType operatorType;
    private int operatorIndex;
    private List<String> tokens;
    private File file;

    protected OperatorParser(CommandLine commandLine) {
        List<String> args = commandLine.getArgsWithCommand();
        if (haveOperator(args)) {
            setOperatorIndex(args);
            setOperatorType(args);
            setTokens(args);
            setFile(args);
        }
    }

    protected boolean haveOperator(List<String> tokens) {
        boolean haveOperator =
                tokens.contains(">") || tokens.contains("1>") ||
                tokens.contains(">>") || tokens.contains("1>>") ||
                tokens.contains("2>") || tokens.contains("2>>");

        return haveOperator;
    }

    private void setOperatorType(List<String> tokens) {
        operatorType = OperatorType.operatorType(tokens.get(operatorIndex));
    }

    private void setOperatorIndex(List<String> tokens) {
        String[] operators = {">", "1>", ">>", "1>>", "2>", "2>>"};
        for (String operator : operators) {
            int index = tokens.indexOf(operator);
            if (index != -1) {
                operatorIndex = index;
                break;
            }
        }
    }

    private void setTokens(List<String> tokens){
        this.tokens = tokens.subList(0, operatorIndex);
    }

    private void setFile(List<String> tokens) {
        try {
            this.file = Files.createFile(Path.of(tokens.get(operatorIndex + 1))).toFile();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    public OperatorType getOperatorType() {
        return operatorType;
    }

    public int getOperatorIndex() {
        return operatorIndex;
    }

    public List<String> getTokens() {
        return tokens;
    }

    public File getFile() {
        return file;
    }
}
