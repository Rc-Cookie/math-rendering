package com.github.rccookie.math.rendering;

import com.github.rccookie.util.Arguments;
import com.github.rccookie.xml.Node;

final class SimplePostfixOperation implements RenderableExpression {

    final RenderableExpression value;
    final RenderableExpression symbol;

    SimplePostfixOperation(RenderableExpression value, RenderableExpression symbol) {
        this.value = Arguments.checkNull(value, "value");
        this.symbol = symbol;
    }

    @Override
    public String toString() {
        return "postfix("+symbol.toString().trim()+", "+value+")";
    }

    @Override
    public String renderInline(RenderOptions options) {
        return value.renderInline(options) + symbol.renderInline(options);
    }

    @Override
    public AsciiArt renderAsciiArt(RenderOptions options) {
        return value.renderAsciiArt(options).appendCenter(symbol.renderAsciiArt(options));
    }

    @Override
    public String renderLatex(RenderOptions options) {
        return value.renderLatex(options) + symbol.renderLatex(options);
    }

    @Override
    public Node renderMathMLNode(RenderOptions options) {
        return Utils.join(value.renderMathMLNode(options), symbol.renderMathMLNode(options));
    }
}
