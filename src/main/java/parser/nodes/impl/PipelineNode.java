package parser.nodes.impl;

import parser.nodes.Node;

public class PipelineNode implements Node {
    private Node left;
    private Node right;

    public PipelineNode(Node left, Node right) {
        this.left = left;
        this.right = right;
    }

    public Node getLeft() {
        return left;
    }

    public Node getRight() {
        return right;
    }
}
