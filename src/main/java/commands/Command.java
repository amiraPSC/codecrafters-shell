package commands;

import parser.CommandLine;
import parser.Parser;

public interface Command {
    void execute(Parser parser) throws Exception;
}
