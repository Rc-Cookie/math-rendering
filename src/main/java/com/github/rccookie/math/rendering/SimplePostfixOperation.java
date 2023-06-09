package com.github.rccookie.math.rendering;

import com.github.rccookie.util.Arguments;
import com.github.rccookie.xml.Node;

import static com.github.rccookie.math.rendering.RenderMode.*;

final class SimplePostfixOperation implements RenderableExpression {

    final RenderableExpression value;
    final RenderableExpression symbol;
    final int precedence;
    final OperatorAlignment alignment;

    SimplePostfixOperation(RenderableExpression value, RenderableExpression symbol, int precedence, OperatorAlignment alignment) {
        this.value = Arguments.checkNull(value, "value");
        this.symbol = symbol;
        this.precedence = precedence;
        this.alignment = Arguments.checkNull(alignment, "alignment");
    }

    @Override
    public String toString() {
        return "postfix("+symbol.toString().trim()+", "+value+")";
    }

    @Override
    public int precedence() {
        return precedence;
    }

    @Override
    public String renderInline(RenderOptions options) {
        return value.render(INLINE, options.setOutsidePrecedence(Math.min(precedence, Integer.MAX_VALUE-1) + 1)) + symbol.render(INLINE, options);
    }

    @Override
    public AsciiArt renderAsciiArt(RenderOptions options) {
        AsciiArt value = this.value.render(ASCII_ART, options.setOutsidePrecedence(Math.min(precedence, Integer.MAX_VALUE-1) + 1)),
                symbol = this.symbol.render(ASCII_ART, options);
        return value.append(symbol, alignment, true);
    }

    @Override
    public String renderLatex(RenderOptions options) {
        return value.render(LATEX, options.setOutsidePrecedence(Math.min(precedence, Integer.MAX_VALUE-1) + 1)) + symbol.render(LATEX, options);
    }

    @Override
    public Node renderMathMLNode(RenderOptions options) {
        return Utils.join(value.render(MATH_ML_NODE, options.setOutsidePrecedence(Math.min(precedence, Integer.MAX_VALUE-1) + 1)), symbol.render(MATH_ML_NODE, options));
    }
}
