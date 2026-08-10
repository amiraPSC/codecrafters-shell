package parser.lexer;

public enum TokenType {
    WORD,
    STRING,

    PIPE,              // |
    AMPERSAND,         // &

    REDIRECT_IN,       // <
    REDIRECT_OUT,      // >
    REDIRECT_APPEND,   // >>
    REDIRECT_ERR,      // 2>
    REDIRECT_ERR_APPEND;// 2>>

    public static TokenType getTokenType(String token){
        if (token.startsWith("\"") || token.startsWith("\'")) return STRING;

        switch (token){
            case "|":
                return PIPE;
            case "&":
                return AMPERSAND;
            case "<":
                return REDIRECT_IN;
            case ">", "1>":
                return REDIRECT_OUT;
            case "2>":
                return REDIRECT_ERR;
            case ">>", "1>>":
                return REDIRECT_APPEND;
            case "2>>":
                return REDIRECT_ERR_APPEND;
        }
        return WORD;
    }

    public static boolean isRedirectOperator(Token token){
        TokenType type = token.getTokenType();
        boolean haveOperator = type == REDIRECT_IN ||
                               type == REDIRECT_OUT ||
                               type == REDIRECT_APPEND ||
                               type == REDIRECT_ERR ||
                               type == REDIRECT_ERR_APPEND;
        return haveOperator;
    }

    public static boolean isTypeOperator(Token token){
        TokenType type = token.getTokenType();
        return type != TokenType.WORD
                && type != TokenType.STRING;
    }

    public static boolean isBackground(TokenType type){
        return type == AMPERSAND;
    }
}
