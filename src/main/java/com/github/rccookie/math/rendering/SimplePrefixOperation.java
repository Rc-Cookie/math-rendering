package com.github.rccookie.math.rendering;

import com.github.rccookie.util.Arguments;
import com.github.rccookie.xml.Node;

import static com.github.rccookie.math.rendering.RenderMode.*;

class SimplePrefixOperation implements RenderableExpression {

    final RenderableExpression value;
    final RenderableExpression symbol;
    final int precedence;
    final OperatorAlignment alignment;

    SimplePrefixOperation(RenderableExpression value, RenderableExpression symbol, int precedence, OperatorAlignment alignment) {
        this.value = Arguments.checkNull(value, "value");
        this.symbol = Arguments.checkNull(symbol, "symbol");
        this.precedence = precedence;
        this.alignment = Arguments.checkNull(alignment, "alignment");
    }

    @Override
    public String toString() {
        return "prefix("+symbol.toString().trim()+", "+value+")";
    }

    @Override
    public int precedence() {
        return precedence;
    }

    @Override
    public String renderInline(RenderOptions options) {
        return symbol.render(INLINE, options) + value.render(INLINE, options.setOutsidePrecedence(Math.min(precedence, Integer.MAX_VALUE-1) + 1));
    }

    @Override
    public AsciiArt renderAsciiArt(RenderOptions options) {
        return symbol.render(ASCII_ART, options)
                .append(value.render(ASCII_ART, options.setOutsidePrecedence(Math.min(precedence, Integer.MAX_VALUE-1) + 1)), alignment, false);
    }

    @Override
    public String renderLatex(RenderOptions options) {
        return symbol.render(LATEX, options)+"{"+value.render(LATEX, options.setOutsidePrecedence(Math.min(precedence, Integer.MAX_VALUE-1) + 1))+"}";
    }

    @Override
    public Node renderMathMLNode(RenderOptions options) {
        return Utils.join(symbol.render(MATH_ML_NODE, options), value.render(MATH_ML_NODE, options.setOutsidePrecedence(Math.min(precedence, Integer.MAX_VALUE-1) + 1)));
    }
}
