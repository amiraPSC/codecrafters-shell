package executors;

import java.io.InputStream;
import java.io.OutputStream;
import java.io.PrintWriter;

public final class ExecutionContext {
    private final InputStream stdin;
    private final OutputStream stdout;
    private final OutputStream stderr;

    public ExecutionContext(InputStream stdin, OutputStream stdout, OutputStream stderr) {
        this.stdin = stdin;
        this.stdout = stdout;
        this.stderr = stderr;
    }

    public ExecutionContext() {
        this(System.in, System.out, System.err);
    }

    public PrintWriter getWriter(){
        return new PrintWriter(stdout, true);
    }

    public InputStream getStdin() {
        return stdin;
    }

    public ExecutionContext withInput(InputStream newInput) {
        return new ExecutionContext(newInput, stdout, stderr);
    }

    public OutputStream getStdout() {
        return stdout;
    }

    public ExecutionContext withOutput(OutputStream newOutput) {
        return new ExecutionContext(stdin, newOutput, stderr);
    }

    public OutputStream getStderr() {
        return stderr;
    }

    public ExecutionContext withError(OutputStream newError) {
        return new ExecutionContext(stdin, stdout, newError);
    }
}
