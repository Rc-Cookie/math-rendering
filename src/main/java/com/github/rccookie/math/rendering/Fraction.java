package com.github.rccookie.math.rendering;

import com.github.rccookie.primitive.int2;
import com.github.rccookie.util.Arguments;
import com.github.rccookie.xml.Node;

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
    public String renderInline(RenderOptions options) {
        return Utils.encapsulate(a.renderInline(options))+"/"+Utils.encapsulate(b.renderInline(options));
    }

    @Override
    public AsciiArt renderAscii(RenderOptions options) {
        return renderFraction(a.renderAscii(options), b.renderAscii(options), options);
    }

    @Override
    public AsciiArt renderUnicode(RenderOptions options) {
        return renderFraction(a.renderUnicode(options), b.renderUnicode(options), options);
    }

    @Override
    public AsciiArt renderAscii(RenderOptions options, CharacterSet charset) {
        return renderFraction(a.renderAscii(options, charset), b.renderAscii(options, charset), options);
    }

    private static AsciiArt renderFraction(AsciiArt a, AsciiArt b, RenderOptions options) {
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
        return "\\frac{"+a.renderLatex(options)+"}{"+b.renderLatex(options)+"}";
    }

    @Override
    public Node renderMathMLNode(RenderOptions options) {
        Node frac = new Node("mfrac");
        frac.children.add(a.renderMathMLNode(options));
        frac.children.add(b.renderMathMLNode(options));
        return frac;
    }
}
