package parser;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

public class CommandLine {
    private String command;
    private List<String> args;

    public CommandLine(String input){
        parseCommandLine(input);
    }

    private void parseCommandLine(String input){
        var list = new ArrayList<String>();
        StringBuilder builder = new StringBuilder();
        boolean openQuote = false;
        boolean tokenStarted = false;
        boolean isEscaping  = false;

        char quote = '\'';

        for (int i = 0; i < input.length(); i++){
            char current = input.charAt(i);

            if (current == '\\' && !isEscaping){
                isEscaping = true;
                continue;
            }

            if (!isEscaping) {

                if (current == '\\' && openQuote && quote == '\"' && i+1 < input.length()){
                    char nextChar = input.charAt(i+1);
                    if (nextChar == '\\' || nextChar == '\"'){
                        isEscaping = true;
                        continue;
                    }
                }

                if ((current == '\'' || current == '\"') && !openQuote) {
                    quote = current;
                }

                if (current == quote) {
                    openQuote = !openQuote;
                    tokenStarted = true;
                    continue;
                }

                if (!openQuote && Character.isWhitespace(current)) {
                    if (tokenStarted) {
                        list.add(builder.toString());
                        builder.setLength(0);
                        tokenStarted = false;
                    }
                    continue;
                }

                builder.append(current);
                tokenStarted = true;
            }else {
                builder.append(current);
                isEscaping = false;
            }
        }

        if (tokenStarted) {
            list.add(builder.toString());
        }

        if (!list.isEmpty()) {
            command = list.get(0);
            args = list.subList(1, list.size());
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
}
