package parser;

import java.util.ArrayList;
import java.util.List;

public class ParserState{
    private StringBuilder builder;
    private List<String> tokens;

    private boolean isEscape;
    private boolean openQuote;
    private boolean tokenStarted;
    private char quote;

    public ParserState() {
        this.builder = new StringBuilder();
        this.tokens = new ArrayList<>();
        this.isEscape = false;
        this.openQuote = false;
        this.tokenStarted = false;
        this.quote = '\'';
    }

    protected void handelNormalState(char currentChar){
        if (handleEscaping(currentChar))
            return;

        if (handleQuoteOpening(currentChar))
            return;

        if (currentChar == '\\'){
            startEscaping();
            return;
        }

        if (handleWhitespace(currentChar))
            return;

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
        tokens.add(builder.toString());
        builder.setLength(0);
        tokenStarted = false;
    }

    public void finishLastToken(){
        if (tokenStarted) {
            tokens.add(builder.toString());
        }
    }

    private void startEscaping(){
        isEscape = true;
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
