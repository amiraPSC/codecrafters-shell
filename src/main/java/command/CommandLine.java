package command;

import java.util.ArrayList;

public class CommandLine {
    private String command;
    private String[] args;

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
            }

            if (!isEscaping) {

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
            args = list.subList(1, list.size()).toArray(new String[0]);
        } else {
            command = "";
            args = new String[0];
        }
    }

    public String getCommand() {
        return command;
    }

    public String[] getArgs() {
        return args;
    }
}
