package parser.lexer;

import java.util.ArrayList;
import java.util.List;

public class Tokenizer {
    private List<Token> tokens = new ArrayList<>();

    public List<Token> tokenize(String line){
        ParserState state = new ParserState();

        for (int i = 0; i < line.length(); i++){
            char currentChar = line.charAt(i);

            if (!state.isOpenQuote()){
                state.handelNormalState(currentChar);
            }else if (state.getQuote() == '\"'){
                i = state.handleDoubleQuote(line, currentChar, i);
            } else if (state.getQuote() == '\'') {
                state.handelSingleQuote(currentChar);
            }
        }

        state.finishLastToken();
        buildTokens(state.getTokens());

        return tokens;
    }

    private void buildTokens(List<String> list){
        for (String s : list){
            Token token = new Token(s);
            tokens.add(token);
        }
    }
}
