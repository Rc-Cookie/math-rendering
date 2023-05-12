package com.github.rccookie.math.rendering;

import java.util.Arrays;

import com.github.rccookie.primitive.int2;
import com.github.rccookie.util.Arguments;
import com.github.rccookie.xml.Node;

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
    public String renderInline(RenderOptions options) {
        StringBuilder str = new StringBuilder();
        for(int i=0; i<elements.length; i++) {
            if(i != 0) str.append(",");
            if(elements[i].length != 1)
                str.append("[");
            for(int j=0; j<elements[i].length; j++) {
                if(j != 0) str.append(" ");
                str.append(elements[i][j].renderInline(options));
            }
            if(elements[i].length != 1)
                str.append("]");
        }
        return str.toString();
    }

    @Override
    public AsciiArt renderAsciiArt(RenderOptions options) {
        AsciiArt[][] elements = new AsciiArt[this.elements.length][this.elements[0].length];
        int[] widths = new int[elements[0].length], heights = new int[elements.length];
        for(int i=0; i<elements.length; i++) for(int j=0; j<elements[i].length; j++) {
            elements[i][j] = this.elements[i][j].renderAsciiArt(options);
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
    public String renderLatex(RenderOptions options) {
        StringBuilder str = new StringBuilder("\\begin{matrix}");
        for(int i=0; i<elements.length; i++) {
            for(int j=0; j<elements[i].length; j++) {
                if(j != 0) str.append("&");
                str.append(elements[i][j].renderLatex(options));
            }
            if(i != elements.length-1)
                str.append("\\\\");
        }
        return str.append("\\end{matrix}").toString();
    }

    @Override
    public Node renderMathMLNode(RenderOptions options) {
        Node table = new Node("mtable");
        for(RenderableExpression[] row : elements) {
            Node tr = new Node("mtr");
            for(RenderableExpression e : row) {
                Node td = new Node("mtd");
                td.children.add(e.renderMathMLNode(options));
                tr.children.add(td);
            }
            table.children.add(tr);
        }
        return table;
    }
}
