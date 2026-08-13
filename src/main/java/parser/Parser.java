package parser;

import parser.lexer.Token;
import parser.lexer.TokenType;
import parser.lexer.Tokenizer;
import parser.nodes.Node;
import parser.nodes.impl.BackgroundNode;
import parser.nodes.impl.CommandNode;
import parser.nodes.impl.PipelineNode;
import parser.nodes.impl.RedirectionNode;

import java.util.ArrayList;
import java.util.List;

public class Parser {
    private List<Token> tokens;
    private int pointer;

    public Node parse(String line){
        Tokenizer tokenizer = new Tokenizer();
        tokens = tokenizer.tokenize(line);
        return parseBackground();
    }

    //Node parseExpression(){}

    Node parseBackground(){
        Node node = parsePipeLine();
        Token token = currentToken();

        if (token != null){
            TokenType type = token.getTokenType();

            if (TokenType.isBackground(type)) {
                return new BackgroundNode(node);
            }
        }

        return node;
    }

    Node parsePipeLine(){
        Node leftNode = parseRedirect();
        Token token = currentToken();

        if (token != null){
            TokenType type = token.getTokenType();

            if (type == TokenType.PIPE){
                pointer++;
                Node rightNode = parsePipeLine();
                return new PipelineNode(leftNode, rightNode);
            }
        }

        return leftNode;
    }

    Node parseRedirect(){
        Node node = parseSimpleCommand();
        Token token = currentToken();

        if (token != null && TokenType.isRedirectOperator(token)){
            pointer++;
            Token token2 = tokens.get(pointer);
            pointer++;
            return new RedirectionNode(token.getTokenType() ,node, token2.getValue());
        }

        return node;
    }

    Node parseSimpleCommand(){
        CommandNode node = new CommandNode(tokens.get(pointer).getValue());
        var args = new ArrayList<String>();

        pointer++;
        for (int i = pointer; i < tokens.size(); i++){
            Token token = tokens.get(i);
            if (!TokenType.isTypeOperator(token)){
                args.add(token.getValue());
            }else {
                pointer = i;
                break;
            }
        }

        node.addAll(args);
        return node;
    }

    private Token currentToken() {
        if (pointer >= tokens.size()) {
            return null;
        }
        return tokens.get(pointer);
    }
}
