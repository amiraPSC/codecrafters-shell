package parser.lexer;

public class Token {
    private TokenType tokenType;
    private String value;

    public Token(String value) {
        this.value = value;
        tokenType = TokenType.getTokenType(value);
    }

    public TokenType getTokenType() {
        return tokenType;
    }

    public String getValue() {
        return value;
    }
}
