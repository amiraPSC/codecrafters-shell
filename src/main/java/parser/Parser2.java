package parser;

import java.util.ArrayList;
import java.util.List;

public class Parser2 {
    private CommandLine commandLine;
    private boolean hasPipeOperator;
    private OperatorParser operatorParser;
    private List<CommandLine> commandsList;

    public Parser2() {
        this.commandLine = new CommandLine();
    }

    public void parse(String line){
        if (hasPipeOperator(line)){
            commandsList = splitPipeline(line);
        }else {
            commandsList = new ArrayList<>();
            commandLine.parse(line);
            operatorParser = new OperatorParser(commandLine);
        }
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
        return commandLine.getTokens();
    }

    public List<String> getArgsWithCommand(){
        var list = new ArrayList<String>();
        list.add(getCommand());
        list.addAll(getTokens());
        return list;
    }

    public boolean isEmpty(){
        return getTokens().isEmpty();
    }

    private List<CommandLine> splitPipeline(String line){
        List<CommandLine> list = new ArrayList<>();
        String[] splits = line.split("\\s*\\|\\s*");
        for (String l : splits){
            CommandLine c = new CommandLine();
            c.parse(l);
            list.add(c);
        }
        return list;
    }

    private boolean hasPipeOperator(String line){
        boolean result = line.contains("|");
        hasPipeOperator = result;
        return result;
    }

    public boolean hasPipeOperator(){
        return hasPipeOperator;
    }

    public boolean hasBackgroundOperator(){
        return commandLine.hasBackgroundOperator();
    }

    public String getFileName() {
        return operatorParser.getFileName();
    }

    public List<CommandLine> getCommandsList() {
        return commandsList;
    }
}
