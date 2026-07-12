package parser;

import org.jline.reader.LineReader;
import org.jline.reader.ParsedLine;

import java.util.ArrayList;
import java.util.List;

public class CommandLine {
    private String command;
    private List<String> args;
    private Reader reader;

    public CommandLine(String input){
        parseCommandLine(input);
    }

    public CommandLine(Reader reader){
        this.reader = reader;
    }

    public void parseCommandLine(){
        args = new ArrayList<>();

        ParsedLine parsedLine = reader.getParse();
        List<String> words = parsedLine.words();

        args.addAll(words);
        args.remove(0);
        command = words.get(0);
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

            if (!openQuote){
                if (isEscaping){
                    builder.append(current);
                    isEscaping = false;
                    continue;
                }

                if (!isEscaping && (current == '\"' || current == '\'')){
                    openQuote = true;
                    quote = current;
                    tokenStarted = true;
                    continue;
                }

                if (current == '\\' && !isEscaping){
                    isEscaping = true;
                    continue;
                }

                if (Character.isWhitespace(current)){
                    if (tokenStarted){
                        list.add(builder.toString());
                        builder.setLength(0);
                        tokenStarted = false;
                    }
                    continue;
                }

                builder.append(current);
                tokenStarted = true;

            }else if (quote == '\"'){
                if (current == '\\' && i+1 < input.length()){
                    char nextChar = input.charAt(i+1);
                    if (nextChar == '\\' || nextChar == '\"' || nextChar == '$' || nextChar == '`'){
                        builder.append(nextChar);
                        tokenStarted = true;
                        i++;
                        continue;
                    }
                }

                if (current == '\"'){
                    openQuote = false;
                    continue;
                }

                builder.append(current);
                tokenStarted = true;

            } else if (quote == '\'') {
                if (current == '\''){
                    openQuote = false;
                    continue;
                }

                builder.append(current);
                tokenStarted = true;
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

    @Override
    public String toString() {
        return "CommandLine{" +
                "args=" + args +
                ", command='" + command + '\'' +
                '}';
    }
}
