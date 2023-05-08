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
        int height = Math.max(a.height(), b.height());
        return a.appendCenter(new AsciiArt(height, i -> i==height-1 ? " | " : " "+bar+" "), true).appendCenter(b, false);
    }

    @Override
    public String renderLatex() {
        return "\\left."+a.renderLatex()+"\\;\\middle|\\;"+b.renderLatex()+"\\right.";
    }
}
