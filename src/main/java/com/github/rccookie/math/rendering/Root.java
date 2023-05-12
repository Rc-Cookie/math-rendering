package com.github.rccookie.math.rendering;

import com.github.rccookie.primitive.int2;
import com.github.rccookie.util.Arguments;
import com.github.rccookie.xml.Node;

final class Root implements RenderableExpression {

    final RenderableExpression degree;
    final RenderableExpression value;

    Root(RenderableExpression degree, RenderableExpression value) {
        this.degree = Arguments.checkNull(degree, "degree");
        this.value = Arguments.checkNull(value, "value");
    }

    @Override
    public String toString() {
        return "root("+degree+", "+value+")";
    }


    @Override
    public String renderInline(RenderOptions options) {
        String degree = this.degree.renderInline(options), value = this.value.renderInline(options);
        if(degree.isEmpty() || degree.equals("2"))
            return "\u221A" + Utils.encapsulate(value);
        String supDeg = Utils.toSuperscript(degree);
        if(supDeg != null)
            return supDeg + "\u221A" + Utils.encapsulate(value);
        return "root("+degree+", "+value+")";
    }

    @Override
    public AsciiArt renderAsciiArt(RenderOptions options) {
        String leftDiag = options.charset.orFallback("\u2572", "\\");
        String rightDiag = options.charset.orFallback("\u2571", "/");
        String vert = options.charset.orFallback("\u2502", "|");
        AsciiArt degree = this.degree.renderAsciiArt(options), value = this.value.renderAsciiArt(options);

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

    private static AsciiArt createRootShape(int height, String leftDiag, String rightDiag) {
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
    public String renderLatex(RenderOptions options) {
        return "\\sqrt["+degree.renderLatex(options)+"]{"+value.renderLatex(options)+"}";
    }

    @Override
    public Node renderMathMLNode(RenderOptions options) {
        Node degree = this.degree.renderMathMLNode(options);
        Node root;
        if(degree.text().isEmpty())
            root = new Node("msqrt");
        else {
            root = new Node("mroot");
            root.children.add(degree);
        }
        root.children.add(0, value.renderMathMLNode(options));
        return root;
    }
}
