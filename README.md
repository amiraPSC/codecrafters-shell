# Java Shell

A Unix-like shell implemented in Java.

## Features

* **Built-in Commands** — Supports common shell built-ins such as `cd`, `pwd`, `echo`, `type`, `complete`, `jobs`, `history`, `declare`, and `exit`.
* **External Commands** — Executes external programs through the system `PATH`.
* **Pipelines** — Supports multi-command pipelines using `|`.
* **I/O Redirection** — Supports input and output redirection using `>`, `1>`, `2>`, `1>>`, `>>`, and `2>>`.
* **Background Processes** — Supports running commands in the background using `&`.
* **Quoting** — Supports single and double quotes.
* **Parameter Expansion** — Supports shell variable expansion using `$VAR`.
* **Command History** — Supports interactive history and persistent history files.
* **Tab Completion** — Supports command and executable completion.

## Architecture

The shell is organized around separate components for parsing, command execution, pipelines, built-ins, terminal interaction, and process management.

- **Parser** — Tokenizes and parses shell input into executable commands.
- **Executors** — Handles built-in commands, external processes, pipelines, and background execution.
- **Terminal** — Provides interactive input, history, and tab completion through JLine.
- **Process Management** — Handles process streams, I/O redirection, and background jobs.

## Technologies

* Java 25
* Maven
* JLine

## Running the Shell

### Requirements

* Java 25
* Maven

### Build

```bash
mvn package
```

### Run

```bash
java --enable-native-access=ALL-UNNAMED --enable-preview -jar target/codecrafters-shell.jar
```

## CodeCrafters

This project was built as part of the [CodeCrafters Build Your Own Shell](https://codecrafters.io/challenges/shell) challenge.
