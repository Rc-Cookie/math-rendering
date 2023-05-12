package com.github.rccookie.math.rendering;

import java.util.Arrays;
import java.util.function.Function;

import com.github.rccookie.util.Arguments;
import com.github.rccookie.xml.Node;

final class List implements RenderableExpression {

    final RenderableExpression delimiter;
    final RenderableExpression[] elements;

    List(RenderableExpression delimiter, RenderableExpression[] elements) {
        this.delimiter = Arguments.checkNull(delimiter, "delimiter");
        this.elements = Arguments.deepCheckNull(elements, "elements");
    }

    @Override
    public String toString() {
        return "list('"+delimiter+"', "+ Arrays.toString(elements)+")";
    }

    @Override
    public String renderInline(RenderOptions options) {
        return renderString(renderableExpression -> renderableExpression.renderInline(options));
    }

    @Override
    public AsciiArt renderAsciiArt(RenderOptions options) {
        if(this.elements.length == 0) return new AsciiArt("");

        AsciiArt delimiter = this.delimiter.renderAsciiArt(options);
        AsciiArt art = elements[0].renderAsciiArt(options);
        for(int i=1; i<elements.length; i++)
            art = art.appendCenter(delimiter).appendCenter(elements[i].renderAsciiArt(options));
        return art;
    }

    @Override
    public String renderLatex(RenderOptions options) {
        return renderString(renderableExpression -> renderableExpression.renderLatex(options));
    }

    @Override
    public Node renderMathMLNode(RenderOptions options) {
        Node list = new Node("mrow");
        if(elements.length == 0) return list;

        list.children.add(elements[0].renderMathMLNode(options));
        if(elements.length == 1) return list;

        Node delimiter = this.delimiter.renderMathMLNode(options);
        for(int i=1; i<elements.length; i++) {
            list.children.add(delimiter.clone());
            list.children.add(elements[i].renderMathMLNode(options));
        }
        return list;
    }

    private String renderString(Function<RenderableExpression, String> renderer) {
        if(elements.length == 0) return "";
        StringBuilder str = new StringBuilder(renderer.apply(elements[0]));
        for (int i = 1; i < elements.length; i++)
            str.append(delimiter).append(renderer.apply(elements[i]));
        return str.toString();
    }
}
