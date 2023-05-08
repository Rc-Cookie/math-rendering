package com.github.rccookie.math.rendering;

import com.github.rccookie.primitive.int2;
import com.github.rccookie.util.Arguments;
import com.github.rccookie.xml.Node;

final class Root implements Expression {

    final Expression degree;
    final Expression value;

    Root(Expression degree, Expression value) {
        this.degree = Arguments.checkNull(degree, "degree");
        this.value = Arguments.checkNull(value, "value");
    }

    @Override
    public String toString() {
        return "root("+degree+", "+value+")";
    }


    @Override
    public String renderInline() {
        String degree = this.degree.renderInline(), value = this.value.renderInline();
        if(degree.isEmpty() || degree.equals("2"))
            return "\u221A" + Utils.encapsulate(value);
        String supDeg = Utils.toSuperscript(degree);
        if(supDeg != null)
            return supDeg + "\u221A" + Utils.encapsulate(value);
        return "root("+degree+", "+value+")";
    }

    @Override
    public AsciiArt renderAscii() {
        return renderArt(degree.renderAscii(), value.renderAscii(), '\\', '/', '|');
    }

    @Override
    public AsciiArt renderUnicode() {
        return renderArt(degree.renderUnicode(), value.renderUnicode(), '\u2572', '\u2571', '\u2502');
    }

    @Override
    public AsciiArt renderAscii(CharacterSet charset) {
        return renderArt(degree.renderAscii(charset), value.renderAscii(charset), charset.orFallback('\u2572', '\\'), charset.orFallback('\u2571', '/'), charset.orFallback('\u2502', '|'));
    }

    private static AsciiArt renderArt(AsciiArt degree, AsciiArt value, char leftDiag, char rightDiag, char vert) {
        AsciiArt shape = createRootShape(value.height(), leftDiag, rightDiag);
        AsciiArt root = shape.appendTop(value);
        if(shape.height() >= 3)
            root = root.appendTop(new AsciiArt(shape.height() / 3, i -> vert+""));
        root = root.draw(new AsciiArt("_".repeat(value.width())), new int2(shape.width(), -1));

        if(value.width() == 0) return root;
        int barHeight = barHeight(shape.height());
        int2 pos = new int2(Math.min(0, 2*barHeight-1 - degree.width()), root.height() - barHeight - 1 - degree.height()+1);
        return root.draw(degree, pos);
    }

    private static AsciiArt createRootShape(int height, char leftDiag, char rightDiag) {
        if(height <= 1) return new AsciiArt("\\"+rightDiag);
        int barHeight = barHeight(height);
        int minBarI = height - barHeight;
        return new AsciiArt(height, i -> {
            if(i < minBarI) return Utils.blank(barHeight + height-i - 1) + rightDiag;
            return Utils.blank(i-minBarI)+leftDiag+Utils.blank(2*(height-i-1))+rightDiag;
        });
    }

    private static int barHeight(int height) {
        return Math.max((height+1) / 3, 1);
    }

    @Override
    public String renderLatex() {
        return "\\sqrt["+degree.renderLatex()+"]{"+value.renderLatex()+"}";
    }

    @Override
    public Node renderMathMLNode() {
        Node degree = this.degree.renderMathMLNode();
        Node root;
        if(degree.text().isEmpty())
            root = new Node("msqrt");
        else {
            root = new Node("mroot");
            root.children.add(degree);
        }
        root.children.add(0, value.renderMathMLNode());
        return root;
    }
}
