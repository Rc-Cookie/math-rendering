package com.github.rccookie.math.rendering;

import java.util.function.Function;

import com.github.rccookie.primitive.int2;
import com.github.rccookie.util.Arguments;

final class AugmentedGrid implements Expression {

    final Grid a, b;

    AugmentedGrid(Grid a, Grid b) {
        this.a = Arguments.checkNull(a, "a");
        this.b = Arguments.checkNull(b, "b");
        if(a.elements.length != b.elements.length)
            throw new IllegalArgumentException("Incompatible grid sizes");
    }

    @Override
    public String toString() {
        return "augmented("+a+", "+b+")";
    }

    @Override
    public String renderInline() {
        StringBuilder str = new StringBuilder();
        for(int i=0; i<a.elements.length; i++) {
            if(i != 0) str.append(",");
            str.append("[");
            for(Expression e : a.elements[i])
                str.append(e.renderInline()).append(" ");
            str.append("|");
            for(Expression e : b.elements[i])
                str.append(" ").append(e.renderInline());
            str.append("]");
        }
        return str.toString();
    }

    @Override
    public AsciiArt renderAscii() {
        return renderGrid(Expression::renderAscii, '|');
    }

    @Override
    public AsciiArt renderUnicode() {
        return renderGrid(Expression::renderUnicode, '\u2502');
    }

    private AsciiArt renderGrid(Function<Expression, AsciiArt> elementRenderer, char vert) {
        AsciiArt[][] elements = new AsciiArt[a.elements.length][a.elements[0].length + b.elements[0].length];
        for(int i=0; i<elements.length; i++) {
            for(int j=0; j<a.elements[i].length; j++)
                elements[i][j] = elementRenderer.apply(a.elements[i][j]);
            for(int j=0; j<b.elements[i].length; j++)
                elements[i][j+a.elements[i].length] = elementRenderer.apply(b.elements[i][j]);
        }

        int[] widths = new int[elements[0].length], heights = new int[elements.length];
        for(int i=0; i<elements.length; i++) for(int j=0; j<elements[i].length; j++) {
            widths[j] = Math.max(widths[j], elements[i][j].width());
            heights[i] = Math.max(heights[i], elements[i][j].height());
        }

        AsciiArt art = new AsciiArt("");
        AsciiArt bar = new AsciiArt(""+vert);

        for(int i=0, yOff=0; i<elements.length; yOff+=heights[i], i++) {
            int xOff = 0;
            for(int j=0; j<a.elements[i].length; xOff+=widths[j]+1, j++) {
                art = art.draw(elements[i][j], new int2(xOff + (widths[j]-elements[i][j].width()+1)/2, yOff + (heights[i]-elements[i][j].height()+1)/2));
            }
            art = art.draw(i == elements.length-1 ? new AsciiArt("|") : bar, new int2(xOff, yOff));
            xOff++;
            for(int j=0; j<b.elements[i].length; xOff+=widths[j+a.elements[i].length]+1, j++) {
                art = art.draw(elements[i][j], new int2(xOff + (widths[j+a.elements[i].length]-elements[i][j].width()+1)/2, yOff + (heights[i]-elements[i][j].height()+1)/2));
            }
        }

        return art;
    }

    @Override
    public String renderLatex() {
        StringBuilder str = new StringBuilder("\\begin{matrix}");
        for(int i=0; i<a.elements.length; i++) {
            for(Expression e : a.elements[i])
                str.append(e.renderInline()).append("&");
            str.append("\\bigm|");
            for(Expression e : b.elements[i])
                str.append("&").append(e.renderInline());
            if(i != a.elements.length-1)
                str.append("\\\\");
        }
        return str.append("\\end{matrix}").toString();
    }
}
