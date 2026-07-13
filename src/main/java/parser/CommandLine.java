package parser;

import java.util.ArrayList;
import java.util.List;

public class CommandLine {
    private String command;
    private List<String> args;

    public void parseCommandLine(String line){
        ParserState state = new ParserState();

        for (int i = 0; i < line.length(); i++){
            char currentChar = line.charAt(i);

            if (!state.isOpenQuote()){
                state.handelNormalState(currentChar);
            }else if (state.getQuote() == '\"'){
                i = state.handleDoubleQuote(line, currentChar, i);
            } else if (state.getQuote() == '\'') {
                state.handelSingleQuote(currentChar);
            }
        }

        state.finishLastToken();

        assignCommandAndArguments(state.getTokens());
    }

    private void assignCommandAndArguments(List<String> tokens){
        if (!tokens.isEmpty()) {
            command = tokens.get(0);
            args = tokens.subList(1, tokens.size());
        } else {
            command = "";
            args = new ArrayList<>();
        }
    }

    public List<String> getArgsWithCommand(){
        var list = new ArrayList<String>();
        list.add(getCommand());
        list.addAll(args);
        return list;
    }

    public String getCommand() {
        return command;
    }

    public List<String> getArgs() {
        return args;
    }

    @Override
    public String toString() {
        return "CommandLine{" +
                "args=" + args +
                ", command='" + command + '\'' +
                '}';
    }
}
