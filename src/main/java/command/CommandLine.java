package command;

import java.util.ArrayList;

public class CommandLine {
    private String command;
    private String[] args;

    public CommandLine(String input){
        parseCommandLine(input);
    }

    private void parseCommandLine(String input){
        command = input.substring(0, input.indexOf(" "));
        var list = new ArrayList<String>();
        StringBuilder builder = new StringBuilder();
        boolean openQuote = false;

        for (int i = input.indexOf(" ") +1; i < input.length(); i++){
            if (input.charAt(i) == '\'') {
                openQuote = !openQuote;
                continue;
            }
            if (!openQuote && input.charAt(i) == ' '){
                builder.append(input.charAt(i));
                list.add(builder.toString());
                builder.delete(0, builder.length()+1);
            }else {
                builder.append(input.charAt(i));
            }
        }
        args = list.toArray(new String[0]);
    }

    public String getCommand() {
        return command;
    }

    public String[] getArgs() {
        return args;
    }
}
