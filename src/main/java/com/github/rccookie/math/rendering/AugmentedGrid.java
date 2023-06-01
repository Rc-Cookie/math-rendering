package com.github.rccookie.math.rendering;

import com.github.rccookie.math.Precedence;
import com.github.rccookie.primitive.int2;
import com.github.rccookie.util.Arguments;
import com.github.rccookie.xml.Node;

import static com.github.rccookie.math.rendering.RenderMode.*;

final class AugmentedGrid implements RenderableExpression {

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
    public int precedence() {
        return Precedence.MID;
    }

    @Override
    public String renderInline(RenderOptions options) {
        options = options.setOutsidePrecedence(precedence());
        StringBuilder str = new StringBuilder();
        for(int i=0; i<a.elements.length; i++) {
            if(i != 0) str.append(",");
            str.append("[");
            for(RenderableExpression e : a.elements[i])
                str.append(" ").append(e.render(INLINE, options)).append(" ");
            str.append("|");
            for(RenderableExpression e : b.elements[i])
                str.append(" ").append(e.render(INLINE, options)).append(" ");
            str.append("]");
        }
        return str.toString();
    }

    @Override
    public AsciiArt renderAsciiArt(RenderOptions options) {
        options = options.setOutsidePrecedence(precedence());

        char vert = options.charset.orFallback('\u2502', '|');
        int aLen = a.elements[0].length;

        AsciiArt[][] elements = new AsciiArt[a.elements.length][aLen + b.elements[0].length];
        for(int i=0; i<elements.length; i++) {
            for(int j=0; j<aLen; j++)
                elements[i][j] = a.elements[i][j].render(ASCII_ART, options);
            for(int j=0; j<b.elements[i].length; j++)
                elements[i][j+aLen] = b.elements[i][j].render(ASCII_ART, options);
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
            for(int j=0; j<aLen; xOff+=widths[j]+1, j++) {
                int w = widths[j], h = heights[i];
                AsciiArt e = elements[i][j];
                art = art.draw(e, new int2(xOff + (w - e.width() + 1) / 2, yOff + (h - e.height() + 1) / 2));
            }
            art = art.draw(i == elements.length-1 ? new AsciiArt("|") : bar, new int2(xOff, yOff));
            xOff += 2;
            for(int j=0; j<b.elements[i].length; xOff+=widths[j+aLen]+1, j++) {
                int w = widths[j+aLen], h = heights[i];
                AsciiArt e = elements[i][j+aLen];
                art = art.draw(e, new int2(xOff + (w - e.width() + 1) / 2, yOff + (h - e.height() + 1) / 2));
            }
        }

        return art.recalculateCenter();
    }

    @Override
    public String renderLatex(RenderOptions options) {
        options = options.setOutsidePrecedence(precedence());
        StringBuilder str = new StringBuilder("\\begin{matrix}");
        for(int i=0; i<a.elements.length; i++) {
            for(RenderableExpression e : a.elements[i])
                str.append(e.render(LATEX, options)).append("&");
            str.append("\\bigm|");
            for(RenderableExpression e : b.elements[i])
                str.append("&").append(e.render(LATEX, options));
            if(i != a.elements.length-1)
                str.append("\\\\");
        }
        return str.append("\\end{matrix}").toString();
    }

    @Override
    public Node renderMathMLNode(RenderOptions options) {
        return new Middle(a,b).render(MATH_ML_NODE, options);
    }
}
