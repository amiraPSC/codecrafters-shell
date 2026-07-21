package parser;

import java.util.ArrayList;
import java.util.List;

public class Parser {
    private CommandLine commandLine;
    private OperatorParser operatorParser;

    public Parser() {
        this.commandLine = new CommandLine();
    }

    public void parse(String line){
        commandLine.parse(line);
        operatorParser = new OperatorParser(commandLine);
    }

    public String getCommand() {
        return commandLine.getCommand();
    }

    public boolean haveOperator(){
        return operatorParser.haveOperator();
    }

    public OperatorType getOperatorType() {
        return operatorParser.getOperatorType();
    }

    public List<String> getTokens() {
        if (haveOperator()){
            return operatorParser.getTokens();
        }
        return commandLine.getArgs();
    }

    public List<String> getArgsWithCommand(){
        var list = new ArrayList<String>();
        list.add(getCommand());
        list.addAll(getTokens());
        return list;
    }

    public boolean hasBackgroundOperator(){
        return commandLine.hasBackgroundOperator();
    }

    public String getFileName() {
        return operatorParser.getFileName();
    }}
