package parser;

import java.util.ArrayList;
import java.util.List;

public class CommandLine {
    private String command;
    private List<String> args;

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

    List<String> getArgsWithCommand(){
        var list = new ArrayList<String>();
        list.add(getCommand());
        list.addAll(args);
        return list;
    }

    String getCommand() {
        return command;
    }

    List<String> getArgs() {
        return args;
    }
}
