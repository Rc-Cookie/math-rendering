package com.github.rccookie.math.rendering;

import com.github.rccookie.util.Arguments;

final class SimpleInfixOperation implements Expression {

    final Expression a;
    final Expression b;
    final Expression symbol;

    SimpleInfixOperation(Expression a, Expression b, Expression symbol) {
        this.a = Arguments.checkNull(a, "a");
        this.b = Arguments.checkNull(b, "b");
        this.symbol = Arguments.checkNull(symbol, "symbol");
    }

    @Override
    public String toString() {
        return symbol.renderInline().trim()+"("+a+", "+b+")";
    }

    @Override
    public String renderInline() {
        return a.renderInline() + symbol.renderInline() + b.renderInline();
    }

    @Override
    public AsciiArt renderAscii() {
        return renderArt(a.renderAscii(), b.renderAscii(), symbol.renderAscii());
    }

    @Override
    public AsciiArt renderUnicode() {
        return renderArt(a.renderUnicode(), b.renderUnicode(), symbol.renderUnicode());
    }

    private static AsciiArt renderArt(AsciiArt a, AsciiArt b, AsciiArt symbol) {
        boolean spaces = (a.height() > 2 || b.height() > 2) && (symbol.height() != 1 || !hasPadding(symbol.toString()));
        AsciiArt art = a;
        if(spaces) art = art.appendTop(new AsciiArt(" "));
        art = art.appendCenter(symbol, false);
        if(spaces) art = art.appendTop(new AsciiArt(" "));
        return art.appendCenter(b, false);
    }

    private static boolean hasPadding(String s) {
        return s.length() >= 3 && s.charAt(0) == ' ' && s.charAt(s.length()-1) == ' ';
    }

    @Override
    public String renderLatex() {
        return a.renderLatex()+" "+symbol.renderLatex()+" "+b.renderLatex();
    }
}
