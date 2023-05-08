package com.github.rccookie.math.rendering;

import java.util.Arrays;
import java.util.function.Function;

import com.github.rccookie.primitive.int2;
import com.github.rccookie.util.Arguments;

final class Grid implements Expression {

    final Expression[][] elements;

    Grid(Expression[][] elements) {
        if(elements.length == 0 || elements[0].length == 0)
            throw new IllegalArgumentException("At least one element required");
        this.elements = new Expression[Arguments.checkNull(elements, "elements").length][];
        for(int i=0; i<elements.length; i++) {
            if(elements[i].length != elements[0].length)
                throw new IllegalArgumentException("Rows must be of the same size");
            this.elements[i] = Arguments.checkNull(elements[i], "elements[" + i + "]").clone();
        }
    }

    @Override
    public String toString() {
        return "grid("+ Arrays.deepToString(elements)+")";
    }

    @Override
    public String renderInline() {
        StringBuilder str = new StringBuilder();
        for(int i=0; i<elements.length; i++) {
            if(i != 0) str.append(",");
            if(elements[i].length != 1)
                str.append("[");
            for(int j=0; j<elements[i].length; j++) {
                if(j != 0) str.append(" ");
                str.append(elements[i][j].renderInline());
            }
            if(elements[i].length != 1)
                str.append("]");
        }
        return str.toString();
    }

    @Override
    public AsciiArt renderAscii() {
        return renderGrid(Expression::renderAscii);
    }

    @Override
    public AsciiArt renderUnicode() {
        return renderGrid(Expression::renderUnicode);
    }

    private AsciiArt renderGrid(Function<Expression, AsciiArt> elementRenderer) {
        AsciiArt[][] elements = new AsciiArt[this.elements.length][this.elements[0].length];
        int[] widths = new int[elements[0].length], heights = new int[elements.length];
        for(int i=0; i<elements.length; i++) for(int j=0; j<elements[i].length; j++) {
            elements[i][j] = elementRenderer.apply(this.elements[i][j]);
            widths[j] = Math.max(widths[j], elements[i][j].width());
            heights[i] = Math.max(heights[i], elements[i][j].height());
        }

        AsciiArt art = new AsciiArt("");
        for(int i=0, yOff=0; i<elements.length; yOff+=heights[i], i++) {
            for(int j=0, xOff=0; j<elements[i].length; xOff+=widths[j]+1, j++) {
                art = art.draw(elements[i][j], new int2(xOff + (widths[j]-elements[i][j].width()+1)/2, yOff + (heights[i]-elements[i][j].height()+1)/2));
            }
        }
        return art.recalculateCenter();
    }

    @Override
    public String renderLatex() {
        StringBuilder str = new StringBuilder("\\begin{matrix}");
        for(int i=0; i<elements.length; i++) {
            for(int j=0; j<elements[i].length; j++) {
                if(j != 0) str.append("&");
                str.append(elements[i][j].renderLatex());
            }
            if(i != elements.length-1)
                str.append("\\\\");
        }
        return str.append("\\end{matrix}").toString();
    }
}
