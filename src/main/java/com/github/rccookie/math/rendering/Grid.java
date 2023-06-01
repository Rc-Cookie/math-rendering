package com.github.rccookie.math.rendering;

import java.util.Arrays;

import com.github.rccookie.math.Precedence;
import com.github.rccookie.primitive.int2;
import com.github.rccookie.util.Arguments;
import com.github.rccookie.xml.Node;

import static com.github.rccookie.math.rendering.RenderMode.*;

final class Grid implements RenderableExpression {

    final RenderableExpression[][] elements;

    Grid(RenderableExpression[][] elements) {
        if(elements.length == 0 || elements[0].length == 0)
            throw new IllegalArgumentException("At least one element required");
        this.elements = new RenderableExpression[Arguments.checkNull(elements, "elements").length][];
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
    public int precedence() {
        if(elements.length == 1 && elements[0].length == 1)
            return elements[0][0].precedence();
        return Precedence.GRID;
    }

    @Override
    public String renderInline(RenderOptions options) {
        options = options.setOutsidePrecedence(Precedence.MIN);

        String comma = options.spaceMode == RenderOptions.SpaceMode.COMPACT ? "," : ", ";
        StringBuilder str = new StringBuilder();
        for(int i=0; i<elements.length; i++) {
            if(i != 0) str.append(comma);
            for(int j=0; j<elements[i].length; j++) {
                if(j != 0) str.append("  ");
                str.append(elements[i][j].render(INLINE, options));
            }
        }
        return str.toString();
    }

    @Override
    public AsciiArt renderAsciiArt(RenderOptions options) {
        options = options.setOutsidePrecedence(Precedence.MIN);

        AsciiArt[][] elements = new AsciiArt[this.elements.length][this.elements[0].length];
        int[] widths = new int[elements[0].length], heights = new int[elements.length];
        for(int i=0; i<elements.length; i++) for(int j=0; j<elements[i].length; j++) {
            elements[i][j] = this.elements[i][j].render(ASCII_ART, options);
            widths[j] = Math.max(widths[j], elements[i][j].width());
            heights[i] = Math.max(heights[i], elements[i][j].height());
        }

        AsciiArt art = new AsciiArt("");
        for(int i=0, yOff=0; i<elements.length; yOff+=heights[i], i++) {
            for(int j=0, xOff=0; j<elements[i].length; xOff+=widths[j]+2, j++) {
                art = art.draw(elements[i][j], new int2(xOff + (widths[j]-elements[i][j].width()+1)/2, yOff + (heights[i]-elements[i][j].height()+1)/2));
            }
        }
        return art.recalculateCenter();
    }

    @Override
    public String renderLatex(RenderOptions options) {
        options = options.setOutsidePrecedence(Precedence.MIN);
        StringBuilder str = new StringBuilder("\\begin{matrix}");
        for(int i=0; i<elements.length; i++) {
            for(int j=0; j<elements[i].length; j++) {
                if(j != 0) str.append("&");
                str.append(elements[i][j].render(LATEX, options));
            }
            if(i != elements.length-1)
                str.append("\\\\");
        }
        return str.append("\\end{matrix}").toString();
    }

    @Override
    public Node renderMathMLNode(RenderOptions options) {
        options = options.setOutsidePrecedence(Precedence.MIN);
        Node table = new Node("mtable");
        for(RenderableExpression[] row : elements) {
            Node tr = new Node("mtr");
            for(RenderableExpression e : row) {
                Node td = new Node("mtd");
                td.children.add(e.render(MATH_ML_NODE, options));
                tr.children.add(td);
            }
            table.children.add(tr);
        }
        return table;
    }
}
