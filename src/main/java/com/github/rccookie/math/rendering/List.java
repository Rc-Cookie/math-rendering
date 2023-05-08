package com.github.rccookie.math.rendering;

import java.util.Arrays;
import java.util.function.Function;

import com.github.rccookie.primitive.int2;
import com.github.rccookie.util.Arguments;

final class List implements Expression {

    final String delimiter;
    final Expression[] elements;

    List(String delimiter, Expression[] elements) {
        this.delimiter = Arguments.checkNull(delimiter, "delimiter");
        this.elements = Arguments.deepCheckNull(elements, "elements");
    }

    @Override
    public String toString() {
        return "list('"+delimiter+"', "+ Arrays.toString(elements)+")";
    }

    @Override
    public String renderInline() {
        return renderString(Expression::renderInline);
    }

    @Override
    public AsciiArt renderAscii() {
        return renderAsciiArt(Expression::renderAscii);
    }

    @Override
    public AsciiArt renderUnicode() {
        return renderAsciiArt(Expression::renderUnicode);
    }

    @Override
    public String renderLatex() {
        return renderString(Expression::renderLatex);
    }

    private AsciiArt renderAsciiArt(Function<Expression, AsciiArt> renderer) {
        if(this.elements.length == 0) return new AsciiArt("");

        AsciiArt[] elements = Arrays.stream(this.elements).map(renderer).toArray(AsciiArt[]::new);
        AsciiArt delimiter = new AsciiArt(this.delimiter);

        int h = elements[0].height();
        for(int i=1; i<elements.length; i++)
            h = Math.max(h, elements[i].height());

        AsciiArt art = AsciiArt.empty(new int2(0,h)).appendCenter(elements[0], false);
        for(int i=1; i<elements.length; i++)
            art = art.appendCenter(delimiter, false).appendCenter(elements[i], false);
        return art;
    }

    private String renderString(Function<Expression, String> renderer) {
        if(elements.length == 0) return "";
        StringBuilder str = new StringBuilder(renderer.apply(elements[0]));
        for(int i=1; i<elements.length; i++)
            str.append(delimiter).append(renderer.apply(elements[i]));
        return str.toString();
    }
}
