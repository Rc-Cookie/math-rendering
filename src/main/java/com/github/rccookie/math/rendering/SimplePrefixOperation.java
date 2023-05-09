package com.github.rccookie.math.rendering;

import com.github.rccookie.util.Arguments;
import com.github.rccookie.xml.Node;

final class SimplePrefixOperation implements RenderableExpression {

    final RenderableExpression value;
    final RenderableExpression symbol;

    SimplePrefixOperation(RenderableExpression value, RenderableExpression symbol) {
        this.value = Arguments.checkNull(value, "value");
        this.symbol = Arguments.checkNull(symbol, "symbol");
    }

    @Override
    public String toString() {
        return "prefix("+symbol.toString().trim()+", "+value+")";
    }

    @Override
    public String renderInline(RenderOptions options) {
        return symbol.renderInline(options) + value.renderInline(options);
    }

    @Override
    public AsciiArt renderAscii(RenderOptions options) {
        return symbol.renderAscii(options).appendCenter(value.renderAscii(options));
    }

    @Override
    public AsciiArt renderUnicode(RenderOptions options) {
        return symbol.renderUnicode(options).appendCenter(value.renderUnicode(options));
    }

    @Override
    public AsciiArt renderAscii(RenderOptions options, CharacterSet charset) {
        return symbol.renderAscii(options, charset).appendCenter(value.renderAscii(options, charset));
    }

    @Override
    public String renderLatex(RenderOptions options) {
        return symbol.renderLatex(options)+"{"+value.renderLatex(options)+"}";
    }

    @Override
    public Node renderMathMLNode(RenderOptions options) {
        return Utils.join(symbol.renderMathMLNode(options), value.renderMathMLNode(options));
    }
}
