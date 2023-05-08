package com.github.rccookie.math.rendering;

import com.github.rccookie.util.Arguments;

final class SimplePostfixOperation implements Expression {

    final Expression value;
    final Expression symbol;

    SimplePostfixOperation(Expression value, Expression symbol) {
        this.value = Arguments.checkNull(value, "value");
        this.symbol = symbol;
    }

    @Override
    public String toString() {
        return "postfix("+symbol.renderInline().trim()+", "+value+")";
    }

    @Override
    public String renderInline() {
        return value.renderInline() + symbol.renderInline();
    }

    @Override
    public AsciiArt renderAscii() {
        return value.renderAscii().appendCenter(symbol.renderAscii(), false);
    }

    @Override
    public AsciiArt renderUnicode() {
        return value.renderUnicode().appendCenter(symbol.renderUnicode(), false);
    }

    @Override
    public String renderLatex() {
        return value.renderLatex() + symbol.renderLatex();
    }
}
