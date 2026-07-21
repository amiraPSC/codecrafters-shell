package parser;

import java.util.ArrayList;
import java.util.List;

public class CommandLine {
    private String command;
    private List<String> args;
    private boolean hasBackgroundOperator = false;

    void parse(String line){
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
        setHasBackgroundOperator();
    }

    void assignCommandAndArguments(List<String> tokens){
        if (!tokens.isEmpty()) {
            command = tokens.get(0);
            args = tokens.subList(1, tokens.size());
        } else {
            command = "";
            args = new ArrayList<>();
        }
    }

    void setHasBackgroundOperator(){
        if (args.isEmpty()) return;

        if (args.getLast().equals("&")){
            hasBackgroundOperator = true;
            args.removeLast();
        }
    }

    List<String> getArgsWithCommand(){
        var list = new ArrayList<String>();
        list.add(getCommand());
        list.addAll(args);
        return list;
    }

    boolean hasBackgroundOperator() {
        return hasBackgroundOperator;
    }

    String getCommand() {
        return command;
    }

    List<String> getArgs() {
        return args;
    }
}
