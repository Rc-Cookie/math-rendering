package com.github.rccookie.math.rendering;

import com.github.rccookie.primitive.int2;
import com.github.rccookie.util.Arguments;
import com.github.rccookie.xml.Node;

final class Fraction implements Expression {

    final Expression a;
    final Expression b;

    Fraction(Expression a, Expression b) {
        this.a = Arguments.checkNull(a, "a");
        this.b = Arguments.checkNull(b, "b");
    }

    @Override
    public String toString() {
        return "frac("+a+", "+b+")";
    }

    @Override
    public String renderInline() {
        return Utils.encapsulate(a.renderInline())+"/"+Utils.encapsulate(b.renderInline());
    }

    @Override
    public AsciiArt renderAscii() {
        return renderFraction(a.renderAscii(), b.renderAscii());
    }

    @Override
    public AsciiArt renderUnicode() {
        return renderFraction(a.renderUnicode(), b.renderUnicode());
    }

    @Override
    public AsciiArt renderAscii(CharacterSet charset) {
        return renderFraction(a.renderAscii(charset), b.renderAscii(charset));
    }

    private static AsciiArt renderFraction(AsciiArt a, AsciiArt b) {
        AsciiArt bar = new AsciiArt("-".repeat(Math.max(a.width(), b.width()) + (a.height() + b.height() > 2 ? 2 : 0)));
        int aPos = (bar.width() - a.width() + 1) / 2;
        int bPos = (bar.width() - b.width() + 1) / 2;
        return bar.draw(b, new int2(bPos, 1)).draw(a, new int2(aPos, -a.height()));
    }

    @Override
    public String renderLatex() {
        return "\\frac{"+a.renderLatex()+"}{"+b.renderLatex()+"}";
    }

    @Override
    public Node renderMathMLNode() {
        Node frac = new Node("mfrac");
        frac.children.add(a.renderMathMLNode());
        frac.children.add(b.renderMathMLNode());
        return frac;
    }
}
