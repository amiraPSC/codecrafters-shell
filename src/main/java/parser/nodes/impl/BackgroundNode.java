package parser.nodes.impl;

import parser.nodes.Node;

public class BackgroundNode implements Node {
    private Node child;

    public BackgroundNode(Node command) {
        this.child = command;
    }

    public Node getChild() {
        return child;
    }
}
