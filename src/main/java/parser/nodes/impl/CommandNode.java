package parser.nodes.impl;

import parser.nodes.Node;

import java.util.ArrayList;
import java.util.List;

public class CommandNode implements Node {
    private String command;
    private List<String> args;

    public CommandNode(String command, List<String> args) {
        this.command = command;
        this.args = args;
    }

    public CommandNode(String command) {
        this.command = command;
        args = new ArrayList<>();
    }

    public void add(String value){
        args.add(value);
    }

    public void addAll(List<String> list){
        args.addAll(list);
    }

    public String getCommand() {
        return command;
    }

    public List<String> getArgs() {
        return args;
    }

    public List<String> getCommandWithArgs(){
        var list = new ArrayList<String>();
        list.add(command);
        list.addAll(args);
        return list;
    }
}
