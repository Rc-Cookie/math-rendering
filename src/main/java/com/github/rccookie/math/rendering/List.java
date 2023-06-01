package com.github.rccookie.math.rendering;

import java.util.Arrays;

import com.github.rccookie.math.Precedence;
import com.github.rccookie.util.Arguments;
import com.github.rccookie.xml.Node;

import static com.github.rccookie.math.rendering.RenderMode.*;
import static com.github.rccookie.math.rendering.RenderMode.MATH_ML_NODE;

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
    public int precedence() {
        if(elements.length == 0)
            return Precedence.MAX;
        if(elements.length == 1)
            return elements[0].precedence();
        return delimiter.precedence();
    }

    @Override
    public String renderInline(RenderOptions options) {
        return renderString(INLINE, options);
    }

    @Override
    public AsciiArt renderAsciiArt(RenderOptions options) {
        if(this.elements.length == 0) return new AsciiArt("");
        if(this.elements.length == 1) return elements[0].render(ASCII_ART, options);

        AsciiArt delimiter = this.delimiter.render(ASCII_ART, options);
        AsciiArt[] elements = Arrays.stream(this.elements).map(e -> e.render(ASCII_ART, options.setOutsidePrecedence(precedence() + 1))).toArray(AsciiArt[]::new);
        if(!Utils.hasPadding(delimiter.toString()) && (options.spaceMode == RenderOptions.SpaceMode.FORCE ||
                (options.spaceMode == RenderOptions.SpaceMode.AUTO && Arrays.stream(elements).anyMatch(e -> e.size().area() != 1))))
            delimiter = delimiter.appendCenter(new AsciiArt(" "));

        AsciiArt art = elements[0];
        for(int i=1; i<elements.length; i++)
            art = art.appendCenter(delimiter).appendCenter(elements[i]);
        return art;
    }

    @Override
    public String renderLatex(RenderOptions options) {
        return renderString(LATEX, options);
    }

    @Override
    public Node renderMathMLNode(RenderOptions options) {
        Node list = new Node("mrow");
        if(elements.length == 0) return list;

        Node delimiter = this.delimiter.render(MATH_ML_NODE, options);
        if(elements.length != 1)
            options = options.setOutsidePrecedence(precedence() + 1);

        list.children.add(elements[0].render(MATH_ML_NODE, options));
        if(elements.length == 1) return list;

        for(int i=1; i<elements.length; i++) {
            list.children.add(delimiter.clone());
            list.children.add(elements[i].render(MATH_ML_NODE, options));
        }
        return list;
    }

    private String renderString(RenderMode<String> renderMode, RenderOptions options) {
        if(elements.length == 0) return "";
        if(elements.length == 1) return elements[0].render(renderMode, options);

        String delimiter = this.delimiter.render(renderMode, options);
        options = options.setOutsidePrecedence(precedence() + 1);

        StringBuilder str = new StringBuilder(elements[0].render(renderMode, options));
        for (int i = 1; i < elements.length; i++)
            str.append(delimiter).append(elements[i].render(renderMode, options));
        return str.toString();
    }
}
