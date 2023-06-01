package com.github.rccookie.math.rendering;

import com.github.rccookie.util.Arguments;
import com.github.rccookie.xml.Node;

import static com.github.rccookie.math.rendering.RenderMode.*;

final class SimpleInfixOperation implements RenderableExpression {

    final RenderableExpression a;
    final RenderableExpression b;
    final RenderableExpression symbol;
    final int precedence;
    final boolean associative;

    SimpleInfixOperation(RenderableExpression a, RenderableExpression b, RenderableExpression symbol, int precedence, boolean associative) {
        this.a = Arguments.checkNull(a, "a");
        this.b = Arguments.checkNull(b, "b");
        this.symbol = Arguments.checkNull(symbol, "symbol");
        this.precedence = precedence;
        this.associative = associative;
    }

    @Override
    public String toString() {
        return symbol.toString().trim()+"("+a+", "+b+")";
    }

    @Override
    public int precedence() {
        return precedence;
    }

    @Override
    public String renderInline(RenderOptions options) {
        return a.render(INLINE, leftOptions(options)) + symbol.render(INLINE, options) + b.render(INLINE, rightOptions(options));
    }

    @Override
    public AsciiArt renderAsciiArt(RenderOptions options) {
        AsciiArt a = this.a.render(ASCII_ART, leftOptions(options)), b = this.b.render(ASCII_ART, rightOptions(options)), symbol = this.symbol.render(ASCII_ART, options);

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
        return a.render(LATEX, leftOptions(options))+" "+symbol.render(LATEX, options)+" "+b.render(LATEX, rightOptions(options));
    }

    @Override
    public Node renderMathMLNode(RenderOptions options) {
        return Utils.join(a.render(MATH_ML_NODE, leftOptions(options)), symbol.render(MATH_ML_NODE, options), b.render(MATH_ML_NODE, rightOptions(options)));
    }


    private RenderOptions leftOptions(RenderOptions options) {
        return options.setOutsidePrecedence(precedence);
    }

    private RenderOptions rightOptions(RenderOptions options) {
        return options.setOutsidePrecedence(associative ? precedence : (precedence + 1));
    }
}
