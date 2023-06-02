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
    final OperatorAlignment alignment;

    SimpleInfixOperation(RenderableExpression a, RenderableExpression b, RenderableExpression symbol, int precedence, boolean associative, OperatorAlignment alignment) {
        this.a = Arguments.checkNull(a, "a");
        this.b = Arguments.checkNull(b, "b");
        this.symbol = Arguments.checkNull(symbol, "symbol");
        this.precedence = precedence;
        this.associative = associative;
        this.alignment = Arguments.checkNull(alignment, "alignment");
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

        boolean spaces;
        if(options.spaceMode == RenderOptions.SpaceMode.COMPACT || Utils.hasPadding(symbol.toString())) spaces = false;
        else if(options.spaceMode == RenderOptions.SpaceMode.FORCE) spaces = true;
        else if(a.height() != 1 || b.height() != 1 || (a.width() > 2 && b.width() > 2)) spaces = true;
        else if(a.width() <= 2 && b.width() <= 2) spaces = false;
        else spaces = (a.width() != 1 && b.width() != 1) || a.toString().contains(" ") || b.toString().contains(" ");

        AsciiArt art = a;
        if(spaces) art = art.appendTop(new AsciiArt(" "));
        art = art.append(symbol, alignment, true);
        if(spaces) art = art.appendTop(new AsciiArt(" "));
        return art.appendCenter(b);
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
