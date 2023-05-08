package com.github.rccookie.math.rendering;

import com.github.rccookie.util.Arguments;
import com.github.rccookie.xml.Node;
import com.github.rccookie.xml.Text;

final class Literal implements Expression {

    final boolean operator;
    final String value;

    Literal(boolean operator, String value) {
        this.operator = operator;
        this.value = Arguments.checkNull(value, "value");
    }

    @Override
    public String toString() {
        return value;
    }

    @Override
    public String renderInline() {
        return value.replace('\n', ' ');
    }

    @Override
    public AsciiArt renderAscii() {
        return new AsciiArt(value);
    }

    @Override
    public AsciiArt renderUnicode() {
        return new AsciiArt(value);
    }

    @Override
    public AsciiArt renderAscii(CharacterSet charset) {
        return new AsciiArt(value);
    }

    @Override
    public String renderLatex() {
        return value;
    }

    @Override
    public Node renderMathMLNode() {
        Node n = new Node(operator ? "mo" : "mi");
        n.children.add(new Text(value));
        return n;
    }
}
