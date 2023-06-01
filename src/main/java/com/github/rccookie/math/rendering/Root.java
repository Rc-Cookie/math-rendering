package com.github.rccookie.math.rendering;

import com.github.rccookie.math.Precedence;
import com.github.rccookie.primitive.int2;
import com.github.rccookie.util.Arguments;
import com.github.rccookie.xml.Node;

import static com.github.rccookie.math.rendering.RenderMode.*;

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
    public int precedence() {
        return Precedence.ROOT;
    }

    @Override
    public String renderInline(RenderOptions options) {
        String degree = this.degree.render(INLINE, options.setOutsidePrecedence(Precedence.MIN));
        if(degree.isEmpty() || degree.equals("2")) {
            if(options.charset.canDisplay("\u221A"))
                return "\u221A" + value.render(INLINE, options.setOutsidePrecedence(Precedence.MAX));
            if(value instanceof Brackets && ((Brackets) value).type == Bracket.ROUND)
                return "sqrt" + value.render(INLINE, options);
            return "sqrt(" + value.render(INLINE, options.setOutsidePrecedence(Precedence.MIN)) + ")";
        }
        String supDeg = Utils.toSuperscript(degree);
        if(supDeg != null && options.charset.canDisplay("\u221A" + supDeg))
            return supDeg + "\u221A" + value.render(INLINE, options.setOutsidePrecedence(Precedence.MAX));
        options = options.setOutsidePrecedence(Precedence.COMMA + 1);
        return "root("+this.degree.render(INLINE, options)+", "+value.render(INLINE, options)+")";
    }

    @Override
    public AsciiArt renderAsciiArt(RenderOptions options) {
        String leftDiag = options.charset.orFallback("\u2572", "\\");
        String rightDiag = options.charset.orFallback("\u2571", "/");
        String vert = options.charset.orFallback("\u2502", "|");

        options = options.setOutsidePrecedence(Precedence.MIN);
        AsciiArt degree = this.degree.render(ASCII_ART, options), value = this.value.render(ASCII_ART, options);

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
        options = options.setOutsidePrecedence(Precedence.MIN);
        return "\\sqrt["+degree.render(LATEX, options)+"]{"+value.render(LATEX, options)+"}";
    }

    @Override
    public Node renderMathMLNode(RenderOptions options) {
        options = options.setOutsidePrecedence(Precedence.MIN);
        Node degree = this.degree.render(MATH_ML_NODE, options);
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
