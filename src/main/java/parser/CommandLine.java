package parser;

import parser.lexer.ParserState;

import java.util.ArrayList;
import java.util.List;

public class CommandLine{
    private String command;
    private List<String> Tokens;
    private boolean hasBackgroundOperator = false;

    public void parse(String line){
        ParserState state = new ParserState();

        for (int i = 0; i < line.length(); i++){
            char currentChar = line.charAt(i);

            if (!state.isOpenQuote()){
                //state.handelNormalState(currentChar);
            }else if (state.getQuote() == '\"'){
                //i = state.handleDoubleQuote(line, currentChar, i);
            } else if (state.getQuote() == '\'') {
                //state.handelSingleQuote(currentChar);
            }
        }

        state.finishLastToken();
        assignCommandAndArguments(state.getTokens());
        setHasBackgroundOperator();
    }

    void assignCommandAndArguments(List<String> tokens){
        if (!tokens.isEmpty()) {
            command = tokens.get(0);
            Tokens = tokens.subList(1, tokens.size());
        } else {
            command = "";
            Tokens = new ArrayList<>();
        }
    }

    void setHasBackgroundOperator(){
        if (Tokens.isEmpty()) return;

        if (Tokens.getLast().equals("&")){
            hasBackgroundOperator = true;
            Tokens.removeLast();
        }
    }

    public List<String> getArgsWithCommand(){
        var list = new ArrayList<String>();
        list.add(getCommand());
        list.addAll(Tokens);
        return list;
    }

    boolean hasBackgroundOperator() {
        return hasBackgroundOperator;
    }

    String getCommand() {
        return command;
    }

    List<String> getTokens() {
        return Tokens;
    }

}
