package com.github.rccookie.math.rendering;

import com.github.rccookie.util.Arguments;

final class Middle implements Expression {

    private final Expression a, b;

    Middle(Expression a, Expression b) {
        this.a = Arguments.checkNull(a, "a");
        this.b = Arguments.checkNull(b, "b");
    }

    @Override
    public String toString() {
        return "mid("+a+", "+b+")";
    }

    @Override
    public String renderInline() {
        return a.renderInline() + " | " + b.renderInline();
    }

    @Override
    public AsciiArt renderAscii() {
        return renderArt(a.renderAscii(), b.renderAscii(), "|");
    }

    @Override
    public AsciiArt renderUnicode() {
        return renderArt(a.renderUnicode(), b.renderUnicode(), "\u2502");
    }

    private static AsciiArt renderArt(AsciiArt a, AsciiArt b, String bar) {
        int height = Math.max(a.center(), b.center()), depth = Math.max(a.height()-a.center(), b.height()-b.center());
        int totalHeight = height + depth;
        AsciiArt barArt = new AsciiArt(totalHeight, i -> i==totalHeight-1 ? " | " : " "+bar+" ").setCenter(height);
        return a.appendCenter(barArt).appendCenter(b);
    }

    @Override
    public String renderLatex() {
        return "\\left."+a.renderLatex()+"\\;\\middle|\\;"+b.renderLatex()+"\\right.";
    }
}
