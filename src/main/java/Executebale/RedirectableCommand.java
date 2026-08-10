package Executebale;

import commands.Command;
import parser.Parser2;

public abstract class RedirectableCommand implements Command {
    @Override
    public void execute(Parser2 parser2) throws Exception {
        if (!parser2.haveOperator()){
            executeNormally(parser2);
        }else {
            handleRedirection(parser2);
        }
    }

    private void handleRedirection(Parser2 parser2) {
        switch (parser2.getOperatorType()){
            case STDOUT_REDIRECT ->  stdoutRedirect(parser2,false);
            case STDERR_REDIRECT -> stderrRedirect(parser2, false);
            case APPEND_STDOUT ->  stdoutRedirect(parser2, true);
            case APPEND_STDERR ->  stderrRedirect(parser2, true);
        }
    }

    protected abstract void executeNormally(Parser2 parser2) throws Exception;

    protected abstract void stdoutRedirect(Parser2 parser2, boolean append);

    protected abstract void stderrRedirect(Parser2 parser2, boolean append);
}
