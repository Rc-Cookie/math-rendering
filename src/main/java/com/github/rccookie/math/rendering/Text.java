package com.github.rccookie.math.rendering;

import com.github.rccookie.util.Arguments;
import com.github.rccookie.xml.Node;

final class Text implements RenderableExpression {

    private final String text;

    Text(String text) {
        this.text = Arguments.checkNull(text, "text");
    }

    @Override
    public String toString() {
        return "text("+text+")";
    }

    @Override
    public String renderInline(RenderOptions options) {
        return text.replace('\n', ' ');
    }

    @Override
    public AsciiArt renderAsciiArt(RenderOptions options) {
        return new AsciiArt(text);
    }

    @Override
    public String renderLatex(RenderOptions options) {
        return "\\text{"+ text +"}";
    }

    @Override
    public Node renderMathMLNode(RenderOptions options) {
        Node s = new Node("ms");
        s.children.add(new com.github.rccookie.xml.Text(text));
        return s;
    }
}
