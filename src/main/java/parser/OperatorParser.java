package parser;

import java.util.List;

public class OperatorParser{
    private CommandLine commandLine;
    private OperatorType operatorType;
    private int operatorIndex;
    private List<String> tokens;
    private String fileName;

    OperatorParser(CommandLine commandLine) {
        this.commandLine = commandLine;
        if (haveOperator()){
            List<String> args = commandLine.getArgsWithCommand();
            setOperatorIndex(args);
            setOperatorType(args);
            setTokens(args);
            setFileName(args);
        }
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

    private void setFileName(List<String> tokens) {
        fileName = tokens.get(operatorIndex + 1);
    }

    OperatorType getOperatorType() {
        return operatorType;
    }

    List<String> getTokens() {
        return tokens;
    }

    String getFileName() {
        return fileName;
    }

    boolean haveOperator() {
        List<String> tokens = getTokens();
        boolean haveOperator =
                tokens.contains(">") || tokens.contains("1>") ||
                        tokens.contains(">>") || tokens.contains("1>>") ||
                        tokens.contains("2>") || tokens.contains("2>>");

        return haveOperator;
    }
}
