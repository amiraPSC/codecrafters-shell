package commands.impl;

import commands.Command;
import parser.Parser;

public abstract class RedirectableCommand implements Command {
    @Override
    public void execute(Parser parser) throws Exception {
        if (!parser.haveOperator()){
            executeNormally(parser);
        }else {
            handleRedirection(parser);
        }
    }

    private void handleRedirection(Parser parser) {
        switch (parser.getOperatorType()){
            case STDOUT_REDIRECT ->  stdoutRedirect(parser,false);
            case STDERR_REDIRECT -> stderrRedirect(parser, false);
            case APPEND_STDOUT ->  stdoutRedirect(parser, true);
            case APPEND_STDERR ->  stderrRedirect(parser, true);
        }
    }

    protected abstract void executeNormally(Parser parser) throws Exception;

    protected abstract void stdoutRedirect(Parser parser, boolean append);

    protected abstract void stderrRedirect(Parser parser, boolean append);
}
