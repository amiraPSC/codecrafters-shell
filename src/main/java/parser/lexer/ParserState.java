package parser.lexer;

import commands.impl.DeclareCommand;

import java.util.ArrayList;
import java.util.List;

public class ParserState{
    private StringBuilder builder = new StringBuilder();
    private StringBuilder variableBuilder = new StringBuilder();
    private List<String> tokens = new ArrayList<>();

    private boolean isEscape = false;
    private boolean isVariable = false;
    private boolean openQuote = false;
    private boolean tokenStarted = false;
    private char quote = '\'';

    protected void handelNormalState(char currentChar){
        if (handleEscaping(currentChar))
            return;

        if (handleQuoteOpening(currentChar))
            return;

        if (currentChar == '\\'){
            startEscaping();
            return;
        }

        if (handleWhitespace(currentChar) || currentChar == '$') {
            handleVariable(currentChar);
            return;
        }

        append(currentChar);
    }

    protected int handleDoubleQuote(String line, char currentChar, int i){
        int index = i;
        if (currentChar == '\\' && i+1 < line.length()){
            char nextChar = line.charAt(i+1);
            if (nextChar == '\\' || nextChar == '\"' || nextChar == '$' || nextChar == '`'){
                append(nextChar);
                index = i + 1;
                return index;
            }
        }

        if (currentChar == '\"'){
            openQuote = false;
            return index;
        }

        append(currentChar);
        return index;
    }

    protected void handelSingleQuote(char currentChar){
        if (currentChar == '\''){
            openQuote = false;
            return;
        }
        append(currentChar);
    }

    private void append(char c){
        if (isVariable && c == '{') return;
        if (isVariable && c == '}'){
            finishTokenVariable();
            return;
        }

        if (isVariable && c != '$') {
            variableBuilder.append(c);
            tokenStarted = true;
            return;
        }

        builder.append(c);
        tokenStarted = true;
    }

    private boolean handleEscaping(char currentChar){
        if (!isEscape) return false;

        builder.append(currentChar);
        isEscape = false;
        return true;
    }

    private boolean handleQuoteOpening(char currentChar){
        if (!isEscape && (currentChar == '\"' || currentChar == '\'')){
            openQuote = true;
            quote = currentChar;
            tokenStarted = true;
            return true;
        }
        return false;
    }

    private boolean handleWhitespace(char currentChar){
        if (Character.isWhitespace(currentChar)){
            if (tokenStarted){
                finishToken();
            }
            return true;
        }
        return false;
    }

    private void finishToken(){
        finishTokenVariable();

        if (builder.isEmpty()){
            tokenStarted = false;
            return;
        }

        tokens.add(builder.toString());
        builder.setLength(0);
        tokenStarted = false;
    }

    public void finishLastToken(){
        finishTokenVariable();
        if (builder.isEmpty())return;
        
        if (tokenStarted) {
            tokens.add(builder.toString());
        }
    }

    private void startEscaping(){
        isEscape = true;
    }

    private void handleVariable(char currentChar){
        if (isVariable && (currentChar == '$' || currentChar == '}')){
            expandVariable();
        }else if (!isVariable && currentChar == '$'){
            startTokenVariable();
        }
    }

    private void expandVariable(){
        String value = null;
        if (DeclareCommand.checkVariable(variableBuilder.toString())){
            value = DeclareCommand.getVariable(variableBuilder.toString());
        }

        variableBuilder.setLength(0);
        if (value != null){
            builder.append(value);
        }
    }

    private void startTokenVariable(){
        isVariable = true;
    }

    private void finishTokenVariable(){
        expandVariable();
        isVariable = false;
    }



    public char getQuote() {
        return quote;
    }

    public boolean isOpenQuote() {
        return openQuote;
    }

    public List<String> getTokens() {
        return tokens;
    }
}
