package parser.nodes.impl;

import parser.lexer.TokenType;
import parser.nodes.Node;

public class RedirectionNode implements Node {
    private TokenType tokenType;
    private Node child;
    private String file;

    public RedirectionNode(TokenType tokenType, Node command, String file) {
        this.tokenType = tokenType;
        this.child = command;
        this.file = file;
    }

    public TokenType getTokenType() {
        return tokenType;
    }

    public Node getChild() {
        return child;
    }

    public String getFile() {
        return file;
    }
}
