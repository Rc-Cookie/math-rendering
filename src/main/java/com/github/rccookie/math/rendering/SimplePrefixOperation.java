package com.github.rccookie.math.rendering;

import com.github.rccookie.util.Arguments;

final class SimplePrefixOperation implements Expression {

    final Expression value;
    final Expression symbol;

    SimplePrefixOperation(Expression value, Expression symbol) {
        this.value = Arguments.checkNull(value, "value");
        this.symbol = Arguments.checkNull(symbol, "symbol");
    }

    @Override
    public String toString() {
        return "prefix("+symbol.renderInline().trim()+", "+value+")";
    }

    @Override
    public String renderInline() {
        return symbol.renderInline() + value.renderInline();
    }

    @Override
    public AsciiArt renderAscii() {
        return symbol.renderAscii().appendCenter(value.renderAscii(), true);
    }

    @Override
    public AsciiArt renderUnicode() {
        return symbol.renderUnicode().appendCenter(value.renderUnicode(), true);
    }

    @Override
    public String renderLatex() {
        return symbol.renderLatex()+"{"+value.renderLatex()+"}";
    }
}
