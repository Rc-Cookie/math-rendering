package com.github.rccookie.math.rendering;

import com.github.rccookie.math.Precedence;
import com.github.rccookie.primitive.int2;
import com.github.rccookie.util.Arguments;
import com.github.rccookie.xml.Node;

import static com.github.rccookie.math.rendering.RenderMode.*;

final class Fraction implements RenderableExpression {

    final RenderableExpression a;
    final RenderableExpression b;

    Fraction(RenderableExpression a, RenderableExpression b) {
        this.a = Arguments.checkNull(a, "a");
        this.b = Arguments.checkNull(b, "b");
    }

    @Override
    public String toString() {
        return "frac("+a+", "+b+")";
    }

    @Override
    public int precedence() {
        return Precedence.FRACTION;
    }

    @Override
    public String renderInline(RenderOptions options) {
        return a.render(INLINE, options.setOutsidePrecedence(Precedence.DIVIDE))+"/"+b.render(INLINE, options.setOutsidePrecedence(Precedence.DIVIDE+1));
    }

    @Override
    public AsciiArt renderAsciiArt(RenderOptions options) {
        AsciiArt a = this.a.render(ASCII_ART, options.setOutsidePrecedence(Precedence.MIN));
        AsciiArt b = this.b.render(ASCII_ART, options.setOutsidePrecedence(Precedence.MIN));

        if(shouldRenderInline(a, b, options))
            return a.appendCenter(new AsciiArt("/")).appendCenter(b);

        AsciiArt bar = new AsciiArt("-".repeat(Math.max(a.width(), b.width()) + (a.height() + b.height() > 2 ? 2 : 0)));
        int aPos = (bar.width() - a.width() + 1) / 2;
        int bPos = (bar.width() - b.width() + 1) / 2;
        return bar.draw(b, new int2(bPos, 1)).draw(a, new int2(aPos, -a.height()));
    }

    private static boolean shouldRenderInline(AsciiArt a, AsciiArt b, RenderOptions options) {
        return a.height() == 1 && b.height() == 1 &&
                a.width() < options.smallFractionsSizeLimit && b.width() < options.smallFractionsSizeLimit &&
                Utils.oneNumOrVar(a.toString()) && Utils.oneNumOrVar(b.toString());
    }

    @Override
    public String renderLatex(RenderOptions options) {
        options = options.setOutsidePrecedence(Precedence.MIN);
        return "\\frac{"+a.render(LATEX, options)+"}{"+b.render(LATEX, options)+"}";
    }

    @Override
    public Node renderMathMLNode(RenderOptions options) {
        options = options.setOutsidePrecedence(Precedence.MIN);
        Node frac = new Node("mfrac");
        frac.children.add(a.renderMathMLNode(options));
        frac.children.add(b.renderMathMLNode(options));
        return frac;
    }
}
