package com.github.rccookie.math.rendering;

import com.github.rccookie.math.Precedence;
import com.github.rccookie.util.Arguments;
import com.github.rccookie.xml.Node;
import com.github.rccookie.xml.Text;

final class NumberLiteral implements RenderableExpression {

    final String value;

    NumberLiteral(String value) {
        this.value = Arguments.checkNull(value, "value");
    }

    @Override
    public int precedence() {
        return value.startsWith("-") ? Precedence.NEGATE : Precedence.MAX;
    }

    @Override
    public String toString() {
        return value;
    }

    @Override
    public String renderInline(RenderOptions options) {
        return value.replace('\n', ' ');
    }

    @Override
    public AsciiArt renderAsciiArt(RenderOptions options) {
        return new AsciiArt(value);
    }

    @Override
    public String renderLatex(RenderOptions options) {
        return value;
    }

    @Override
    public Node renderMathMLNode(RenderOptions options) {
        Node n = new Node("mn");
        n.children.add(new Text(value));
        return n;
    }
}
