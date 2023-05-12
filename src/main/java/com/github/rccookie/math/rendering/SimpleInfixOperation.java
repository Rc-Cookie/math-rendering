package com.github.rccookie.math.rendering;

import com.github.rccookie.util.Arguments;
import com.github.rccookie.xml.Node;

final class SimpleInfixOperation implements RenderableExpression {

    final RenderableExpression a;
    final RenderableExpression b;
    final RenderableExpression symbol;

    SimpleInfixOperation(RenderableExpression a, RenderableExpression b, RenderableExpression symbol) {
        this.a = Arguments.checkNull(a, "a");
        this.b = Arguments.checkNull(b, "b");
        this.symbol = Arguments.checkNull(symbol, "symbol");
    }

    @Override
    public String toString() {
        return symbol.toString().trim()+"("+a+", "+b+")";
    }

    @Override
    public String renderInline(RenderOptions options) {
        return a.renderInline(options) + symbol.renderInline(options) + b.renderInline(options);
    }

    @Override
    public AsciiArt renderAsciiArt(RenderOptions options) {
        AsciiArt a = this.a.renderAsciiArt(options), b = this.b.renderAsciiArt(options), symbol = this.symbol.renderAsciiArt(options);

        boolean spaces = (a.height() > 2 || b.height() > 2) && (symbol.height() != 1 || !hasPadding(symbol.toString()));
        AsciiArt art = a;
        if(spaces) art = art.appendTop(new AsciiArt(" "));
        art = art.appendCenter(symbol);
        if(spaces) art = art.appendTop(new AsciiArt(" "));
        return art.appendCenter(b);
    }

    private static boolean hasPadding(String s) {
        return s.length() >= 3 && s.charAt(0) == ' ' && s.charAt(s.length()-1) == ' ';
    }

    @Override
    public String renderLatex(RenderOptions options) {
        return a.renderLatex(options)+" "+symbol.renderLatex(options)+" "+b.renderLatex(options);
    }

    @Override
    public Node renderMathMLNode(RenderOptions options) {
        return Utils.join(a.renderMathMLNode(options), symbol.renderMathMLNode(options), b.renderMathMLNode(options));
    }
}
